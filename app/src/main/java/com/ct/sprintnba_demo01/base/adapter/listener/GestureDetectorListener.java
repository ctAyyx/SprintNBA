package com.ct.sprintnba_demo01.base.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ct on 2017/3/10.
 * ===============================
 * 手势探测监听器
 * ==============================
 * SimpleOnGestureListener实际是实现了
 * OnGestureListener
 * OnDoubleTapListener
 * ===============================
 */

public class GestureDetectorListener extends GestureDetector.SimpleOnGestureListener {

    private RecyclerView recyclerView;
    private OnRvItemTouchHelpListener mlistener;

    public GestureDetectorListener(RecyclerView recyclerView, OnRvItemTouchHelpListener mlistener) {
        this.recyclerView = recyclerView;
        this.mlistener = mlistener;
    }

    /********************************
     * OnGestureListener中的方法    *
     ********************************/

    //用户按下屏幕就会触发
    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    //如果是按下的事件超过瞬间，而且在按下的时候没有松开或者是拖动。
    @Override
    public void onShowPress(MotionEvent e) {
        super.onShowPress(e);
    }

    //一次单独的轻击抬起操作,就是普通的点击事件
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());

        if (child != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
            if (mlistener != null)
                mlistener.onItemClick(holder);
        }

        return true;
    }

    //在屏幕上拖动事件
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return super.onScroll(e1, e2, distanceX, distanceY);
    }

    //长按屏幕，超过一定时长
    @Override
    public void onLongPress(MotionEvent e) {
        super.onLongPress(e);

        View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (child != null) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(child);
            if (mlistener != null)
                mlistener.onItemLongClick(holder);
        }

    }

    //滑屏 用户按下触摸屏，快速移动后松开
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return super.onFling(e1, e2, velocityX, velocityY);
    }


    /*********************************
     * OnDoubleTapListener中的方法   *
     *********************************/

    //单击事件。用来判断该次点击是SingTap而不是DoubleTap,
    //如果连续点击两次就是DoubleTap,如果点击一次，
    //系统等待一段时间后没有收到第二次点击则判定为SingTap而不是DoubleTap
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return super.onSingleTapConfirmed(e);
    }


    //双击事件
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return super.onDoubleTap(e);
    }

    //双击间隔中发生的动作，指触发onDoubletap以后，在双击之间发生的其它动作
    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return super.onDoubleTapEvent(e);
    }


}
