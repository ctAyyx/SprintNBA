package com.ct.sprintnba_demo01.base.net;

/**
 * Created by ct on 2017/1/18.
 * ======================================
 * 网络常量类
 * ======================================
 * 网络返回值
 * <p>
 * 网络数据地址
 */

public class NetConstant {
    public static final int NO_NET_NO_CACHE = -100;//没网络没缓存
    public static final int NET_ERROR = -200; //网络创建异常
    public static final int RES_NULL = -300;//返回数据为空

    public static long ConnectionTime = 5000;
    /****************************************
     * NBA
     *****************************************/
    public static final int RESCODE_NBA_SUCCESS = 0;

    public static final String HOST_NBA = "http://sportsnba.qq.com";
    public static final String HOST_NBA_VIDEO = "http://h5vv.video.qq.com";
    public static final String HOST_NBA_PLAYER = "http://sports.qq.com/kbsweb/kbsshare/player.htm?ref=nbaapp&pid=";
    /******************************************************************************/


    /**************************************
     * MM
     ****************************************/
    public static final int RESCODE_MM_SUCCESS = 0;
    public static final String HOST_MM = "http://img.m.duba.com";
    /******************************************************************************/


    /***************************************
     * MUSIC
     ***************************************/
    public static final int RESCODE_MUSIC_SUCCESS = 22000;
    public static final String HOST_MUSIC = "http://tingapi.ting.baidu.com";
    /******************************************************************************/
}
