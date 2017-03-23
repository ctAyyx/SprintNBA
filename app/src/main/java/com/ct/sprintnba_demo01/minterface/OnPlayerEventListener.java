package com.ct.sprintnba_demo01.minterface;

import com.ct.sprintnba_demo01.mentity.Music;

/**
 * Created by ct on 2017/2/15.
 * =========================
 * 播放进度监听
 * =========================
 */

public interface OnPlayerEventListener {

    /**
     * 更新进度
     */
    void onPublish(int progress);

    /**
     * 切换歌曲
     */
    void onChange(Music music);

    /**
     * 暂停播放
     */
    void onPlayerPause();

    /**
     * 继续播放
     */
    void onPlayerResume();

    /**
     * 更新定时停止播放时间
     */
    void onTimer(long remain);
}
