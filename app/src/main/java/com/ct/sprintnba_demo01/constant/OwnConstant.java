package com.ct.sprintnba_demo01.constant;

/**
 * Created by Administrator on 2017/1/18.
 */

public class OwnConstant {

    public static final int DEFAULTLOAD = 10;

    public static final String COLUMN_NBA = "Column_NBA";
    public static final String COLUMN_MM = "Column_MM";
    public static final String COLUMN_MUSIC = "COLUMN_MUSIC";

    public static final String WEB_TITLE = "web_title";
    public static final String WEB_URL = "web_url";
    public static final String WEB_SHOW_BOTTOM_BAR = "show_bottom_bar";

    /***************************************
     * Activity 参数
     **************************************/
    public static final String NIGHT = "night";
    public static final String DAY = "day";
    public static final String ISNIGHTTHEME = "nightthme";


    /***************************************
     * Music
     **************************************/
    public static final String MUSIC_TYPE = "type_music";
    public static final String MUSIC_NAME = "name_music";
    public static final String FILENAME_MP3 = ".mp3";
    public static final String FILENAME_LRC = ".lrc";
    public static final String MUSIC_ARTIST_ID = "MUSIC_ARTIST_ID";
    public static final String MUSIC_ARTIST_NAME = "MUSIC_ARTIST_NAME";
    public static final int MUSIC_LOCAL_NEXT = -2;

    /*****************************************
     * Music play mode
     ****************************************/

    public static final int MUSIC_MODE_LOOP = 0;  //循环
    public static final int MUSIC_MODE_SHUFFLE = 1;//随机
    public static final int MUSIC_MODE_ONE = 2;    //单曲

    /*****************************************
     * ACTION
     ******************************************/
    public static final String ACTION_MEDIA_PLAY_PAUSE = "com.ct.sprintnba_demo01.ACTION_MEDIA_PLAY_PAUSE";
    public static final String ACTION_MEDIA_NEXT = "com.ct.sprintnba_demo01.ACTION_MEDIA_NEXT";
    public static final String ACTION_MEDIA_PREVIOUS = "com.ct.sprintnba_demo01.ACTION_MEDIA_PREVIOUS";
    public static final String VOLUME_CHANGED_ACTION = "com.ct.sprintnba_demo01.VOLUME_CHANGED_ACTION";

    /***************************************
     * EXTRAS
     *************************************/
    public static final String MUSIC_LIST_TYPE = "music_list_type";
    public static final String TING_UID = "ting_uid";
    public static final String FROM_NOTIFICATION = "from_notification";
    public static final String DOWNLOAD_UPDATE = "download_update";

    /*************************************
     * ECache NAME
     **************************************/
    public static final String CACHE_MUSIC_BASE = "Music";                   //音乐缓存目录
    public static final String CACHE_MUSIC_PLAY_MODE = "musicPlay_mode";
    public static final String CACHE_MUSIC_PLAYING = "music_palying";
    public static final String CACHE_MUSIC_LIST = "music_list";
    public static final String CACHE_MUSIC_STATE = "music_state";

    public static final String CACHE_ACTIVITY_BASE = "Activity";     //全局参数缓存目录
    public static final String CACHE_ACTIVITY_NIGHT = "activity_night";

}
