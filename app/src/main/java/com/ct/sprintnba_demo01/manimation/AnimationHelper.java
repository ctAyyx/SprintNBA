package com.ct.sprintnba_demo01.manimation;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ct.sprintnba_demo01.R;

/**
 * Created by ct on 2017/1/18.
 */

public class AnimationHelper {
    private int mLastPosition = -1;
    private Animation animation;


    public void showItemAnim(final View view, final int position, Animation.AnimationListener listener) {
        if (position > mLastPosition) {
            Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.item_bottom_in);
            view.startAnimation(animation);
            if (listener != null)
                animation.setAnimationListener(listener);
            mLastPosition = position;
        }

    }

    public void cancleAnim() {

        animation.cancel();
    }
}
