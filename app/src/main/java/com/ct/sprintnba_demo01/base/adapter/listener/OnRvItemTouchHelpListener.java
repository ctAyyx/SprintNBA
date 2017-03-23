package com.ct.sprintnba_demo01.base.adapter.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by ct on 2017/3/10.
 * ================================
 * RecyclerView Item点击事件帮助接口
 * ================================
 */

public interface OnRvItemTouchHelpListener {

    void onItemClick(RecyclerView.ViewHolder holder);

    void onItemLongClick(RecyclerView.ViewHolder holder);

    void onItemDoubleClick(RecyclerView.ViewHolder holder);

    void onScroll(RecyclerView.ViewHolder holder);
}
