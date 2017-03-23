package com.ct.sprintnba_demo01.music;

import android.app.Activity;

import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.minterface.MusicPlayInterface;

/**
 * Created by ct on 2017/2/15.
 * ============================
 * 音乐播放
 * <p>
 * ============================
 */

public abstract class PlayMusic implements MusicPlayInterface<Music> {
    protected Activity mActivity;
    protected Music music;
    protected int mCounter;  //当前进行步骤
    private int mTotalStep;//总的 步骤数

    public PlayMusic(Activity activity, int totalStep) {
        this.mActivity = activity;
        this.mTotalStep = totalStep;

    }


    @Override
    public void execute() {
        checkNetWork();
    }

    /**
     * 判断网络连接
     */
    private void checkNetWork() {
        //判断网络是否连接
        //判断当前网络是否为移动网络

        getPlayInfoWapper();
    }

    private void getPlayInfoWapper() {
        onPrepare();
        getPlayInfo();
    }

    protected abstract void getPlayInfo();

    public void checkCounter() {
        mCounter++;
        if (mCounter == mTotalStep)
            onSuccess(music);
    }
}
