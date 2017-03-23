package com.ct.sprintnba_demo01.base.adapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by ct on 2017/3/10.
 * ===================================
 * 关于 RecyclerView的Item点击事件监听
 * ===================================
 */

public class OnRvItemTouchListener implements RecyclerView.OnItemTouchListener {
    private RecyclerView recyclerView;
    private GestureDetector detector;

    public OnRvItemTouchListener(RecyclerView recyclerView, OnRvItemTouchHelpListener listener) {
        this.recyclerView = recyclerView;
        detector = new GestureDetector(recyclerView.getContext(), new GestureDetectorListener(this.recyclerView, listener));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        detector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        detector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
