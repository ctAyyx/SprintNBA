package com.ct.sprintnba_demo01.mcontroller;

import android.content.Context;
import android.util.Log;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;

/**
 * Created by Administrator on 2017/1/12.
 */

public class NewsController extends BaseController<BaseNBAResponse> {
    public NewsController(Context context, IView iView) {
        super(context, iView);
    }

    /**
     * 获取不同专栏的数据indexs
     */
    public void getNewsIndexs(int reqId, String column) {
        ServiceApi serviceApi;

        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_NBA)) == null)
            return;
        helper.setObservable(serviceApi.getNewIndex(column), reqId);
    }

    /**
     * 获取Item
     */

    public void getNewsList(int reqId, String column, String articleIds) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_NBA)) == null)
            return;
        helper.setObservable(serviceApi.getNewsItem(column, articleIds), reqId);
    }

    /**
     * 获取详情
     */
    public void getDetail(int reqId, String column, String articleId) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_NBA)) == null)
            return;
        helper.setObservable(serviceApi.getDetail(column, articleId), reqId);
    }

    /**
     * 获取球员信息
     */
    public void getNBAPlayer(int reqId) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_NBA)) == null)
            return;
        helper.setObservable(serviceApi.getNBAPlayer(), reqId);
    }

}
