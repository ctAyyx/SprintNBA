package com.ct.sprintnba_demo01.mhandler;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ScrollView;


import com.ct.sprintnba_demo01.base.net.NetService;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static com.ct.sprintnba_demo01.base.net.NetConstant.ConnectionTime;

/**
 * Created by ct on 2016/12/27
 * =============================================.
 * 基础上拉刷新下拉加载管理器
 * =============================================.
 * 实现是否能上拉刷新下拉加载判断
 * =============================================.
 */

public abstract class BasePtrHandler extends PtrDefaultHandler2 {

    private Boolean canLaodMore = true;
    private Boolean canRefresh = true;
    private PtrFrameLayout loadFrame, refreshFrame;
    private PtrHander hander;

    public BasePtrHandler() {
        hander = new PtrHander();
    }

    @Override
    public void onLoadMoreBegin(PtrFrameLayout frame) {
        loadFrame = frame;
        onLoadMoreBegin();
        post(ConnectionTime);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        refreshFrame = frame;
        onRefreshBegin();
        post(ConnectionTime);
    }

    @Override
    public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
        if (content instanceof FrameLayout) {
            FrameLayout layout = (FrameLayout) content;
            ViewGroup view = (ViewGroup) layout.getChildAt(0);
            View newContent = findView(view);
            if (newContent == null)
                return false;
            if (super.checkCanDoLoadMore(frame, newContent, footer))
                return canLaodMore;
            return false;
        } else {
            return super.checkCanDoLoadMore(frame, content, footer);
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (content instanceof FrameLayout) {
            FrameLayout layout = (FrameLayout) content;
            ViewGroup view = (ViewGroup) layout.getChildAt(0);
            View newContent = findView(view);
            if (newContent == null)
                return false;
            if (super.checkCanDoRefresh(frame, newContent, header))
                return canRefresh;
            return false;
        } else {
            return super.checkCanDoRefresh(frame, content, header);
        }
    }

    public abstract void onLoadMoreBegin();

    public abstract void onRefreshBegin();

    /**
     * 刷新加载计时器
     * 指定时间停止刷新或加载
     */
    private void post(long millTime) {
        if (hander == null)
            return;
        hander.sendEmptyMessageDelayed(PtrHander.STOP, millTime);
    }

    /**
     * 立即停止刷新或加载
     */
    public void stop() {
        if (hander == null)
            return;
        hander.sendEmptyMessage(PtrHander.STOP);
    }

    private View findView(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if (view instanceof AbsListView)
                return view;
            else if (view instanceof ScrollView)
                return view;
            else if (view instanceof RecyclerView)
                return view;

           /* else if (view instanceof ViewGroup)
                findView((ViewGroup) view);*/
        }
        return null;
    }

    public void setCanLoadMore(Boolean canLaodMore) {
        this.canLaodMore = canLaodMore;
    }

    public void setCanRefresh(Boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    class PtrHander extends Handler {
        static final int STOP = 1;

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP:
                    if (loadFrame != null)
                        loadFrame.refreshComplete();
                    if (refreshFrame != null)
                        refreshFrame.refreshComplete();
                    break;
            }
        }
    }
}
