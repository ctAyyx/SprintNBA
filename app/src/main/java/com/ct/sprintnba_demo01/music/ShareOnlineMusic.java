package com.ct.sprintnba_demo01.music;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.SongEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/2/20.
 */

public abstract class ShareOnlineMusic extends ShareMusic {
    private SongEntity entity;

    public ShareOnlineMusic(Context mContext, SongEntity entity) {
        super(mContext);
        this.entity = entity;
    }

    @Override
    public void getMusic() {
        Call<BaseMusicResponse> call = NetService.getApiServiceMusic(mContext).getMusic(entity.song_id);
        call.enqueue(new Callback<BaseMusicResponse>() {
            @Override
            public void onResponse(Call<BaseMusicResponse> call, Response<BaseMusicResponse> response) {
                if (response.body() == null)
                    onFailure(call, new NullPointerException("获取歌曲链接失败！"));

                Bitrate bitrate = response.body().bitrate;
                if (bitrate != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "我用SprintNBA分享了歌曲" + entity.title + bitrate.file_link);
                    mContext.startActivity(Intent.createChooser(intent, "分享"));
                    onSuccess(bitrate);

                } else {
                    Toast.makeText(mContext, "分享失败！", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<BaseMusicResponse> call, Throwable t) {
                Toast.makeText(mContext, "分享失败！", Toast.LENGTH_LONG).show();
                Log.i("TAG", "获取歌曲链接失败" + t.toString());
                return;
            }
        });
    }
}
