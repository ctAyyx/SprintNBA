package com.ct.sprintnba_demo01.base;


import com.ct.sprintnba_demo01.base.response.BaseMMResponse;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.BaseResponse;

/**
 * 视图控制接口
 * <p>
 * Created by Administrator on 16-8-23.
 */
public interface IView<T> {

    /**
     * 初始化视图
     */
    //  void initView();

    /**
     * 通知View更新视图
     * <p>
     * 当数据放生改变时触发
     *
     * @param reqId    标志
     * @param response 数据
     */
    void onUpdateView(int reqId, T response);

  /*  *//**
     * 更新NBA资源
     * * @param reqId    标志
     *
     * @param response 数据
     *//*
    void onUpdateViewNBA(int reqId, BaseNBAResponse response);

    *//**
     * 更新美女资源
     *
     * @param reqId    标志
     * @param response 数据
     *//*
    void onUpdateViewMM(int reqId, BaseMMResponse response);

    void onUpdateViewMusic(int reqId, BaseMusicResponse response);*/
}
