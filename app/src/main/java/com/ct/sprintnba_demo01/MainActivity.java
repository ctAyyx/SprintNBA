package com.ct.sprintnba_demo01;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.utils.Preference;
import com.ct.sprintnba_demo01.base.utils.SystemUtils;
import com.ct.sprintnba_demo01.mfragment.NewsFragment;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.mutils.FileUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动界面
 * ==================================
 * 铺满全屏
 * <p>
 * 随机选择启动图片
 * ==================================
 */
public class MainActivity extends BaseActivity {

    int[] imgs = new int[]{
            R.mipmap.irving,
            R.mipmap.bryant,
            R.mipmap.james,
            R.mipmap.harden,
            R.mipmap.curry};
    @BindView(R.id.img_launch)
    ImageView imgLaunch;
    @BindView(R.id.tv_launch_skip)
    TextView tvLaunchSkip;

    private CountDownTimer mTimer;


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        Preference.init(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getToolBar().setVisibility(View.GONE);
        int i = (int) (Math.random() * imgs.length);
        imgLaunch.setImageResource(imgs[i]);

        mTimer = new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvLaunchSkip.setText(String.format(getString(R.string.skip), millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (mTimer != null)
                    mTimer = null;
                tvLaunchSkip.setVisibility(View.GONE);

                startActivityAndStopSelf(NewsActivity.class, null, null);
            }
        };


        //判断音乐服务是否运行
        if (SystemUtils.isServiceRunning(MainActivity.this, MusicPlayService.class))
            startActivityAndStopSelf(MusicPlayActivity.class, null, null);
        else
            mTimer.start();

    }

    @OnClick(R.id.tv_launch_skip)
    public void onClick() {
        if (mTimer != null)
            mTimer.cancel();
        mTimer = null;
        startActivityAndStopSelf(NewsActivity.class, null, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null)
            mTimer.cancel();
        mTimer = null;
    }


}
