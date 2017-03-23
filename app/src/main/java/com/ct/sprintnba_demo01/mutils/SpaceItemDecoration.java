package com.ct.sprintnba_demo01.mutils;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ct.sprintnba_demo01.base.utils.DeviceUtils;

/**
 * Created by ct on 2017/2/10.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int px;

    public SpaceItemDecoration(Context context, int dp) {
        this.px = DeviceUtils.dp2px(context, dp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = px;
        //  super.getItemOffsets(outRect, view, parent, state);


    }
}
