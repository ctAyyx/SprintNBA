package com.ct.sprintnba_demo01.mview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.base.utils.ImageUtils;
import com.ct.sprintnba_demo01.mutils.CoverLoader;

/**
 * Created by ct on 2017/2/17.
 * ============================
 * 专辑封面View
 * ============================
 */

public class AlbumCoverView extends View implements ValueAnimator.AnimatorUpdateListener {
    private static final long TIME_UPDATE = 50L;
    private static final float DISC_ROTATION_INCERASE = 0.5f;
    private static final float NEEDLE_ROTATION_PLAY = 0.0f;   //开始播放旋转角度
    private static final float NEEDLE_ROTATION_PAUSE = -25.0f;//暂停播放旋转角度

    private Handler mHandler = new Handler();
    private Bitmap mDiscBitmap;
    private Bitmap mCoverBitmap;
    private Bitmap mNeedleBitmap;
    private Drawable mTopLine;
    private Drawable mCoverBorder;
    private int mTopLineHeight;
    private int mCoverBorderWidth;
    private Matrix mDiscMatrix = new Matrix();
    private Matrix mCoverMatrix = new Matrix();
    private Matrix mNeedleMatrix = new Matrix();
    private ValueAnimator mPlayAnimator;
    private ValueAnimator mPauseAnimator;
    private float mDiscRotation = 0.0f;
    private float mNeedleRotation = NEEDLE_ROTATION_PLAY;
    private boolean isPlaying = false;

    //图片起始坐标
    private Point mDiscPoint = new Point();
    private Point mCoverPoint = new Point();
    private Point mNeedlePoint = new Point();

    //旋转中心坐标
    private Point mDiscCenterPoint = new Point();
    private Point mCoverCenterPoint = new Point();
    private Point mNeedleCenterPoint = new Point();


    public AlbumCoverView(Context context) {
        this(context, null);
    }

