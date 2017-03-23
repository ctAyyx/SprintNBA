package com.ct.sprintnba_demo01.mcontroller;

import android.content.Context;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.base.response.ArtistResponse;

/**
 * Created by ct on 2017/2/20.
 */

public class ArtistController extends BaseController<ArtistResponse> {

    public ArtistController(Context context, IView iView) {
        super(context, iView);
    }

    /**
     * 获取歌手详情
     */
    public void getArtist(int reqId, String uid) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_MUSIC)) == null)
            return;
        helper.setObservable(serviceApi.getArtist(uid), reqId);
    }

}
