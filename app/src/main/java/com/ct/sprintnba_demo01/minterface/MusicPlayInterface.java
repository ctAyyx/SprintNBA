package com.ct.sprintnba_demo01.minterface;

/**
 * Created by ct on 2017/2/15.
 * ==========================
 * 音乐播放接口
 * ==========================
 */

public interface MusicPlayInterface<T> {

    void execute();

    void onPrepare();

    void onSuccess(T t);

    void onFail(Exception e);
}