    public AlbumCoverView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumCoverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTopLine = getResources().getDrawable(R.drawable.album_cover_top_line_shape, null);
            mCoverBorder = getResources().getDrawable(R.drawable.albun_cover_border_shape, null);
        } else {
            mTopLine = getResources().getDrawable(R.drawable.album_cover_top_line_shape);
            mCoverBorder = getResources().getDrawable(R.drawable.albun_cover_border_shape);

        }
        mDiscBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_disc);
        mDiscBitmap = ImageUtils.resizeImage(mDiscBitmap, (int) (getScreenWidth() * 0.75), (int) (getScreenWidth() * 0.75));

        mCoverBitmap = CoverLoader.getInstance(getContext()).loadRound(null);


        mNeedleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play_page_needle);
        mNeedleBitmap = ImageUtils.resizeImage(mNeedleBitmap, (int) (getScreenWidth() * 0.25), (int) (getScreenWidth() * 0.375));

        mTopLineHeight = DeviceUtils.dp2px(getContext(), 1);
        mCoverBorderWidth = DeviceUtils.dp2px(getContext(), 1);

        mPlayAnimator = ValueAnimator.ofFloat(NEEDLE_ROTATION_PAUSE, NEEDLE_ROTATION_PLAY);
        mPlayAnimator.setDuration(300);
        mPlayAnimator.addUpdateListener(this);

        mPauseAnimator = ValueAnimator.ofFloat(NEEDLE_ROTATION_PLAY, NEEDLE_ROTATION_PAUSE);
        mPauseAnimator.setDuration(300);
        mPauseAnimator.addUpdateListener(this);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize();
    }

    /**
     * 确定图片起始位置与旋转中心坐标
     */
    private void initSize() {
        int disOffsetY = mNeedleBitmap.getHeight() / 2;
        //唱片起始位置
        mDiscPoint.x = (getWidth() - mDiscBitmap.getWidth()) / 2;
        mDiscPoint.y = disOffsetY;
        //专辑起始位置
        mCoverPoint.x = (getWidth() - mCoverBitmap.getWidth()) / 2;
        mCoverPoint.y = disOffsetY + (mDiscBitmap.getHeight() - mCoverBitmap.getHeight()) / 2;
        //指针起始位置
        mNeedlePoint.x = getWidth() / 2 - mNeedleBitmap.getWidth() / 6;
        mNeedlePoint.y = -mNeedleBitmap.getWidth() / 6;

        mDiscCenterPoint.x = getWidth() / 2;
        mDiscCenterPoint.y = mDiscBitmap.getHeight() / 2 + disOffsetY;

        mCoverCenterPoint.x = mDiscCenterPoint.x;
        mCoverCenterPoint.y = mDiscCenterPoint.y;

        mNeedleCenterPoint.x = mDiscCenterPoint.x;
        mNeedleCenterPoint.y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //1.绘制顶部虚线
        mTopLine.setBounds(0, 0, getWidth(), mTopLineHeight);
        mTopLine.draw(canvas);
        //2.绘制黑胶片外侧半透明边框
        mCoverBorder.setBounds(mDiscPoint.x - mCoverBorderWidth, mDiscPoint.y - mCoverBorderWidth,
                mDiscPoint.x + mDiscBitmap.getWidth() + mCoverBorderWidth,
                mDiscPoint.y + mDiscBitmap.getHeight() + mCoverBorderWidth
        );
        //3.绘制黑胶，设置旋转中心和旋转角度,setRotate和preTranslate顺序很重要
        //Matrix调用一系列set,pre,post方法时,可视为将这些方法插入到一个队列.当然,按照队列中从头至尾的顺序调用执行.
        //其中pre表示在队头插入一个方法, post表示在队尾插入一个方法.而set表示把当前队列清空, 并且总是位于队列的最中间位置.当执行了一次set后:
        //pre方法总是插入到set前部的队列的最前面, post方法总是插入到set后部的队列的最后面
        mDiscMatrix.setRotate(mDiscRotation, mDiscCenterPoint.x, mDiscCenterPoint.y);
        //设置图片起始坐标
        mDiscMatrix.preTranslate(mDiscPoint.x, mDiscPoint.y);
        canvas.drawBitmap(mDiscBitmap, mDiscMatrix, null);

        //4 绘制封面
        mCoverMatrix.setRotate(mDiscRotation, mCoverCenterPoint.x, mCoverCenterPoint.y);
        mCoverMatrix.preTranslate(mCoverPoint.x, mCoverPoint.y);
        canvas.drawBitmap(mCoverBitmap, mCoverMatrix, null);
        //5 绘制指针
        mNeedleMatrix.setRotate(mNeedleRotation, mNeedleCenterPoint.x, mNeedleCenterPoint.y);
        mNeedleMatrix.preTranslate(mNeedlePoint.x, mNeedlePoint.y);
        canvas.drawBitmap(mNeedleBitmap, mNeedleMatrix, null);

    }

    public void initNeedle(boolean isPlaying) {
        mNeedleRotation = isPlaying ? NEEDLE_ROTATION_PLAY : NEEDLE_ROTATION_PAUSE;
        invalidate();
    }

    public void setCoverBitmap(Bitmap bitmap) {
        if (bitmap == null)
            return;

        mCoverBitmap = //bitmap;
                ImageUtils.createCircleImage(ImageUtils.resizeImage(bitmap, DeviceUtils.deviceWidth(getContext()) / 2, DeviceUtils.deviceWidth(getContext()) / 2));
        mDiscRotation = 0.0f;
        invalidate();
    }

    public void start() {
        if (isPlaying)
            return;

        isPlaying = true;
        mHandler.post(mRotationRunnable);
        mPlayAnimator.start();

    }

    public void pause() {
        if (!isPlaying)
            return;
        isPlaying = false;
        mHandler.removeCallbacks(mRotationRunnable);
        mPauseAnimator.start();
    }


    private int getScreenWidth() {
        return DeviceUtils.deviceWidth(getContext());
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mNeedleRotation = (float) animation.getAnimatedValue();
        invalidate();
    }

    private Runnable mRotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                mDiscRotation += DISC_ROTATION_INCERASE;
                if (mDiscRotation >= 360)
                    mDiscRotation = 0;
                invalidate();
            }

            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };
}
