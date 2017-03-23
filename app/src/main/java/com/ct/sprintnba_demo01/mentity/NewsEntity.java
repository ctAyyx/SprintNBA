package com.ct.sprintnba_demo01.mentity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */

public class NewsEntity {
    public String index;
    public String atype; // 0：新闻  1:多图模式  2:视频 h5:banner
    public String title;
    @SerializedName("abstract")
    public String abstractX;
    public String imgurl;
    public String imgurl2;
    public String newsId;
    public String url;
    public String commentId;
    public String pub_time;
    public String column;
    public String vid;
    public String duration;
    public String img_count;
    public String footer;
    public List<String> images_3;

    public String realUrl; // 存放腾讯视频真实地址

}
