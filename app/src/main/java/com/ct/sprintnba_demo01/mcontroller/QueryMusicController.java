package com.ct.sprintnba_demo01.mcontroller;

import android.content.Context;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.base.response.QueryMusicResponse;

/**
 * Created by Administrator on 2017/2/28.
 */

public class QueryMusicController extends BaseController<QueryMusicResponse> {

    public QueryMusicController(Context context, IView iView) {
        super(context, iView);
    }

    /**
     * 通过歌曲名获取歌曲
     */

    public void getQueryMusic(int reqId, String query) {
        ServiceApi serviceApi;
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_MUSIC)) == null)
            return;
        helper.setObservable(serviceApi.getSongList(query), reqId);
    }
}
