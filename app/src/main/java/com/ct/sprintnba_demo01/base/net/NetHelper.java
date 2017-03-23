package com.ct.sprintnba_demo01.base.net;

import android.content.Context;
import android.util.Log;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.response.BaseMMResponse;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.BaseResponse;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.base.utils.ECache;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 网络辅助类
 * =========================================
 * Created by ct on 2016/12/27.
 * =========================================
 * <p>
 * <p>
 * =========================================
 */

public class NetHelper<T> {
    private Context context;
    private IView iView;
    private ECache cache;


    public NetHelper(Context context, IView iView) {
        this.context = context;
        this.iView = iView;

    }

    /**
     * 获取网络访问接口
     */
    public ServiceApi getServiceApi(String host) {
        //判断网络是否可用
        if (!DeviceUtils.isNetworkConnected(context)) {
            iView.onUpdateView(NetConstant.NO_NET_NO_CACHE, null);
            return null;
        }
        try {
            return NetService.getApiService(context, host);
        } catch (Exception e) {
            iView.onUpdateView(NetConstant.NET_ERROR, null);
        }
        return null;
    }

    public void
    setObservable(Observable<? extends T> observable, final int reqId) {
        setObservable(observable, "", reqId);
    }

    public void setObservable(Observable<? extends T> observable, final String key, final int reqId) {


        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T response) {
                        if (response == null) {
                            iView.onUpdateView(NetConstant.RES_NULL, null);
                            return;
                        }
                        iView.onUpdateView(reqId, response);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.i("TAG", "NetHelper Throwable:  " + reqId + "--->" + throwable.toString());
                        iView.onUpdateView(NetConstant.NET_ERROR, null);

                    }
                });
    }


    /************************************************************
     * =================================
     * NBA 数据                    =====
     * =================================
     ************************************************************/
//    public ServiceApi getServiceApiNBA() {
//        //判断网络是否可用
//        if (!DeviceUtils.isNetworkConnected(context)) {
//            iView.onUpdateViewNBA(NetConstant.NO_NET_NO_CACHE, null);
//            return null;
//        }
//        try {
//            return NetService.getApiServiceNBA(context);
//        } catch (Exception e) {
//            iView.onUpdateViewNBA(NetConstant.NET_ERROR, null);
//        }
//        return null;
//    }
//
//    public void setObservableNBA(Observable<? extends BaseNBAResponse> observable, final int reqId) {
//        setObservableNBA(observable, "", reqId);
//    }
//
//    public void setObservableNBA(Observable<? extends BaseNBAResponse> observable, final String key, final int reqId) {
//
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseNBAResponse>() {
//                    @Override
//                    public void call(BaseNBAResponse response) {
//                        if (response == null) {
//                            iView.onUpdateViewNBA(NetConstant.RES_NULL, null);
//                            return;
//                        }
//                        switch (response.resCode) {
//                            case NetConstant.RESCODE_NBA_SUCCESS:
//                                if (cache != null) {
//                                    cache.put(key, response);
//                                }
//                                iView.onUpdateViewNBA(reqId, response);
//                                break;
//                        }
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//
//                        iView.onUpdateViewNBA(NetConstant.NET_ERROR, null);
//
//                    }
//                });
//    }

    /************************************************************************************************************/

    /*******************************************************************
     * =================================
     * 美女图片数据                =====
     * =================================
     *******************************************************************/
//    public ServiceApi getServiceApiMM() {
//        //判断网络是否可用
//        if (!DeviceUtils.isNetworkConnected(context)) {
//            iView.onUpdateViewMM(NetConstant.NO_NET_NO_CACHE, null);
//            return null;
//        }
//        try {
//            return NetService.getApiServiceMM(context);
//        } catch (Exception e) {
//            iView.onUpdateViewMM(NetConstant.NET_ERROR, null);
//        }
//        return null;
//    }
//
//    public void setObservableMM(Observable<? extends BaseMMResponse> observable, final int reqId) {
//        setObservableMM(observable, "", reqId);
//    }
//
//    public void setObservableMM(Observable<? extends BaseMMResponse> observable, final String key, final int reqId) {
//
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseMMResponse>() {
//                    @Override
//                    public void call(BaseMMResponse response) {
//                        if (response == null) {
//                            iView.onUpdateViewMM(NetConstant.RES_NULL, null);
//                            return;
//                        }
//                        switch (response.status) {
//                            case NetConstant.RESCODE_MM_SUCCESS:
//                                if (cache != null) {
//                                    cache.put(key, response);
//                                }
//                                iView.onUpdateViewMM(reqId, response);
//                                break;
//                        }
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.i("TAG", "THROW" + throwable.toString());
//                        iView.onUpdateViewMM(NetConstant.NET_ERROR, null);
//                    }
//                });
//    }
/******************************************************************************************************************/

    /*******************************************************************
     * =================================
     * 音乐数据                =====
     * =================================
     *******************************************************************/
//    public ServiceApi getServiceApiMusic() {
//        //判断网络是否可用
//        if (!DeviceUtils.isNetworkConnected(context)) {
//            iView.onUpdateViewMusic(NetConstant.NO_NET_NO_CACHE, null);
//            return null;
//        }
//        try {
//            return NetService.getApiServiceMusic(context);
//        } catch (Exception e) {
//            Log.i("TAG", "网络创建异常" + e.toString());
//            iView.onUpdateViewMusic(NetConstant.NET_ERROR, null);
//        }
//        return null;
//    }
//
//    public void setObservableMusic(Observable<? extends BaseMusicResponse> observable, final int reqId) {
//        setObservableMusic(observable, "", reqId);
//    }
//
//    public void setObservableMusic(Observable<? extends BaseMusicResponse> observable, final String key, final int reqId) {
//
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseMusicResponse>() {
//                    @Override
//                    public void call(BaseMusicResponse response) {
//                        if (response == null) {
//                            iView.onUpdateViewMusic(NetConstant.RES_NULL, null);
//                            return;
//                        }
//                        switch (response.status) {
//                            case NetConstant.RESCODE_MUSIC_SUCCESS:
//                                if (cache != null) {
//                                    cache.put(key, response);
//                                }
//                                iView.onUpdateViewMusic(reqId, response);
//                                break;
//                        }
//
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Log.i("TAG", "THROW" + throwable.toString());
//                        iView.onUpdateViewMusic(NetConstant.NET_ERROR, null);
//                    }
//                });
//    }
/******************************************************************************************************************/
}
