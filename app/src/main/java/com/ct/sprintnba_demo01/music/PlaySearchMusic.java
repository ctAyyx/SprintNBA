package com.ct.sprintnba_demo01.music;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.response.MusicLrcResponse;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.QuerySongEntity;
import com.ct.sprintnba_demo01.mutils.FileUtils;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/3/1.
 */

public abstract class PlaySearchMusic extends PlayMusic {
    private QuerySongEntity entity;

    public PlaySearchMusic(Activity activity, QuerySongEntity entity) {
        super(activity, 2);
        this.entity = entity;
    }

    @Override
    protected void getPlayInfo() {
        music = new Music();

        String lrcFileName = FileUtils.getLrcFileName(entity.artistname, entity.songname);
        File file = new File(FileUtils.getLrcDir(), lrcFileName);
        //判断歌词是否存在
        if (!file.exists())
            downLrc(file.getPath(), entity.songid);
        else
            mCounter++;

        music.type = Music.Type.ONLINE;
        music.artist = entity.artistname;
        music.title = entity.songname;
        music.album = entity.songname;
        getMusic(entity.songid);

    }


    private void downLrc(final String lrcFileName, String songId) {
        Call<MusicLrcResponse> call = NetService.getApiServiceMusic(mActivity).downSearchLrc(songId);
        call.enqueue(new Callback<MusicLrcResponse>() {
            @Override
            public void onResponse(Call<MusicLrcResponse> call, Response<MusicLrcResponse> response) {
                if (response == null)
                    return;
                final String lrcContent = response.body().lrcContent;
                new Thread() {
                    @Override
                    public void run() {
                        FileUtils.saveLrcFile(lrcFileName, lrcContent);
                    }
                }.start();
                checkCounter();
            }

            @Override
            public void onFailure(Call<MusicLrcResponse> call, Throwable t) {

            }
        });

    }

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
                return;
            }
        });
    }
}
