package com.ct.sprintnba_demo01.mcontroller;

import android.content.Context;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.base.response.BaseMMResponse;


/**
 * Created by ct on 2017/1/13.
 */

public class MMController extends BaseController<BaseMMResponse> {
    public MMController(Context context, IView iView) {
        super(context, iView);
    }

    public void getMMphoto(int reqId, int start, int page, String category) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_MM2)) == null)
            return;
        helper.setObservable(serviceApi.getMMPhoto2(category, start, page), reqId);
    }
}
