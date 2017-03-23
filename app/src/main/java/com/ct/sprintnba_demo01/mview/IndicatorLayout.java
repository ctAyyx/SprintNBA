package com.ct.sprintnba_demo01.mview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;

/**
 * Created by ct on 2017/2/16.
 * <p>
 * =============================
 * 指示器布局
 * 用于音乐播放界面的小圆点
 * =============================
 */

public class IndicatorLayout extends LinearLayout {
    public IndicatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IndicatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorLayout(Context context) {
        this(context, null);
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }


    public void onCreate(int count) {
        if (count < 0)
            return;

        for (int i = 0; i < count; i++) {
            ImageView img = new ImageView(getContext());
            img.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            int padding = DeviceUtils.px2dp(getContext(), 2);
            img.setPadding(padding, 0, padding, 0);
            img.setImageResource(i == 0 ? R.drawable.ic_play_page_indicator_selected : R.drawable.ic_play_page_indicator_unselected);
            addView(img);
        }

    }

    public void setCurrentSelected(int position) {
        int count = getChildCount();

        if (position >= count)
            throw new ArrayIndexOutOfBoundsException("给定的Position大于ChildCount的角标");

        for (int i = 0; i < count; i++) {
            ImageView img = (ImageView) getChildAt(i);
            if (position == i)
                img.setImageResource(R.drawable.ic_play_page_indicator_selected);
            else
                img.setImageResource(R.drawable.ic_play_page_indicator_unselected);
        }


    }

}
