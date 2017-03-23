package com.ct.sprintnba_demo01.music;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mutils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/2/15.
 * =========================
 * 播放在线音乐
 * =========================
 */

public abstract class PlayOnlineMusic extends PlayMusic {
    private SongEntity mEntity;


    public PlayOnlineMusic(Activity activity, SongEntity entity) {
        super(activity, 3);
        this.mEntity = entity;
    }


    /**
     * 获取播放的音乐信息 封装成Music实体
     */
    @Override
    protected void getPlayInfo() {
        music = new Music();

        String lrcFileName = FileUtils.getLrcFileName(mEntity.artist_name, mEntity.title);
        File file = new File(FileUtils.getLrcDir(), lrcFileName);
        //判断歌词是否存在
        if (!file.exists() && !TextUtils.isEmpty(mEntity.lrclink))
            downLrc(lrcFileName);
        else
            mCounter++;

        //判断专辑图片是否存在
        String picUrl = mEntity.pic_big;
        if (TextUtils.isEmpty(picUrl))
            picUrl = mEntity.pic_small;
        if (!TextUtils.isEmpty(picUrl))//存在则下载
            downAlbum(picUrl);
        else
            mCounter++;


        music.type = Music.Type.ONLINE;
        music.title = mEntity.title;
        music.artist = mEntity.artist_name;
        music.album = mEntity.album_title;

        //获取歌曲连接
        getMusic(mEntity.song_id);

    }

    /**
     * 下载歌词
     *
     * @param lrcName 保存歌词名
     */
    private void downLrc(final String lrcName) {
        Call<ResponseBody> call = NetService.getApiServiceMusic(mActivity).downMusicLyric(mEntity.lrclink);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(final Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.body() == null)
                    onFailure(call, new NullPointerException("歌词数据为空"));

                new Thread() {
                    @Override
                    public void run() {
                        InputStream is = null;
                        byte[] buf = new byte[2048];
                        int len;
                        FileOutputStream fos = null;

                        try {
                            //下载并保存歌词
                            is = response.body().byteStream();
                            final long total = response.body().contentLength();
                            File dir = new File(FileUtils.getLrcDir());
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            File file = new File(dir, lrcName);
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            checkCounter();

                        } catch (Exception e) {
                            onFailure(call, e);
                        } finally {
                            try {
                                if (is != null) is.close();
                            } catch (IOException e) {
                            }
                            try {
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }.start();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("TAG", "歌词下载出错" + t.toString());
                return;
            }
        });
    }


    /**
     * 下载专辑图片
     */
    private void downAlbum(String picUrl) {

        Call<ResponseBody> call = NetService.getApiServiceMusic(mActivity).downMusicImg(picUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.body() == null)
                    onFailure(call, new NullPointerException("专辑图片下载失败！"));

                new Thread() {
                    @Override
                    public void run() {
                        music.cover = BitmapFactory.decodeStream(response.body().byteStream());
                    }
                }.start();
                checkCounter();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("TAG", "专辑图片下载失败" + t.toString());
            }
        });


    }

    /**
     * 获取播放歌曲的连接
     */
    private void getMusic(final String songId) {
        Call<BaseMusicResponse> call = NetService.getApiServiceMusic(mActivity).getMusic(songId);
        call.enqueue(new Callback<BaseMusicResponse>() {
            @Override
            public void onResponse(Call<BaseMusicResponse> call, Response<BaseMusicResponse> response) {
                if (response.body() == null)
                    onFailure(call, new NullPointerException("获取歌曲链接失败！"));

                Bitrate bitrate = response.body().bitrate;
                if (bitrate != null) {
                    music.uri = bitrate.file_link;
                    music.duration = bitrate.file_duration * 1000;
                    checkCounter();
                } else {
                    Log.i("TAG", "获取该歌曲失败！" + songId);
                }

            }

            @Override
            public void onFailure(Call<BaseMusicResponse> call, Throwable t) {
                Log.i("TAG", "获取歌曲链接失败" + t.toString());
            }
        });
    }
}
