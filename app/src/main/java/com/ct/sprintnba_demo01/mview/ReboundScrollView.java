package com.ct.sprintnba_demo01.mview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.ct.sprintnba_demo01.R;

/**
 * Created by ct on 2017/3/6.
 * <p>
 * ======================================
 * 弹性ScrollView
 * ======================================
 */

public class ReboundScrollView extends NestedScrollView {
    //阻尼值
    private static final float MOVE_FACTOR = 0.5f;

    //松开手指，界面回到正常位置的动画时间
    private static final int ANIM_TIME = 300;

    //ScrollView的子View
    private View contentView;

    //记录按下时的位置
    private float startY;

    //用于记录正常界面的位置
    private Rect originalRect = new Rect();

    //是否可以继续下拉
    private boolean canPullDown = false;

    //是否可以继续上拉
    private boolean canPullUp = false;

    //标记是否移动了布局
    private boolean isMove = false;

    //标记用户是否希望可以上拉,默认为true
    private boolean canUp = true;
    private boolean canDown = true;


    public ReboundScrollView(Context context) {
        this(context, null);
    }

    public ReboundScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReboundScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ReboundScrollView, defStyleAttr, 0);
        canDown = array.getBoolean(R.styleable.ReboundScrollView_canDown, true);
        canUp = array.getBoolean(R.styleable.ReboundScrollView_canUp, true);
        array.recycle();

    }


    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0)
            contentView = getChildAt(0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (contentView == null)
            return;
        // ScrollView中的唯一子控件的位置信息, 这个位置信息在整个控件的生命周期中保持不变
        originalRect.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (contentView == null)
            return super.dispatchTouchEvent(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下的Y值
                startY = ev.getY();

                //判断是否可以上拉或下拉
                canPullUp = isCanPullUp();
                canPullDown = isCanPullDown();

                break;

            case MotionEvent.ACTION_UP:
                //判断是否移动了布局
                if (!isMove)
                    break;

                //开启动画
                TranslateAnimation animation = new TranslateAnimation(0, 0, contentView.getTop(), originalRect.top);
                animation.setDuration(ANIM_TIME);
                contentView.startAnimation(animation);

                //设置回到正常的位置
                contentView.layout(originalRect.left, originalRect.top, originalRect.right, originalRect.bottom);
                canPullDown = false;
                canPullUp = false;
                isMove = false;

                break;
            case MotionEvent.ACTION_MOVE:
                if (!canDown && !canUp)//不希望上拉和下拉
                    break;

                //在滚动过程中 ，既没有滚动到可以上拉的程度，也没有滚动到可以下拉的程度
                if (!canPullUp && !canPullDown) {
                    canPullUp = isCanPullUp();
                    canPullDown = isCanPullDown();
                    startY = ev.getY();
                }

                //计算手指移动的距离
                float nowY = ev.getY();
                int detaY = (int) (nowY - startY);

                //是否移动布局
                boolean showMove = (canPullDown && detaY > 0)//不可以下拉，且手指向下移动
                        || (canPullUp && detaY < 0)//不可以上拉，且手指向上
                        || (canPullUp && canPullDown);//这种情况出现在ScrollView包裹的控件比ScrollView还小

                if (showMove) {
                    //计算偏移量
                    int offset = (int) (detaY * MOVE_FACTOR);
                    if ((offset > 0 && canDown) || (offset < 0 && canUp))
                        contentView.layout(originalRect.left, originalRect.top + offset, originalRect.right, originalRect.bottom + offset);
                    isMove = true;
                }

                break;

        }
        return super.dispatchTouchEvent(ev);

    }

    /**
     * 判断是否可以继续下拉
     *
     * @return true 不可以继续下拉
     * false 可以继续下拉
     */
    private boolean isCanPullDown() {
        return getScrollY() == 0 || contentView.getHeight() < getHeight() + getScrollY();
    }

    /**
     * 判断是否可以继续上拉
     *
     * @return true 不可以继续上拉
     * false 可以继续上拉
     */
    private boolean isCanPullUp() {
        return contentView.getHeight() <= getHeight() + getScrollY();
    }


    public void setPullUp(boolean canPullUp) {
        this.canUp = canPullUp;
    }

    public void setPullDown(boolean canPullDown) {
        this.canDown = canPullDown;
    }

}
