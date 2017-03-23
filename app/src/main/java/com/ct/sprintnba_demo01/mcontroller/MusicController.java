package com.ct.sprintnba_demo01.mcontroller;

import android.content.Context;

import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.controller.BaseController;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.constant.OwnConstant;

/**
 * Created by Administrator on 2017/2/13.
 */

public class MusicController extends BaseController<BaseMusicResponse> {
    public MusicController(Context context, IView iView) {
        super(context, iView);
    }

    /**
     * 获取每个分类的前3歌曲
     */
    //http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=2&size=3
    public void getTop3Music(int reqId, String type, int size) {
        ServiceApi serviceApi;
     /*   if ((serviceApiMusic = helper.getServiceApiMusic()) == null)
            return;
        helper.setObservableMusic(serviceApiMusic.getTop3Music(type, size), reqId);*/
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_MUSIC)) == null)
            return;
        helper.setObservable(serviceApi.getTop3Music(type, size), reqId);
    }


    /**
     * 获取指定榜单的歌曲
     */
    public void getMusicList(int reqId, String type, int size, int offset) {
        ServiceApi serviceApi;
        /*if ((serviceApiMusic = helper.getServiceApiMusic()) == null)
            return;
        helper.setObservableMusic(serviceApiMusic.getMusicList(type, size, offset), reqId);*/
        if ((serviceApi = helper.getServiceApi(NetConstant.HOST_MUSIC)) == null)
            return;
        helper.setObservable(serviceApi.getMusicList(type, size, offset), reqId);
    }


}
