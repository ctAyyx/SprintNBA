package com.ct.sprintnba_demo01.mview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ct on 2017/1/13.
 * ===============================
 * 可以禁止该ViewPager的所有Touch事件
 * ===============================
 */

public class  XViewPager extends ViewPager {
    private boolean isEnableScroll = true;

    public void setEnableScroll(boolean isEnableScroll) {
        this.isEnableScroll = isEnableScroll;
    }

    public XViewPager(Context context) {
        super(context);
    }

    public XViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnableScroll)
            return false;
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnableScroll)
            return false;
        return super.onTouchEvent(ev);
    }
}
