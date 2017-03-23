package com.ct.sprintnba_demo01.music;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.minterface.MusicPlayInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/2/20.
 */

public abstract class ShareMusic implements MusicPlayInterface<Bitrate> {
    protected Context mContext;


    public ShareMusic(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void execute() {
        shareWrapper();
    }

    private void shareWrapper() {
        onPrepare();
        getMusic();
    }


    public abstract void getMusic();

    ;


}
