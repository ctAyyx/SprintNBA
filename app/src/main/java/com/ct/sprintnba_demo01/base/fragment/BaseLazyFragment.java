package com.ct.sprintnba_demo01.base.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;

import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by ct on 2017/1/13.
 * =======================================
 * 懒加载模式的Fragment
 * 只有创建并显示的时候才会调用onCreateViewLazy方法
 * <p>
 * <p>
 * 懒加载的原理onCreateView的时候Fragment有可能没有显示出来。<br>
 * 但是调用到setUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true的时候就说明有显示出来<br>
 * 但是要考虑onCreateView和setUserVisibleHint的先后问题所以才有了下面的代码
 * <p>
 * 注意：<br>
 * 《1》原先的Fragment的回调方法名字后面要加个Lazy，比如Fragment的onCreateView方法， 就写成onCreateViewLazy <br>
 * 《2》使用该LazyFragment会导致多一层布局深度
 * =======================================
 */

public class BaseLazyFragment<T> extends BaseFragment<T> {
    private boolean isInit;  //标记是否初始化
    private Bundle savedInstanceState;
    public static final String INTENT_BOOLEAN_LAZYLOAD = "INTENT_BOOLEAN_LAZYLOAD";
    private boolean isLazyLoad = true; //标记是否开启懒加载，默认开启
    private FrameLayout layout; //
    private boolean isStart;  //标记是否启动

    @Override
    protected final void onCreateView(Bundle savedInstanceState) {
        super.onCreateView(savedInstanceState);
        Bundle bundle = getArguments();//获取从Activity传入的数据,通过此方法该设置是否开启懒加载
        if (bundle != null)
            isLazyLoad = bundle.getBoolean(INTENT_BOOLEAN_LAZYLOAD, isLazyLoad);
        if (isLazyLoad) {
            if (getUserVisibleHint() && !isInit) {
                isInit = true;
                this.savedInstanceState = savedInstanceState;
                onCreateViewLazy(savedInstanceState);
            } else {
                layout = new FrameLayout(getApplicationContext());//创建底层布局
                layout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                super.setContentView(layout);

            }
        } else {
            isInit = true;
            onCreateViewLazy(savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInit && getContentView() != null) {
            isInit = true;
            onCreateViewLazy(savedInstanceState);
            onResumeLazy();

        }
        if (isInit && getContentView() != null) {
            if (isVisibleToUser) {
                isStart = true;
                onFragmenStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }

    }

    @Override
    public void setContentView(int layoutId) {
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            View view = inflater.inflate(layoutId, layout, false);
            layout.addView(view);
        } else {
            super.setContentView(layoutId);
        }

        ButterKnife.bind(this, getContentView());
    }

    @Override
    public void setContentView(View view) {
        if (isLazyLoad && getContentView() != null && getContentView().getParent() != null) {
            layout.removeAllViews();
            layout.addView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public final void onStart() {
        super.onStart();
        if (isInit && !isStart && getUserVisibleHint()) {
            isStart = true;
            onFragmenStartLazy();
        }
    }

    @Override
    public final void onResume() {
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    public final void onPause() {
        super.onPause();
        if (isInit)
            onPauseLazy();
    }

    @Override
    public final void onStop() {
        super.onStop();
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        if (isInit)
            onDestroyViewLazy();
        isInit = false;
    }


    protected void onCreateViewLazy(Bundle savedInstanceState) {
    }

    protected void onResumeLazy() {

    }

    protected void onFragmenStartLazy() {
    }

    protected void onFragmentStopLazy() {
    }

    protected void onPauseLazy() {
    }

    protected void onDestroyViewLazy() {
    }
}
