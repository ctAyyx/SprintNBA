package com.ct.sprintnba_demo01.music;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mutils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/2/20.
 */

public abstract class DownOnlineMusic extends DownMusic {
    private SongEntity entity;


    public DownOnlineMusic(Context mContext, SongEntity entity) {
        super(mContext);
        this.entity = entity;
    }

    @Override
    protected void doownload() {
        //获取下载歌曲信息
        Call<BaseMusicResponse> call = NetService.getApiServiceMusic(mContext).getMusic(entity.song_id);
        call.enqueue(new Callback<BaseMusicResponse>() {
            @Override
            public void onResponse(Call<BaseMusicResponse> call, Response<BaseMusicResponse> response) {
                if (response.body() == null)
                    onFailure(call, new NullPointerException("获取歌曲链接失败！"));
                Bitrate bitrate = response.body().bitrate;
                if (bitrate != null) {
                    downMusic(bitrate.file_link, entity.artist_name, entity.title);
                    onSuccess(bitrate);
                    String lrcFileName = FileUtils.getLrcFileName(entity.artist_name, entity.title);
                    File file = new File(FileUtils.getLrcDir(), lrcFileName);
                    //判断歌词是否存在
                    if (!file.exists() && !TextUtils.isEmpty(entity.lrclink))
                        downLrc(lrcFileName);
                } else {
                    Toast.makeText(mContext, "下载失败！", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<BaseMusicResponse> call, Throwable t) {
                Toast.makeText(mContext, "下载失败！", Toast.LENGTH_LONG).show();
                Log.i("TAG", "获取歌曲链接失败" + t.toString());
                return;
            }
        });
    }

    /**
     * 下载歌词
     *
     * @param lrcName 保存歌词名
     */
    private void downLrc(final String lrcName) {
        Call<ResponseBody> call = NetService.getApiServiceMusic(mContext).downMusicLyric(entity.lrclink);

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
                Toast.makeText(mContext, "歌词下载出错，请重试！", Toast.LENGTH_LONG).show();
                Log.i("TAG", "歌词下载出错" + t.toString());
            }
        });
    }

}
