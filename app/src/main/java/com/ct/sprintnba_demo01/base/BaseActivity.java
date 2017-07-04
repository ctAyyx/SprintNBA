package com.ct.sprintnba_demo01.base;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.manager.BaseAppManager;
import com.ct.sprintnba_demo01.base.response.BaseMMResponse;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.BaseResponse;
import com.ct.sprintnba_demo01.base.utils.Preference;
import com.ct.sprintnba_demo01.base.utils.StatusBarCompat;
import com.ct.sprintnba_demo01.constant.OwnConstant;

import java.util.HashMap;

import butterknife.ButterKnife;


/**
 * Created by ct on 2016/12/26.
 * =========================================
 * Activity基类
 * =========================================
 * <p>
 * 实现数据请求
 * <p>
 * 实现动态数据控制
 * ==========================================
 * 框架
 * ptr-load-more
 * Rxjava+RxBus架构
 * Retrofit+GSon
 * ==========================================
 * 说明：
 * 当继承该类后，要访问网络数据
 * 则需规定controller的泛型
 * 返回数据的泛型
 * <p>
 * ==========================================
 */

public abstract class BaseActivity<T extends BaseController, RT> extends AppCompatActivity implements IView<RT> {

    private Toolbar mToolbar;
    protected T controller;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if (savedInstanceState != null) {
            // savedInstanceState.putBundle("android:viewHierarchyState", null);
            savedInstanceState.putString("android:support:fragments", "");
        }

        super.onCreate(savedInstanceState);
       // StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.colorPrimary));
     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
        BaseAppManager.getInstance().addActivity(this);
        Preference.init(this);

        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        } else {
            throw new RuntimeException("You should set LayoutId !");
        }

        if (controller == null)
            controller = (T) new BaseController(this, this);
        controller.onCreate(getIntent().getBundleExtra(BaseController.CREATE_BUNDLE));//获取初始化时传递的数据


        initViewsAndEvents(savedInstanceState);


    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.base_tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public void initToolBar(Toolbar toolbar) {
        mToolbar = toolbar;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public abstract int getLayoutId();

    public abstract void initViewsAndEvents(Bundle savedInstanceState);

    @Override
    protected void onResume() {
        super.onResume();
        controller.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
        BaseAppManager.getInstance().removeActivity(this);
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }


    public void startActivityNow(Class<?> goal, Bundle bundle, String name) {
        Intent intent = new Intent(this, goal);
        if (bundle != null && !TextUtils.isEmpty(name))
            intent.putExtra(name, bundle);
        startActivity(intent);
    }

    public void startActivityAndStopSelf(Class<?> goal, Bundle bundle, String name) {
        startActivityNow(goal, bundle, name);
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            ActivityCompat.finishAfterTransition(BaseActivity.this);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateView(int reqId, RT response) {

    }


    /******************************
     * 在此实现白天/夜间模式的切换
     * <p>
     * 以便所有子类Activity都能
     *****************************/
    /**
     * 修改当前主题为夜间模式
     */
    protected void changThemeToNight() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        Preference.putBoolean(OwnConstant.ISNIGHTTHEME, true);
        recreate();
    }

    /**
     * 修改当前主题为白天模式
     */
    protected void changtThemeToDay() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        Preference.putBoolean(OwnConstant.ISNIGHTTHEME, false);
        recreate();
    }

    protected void changeTheme() {
        if (Preference.getBoolean(OwnConstant.ISNIGHTTHEME)) {
            //表示当前为夜间模式 ，改变主题为白天模式
            changtThemeToDay();
        } else {
            changThemeToNight();
        }
    }

}
