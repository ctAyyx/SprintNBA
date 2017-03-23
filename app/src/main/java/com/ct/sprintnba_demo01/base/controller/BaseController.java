package com.ct.sprintnba_demo01.base.controller;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.net.NetHelper;
import com.ct.sprintnba_demo01.base.response.BaseResponse;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;

import rx.Subscription;
import rx.functions.Action1;

/**
 * 数据控制基类
 * <p>
 * Created by Administrator on 16-8-23.
 * =================================================
 * 创建网络辅助类NetHelper
 * <p>
 * 创建RxBus
 * =================================================
 * =================================================
 */
public class BaseController<T> {


    protected IView iView;
    protected Context context;
    private Subscription rxSubscription;

    public NetHelper<T> helper;

    public static final String CREATE_BUNDLE = "CREATE_BUNDLE";
    private boolean isRunning;  //生命标记

    public BaseController(final Context context, IView iView) {
        this.context = context;
        this.iView = iView;
        helper = new NetHelper(context, iView);
        this.rxSubscription =
                RxBus.getInstance().toObserverable(RxDataEvent.class)
                        .subscribe(new Action1<RxDataEvent>() {
                                       @Override
                                       public void call(RxDataEvent event) {
                                           if (event != null)
                                               onDataReceive(event.type, (T) event.data);
                                       }
                                   }

                                ,
                                new Action1<Throwable>()

                                {
                                    @Override
                                    public void call(Throwable throwable) {


                                    }
                                }

                        );
    }


    /**
     * 当接收到数据
     *
     * @param resId    标志
     * @param response 数据
     */
    public void onDataReceive(int resId, final T response) {
        iView.onUpdateView(resId, response);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onCreate(Bundle bundle) {
        isRunning = true;
    }

    public void initView() {
        // iView.initView();
    }

    public void onDestroy() {
        this.rxSubscription.unsubscribe();
    }

    public void onNewIntent(Intent intent) {
    }

    public void onPause() {
        isRunning = false;
    }

    public void onResume() {
        isRunning = true;
    }


}
