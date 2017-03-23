package com.ct.sprintnba_demo01.madapter.callback;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2017/3/13.
 */

public interface OnItemMoveListener {
    /**
     * 当Item移动时调用
     */
    void onMove(int fromPosition, int toPosition);




}
