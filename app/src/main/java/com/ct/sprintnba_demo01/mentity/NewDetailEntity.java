package com.ct.sprintnba_demo01.mentity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ct on 2017/1/18.
 */

public class NewDetailEntity {

 /*  abstractX =转眼间赛季已经过半，虽然现在对许多奖项进行预测还为时过早，但...
     atype=0
    commentId=1725373409
    commentsNum=1
    content
   imgurl = http://inews.gtimg.com/newsapp_ls/0/1047141759_640330/0
    imgurl1=http://inews.gtimg.com/newsapp_ls/0/1047141759_640470/0
    imgurl2=http://inews.gtimg.com/newsapp_ls/0/1047141759_150120/0
    isCollect=-1
    newsAppId=
    pub_time=2017-01-18 02:48:23
    pub_time_new=01-18 02:48
    source=
    NBA官网
            title = 外媒评选半程奖项
    ：
    MVP威少哈登平分秋色
            upNum = 77
    url=http://nbachina.qq.com/a/20170118/001248.htm*/

    @SerializedName("abstract")
    public String abstractX;
    public int atype;
    public String commentId;
    public int commentsNum;
    public List<Describe> content;
    public String imgurl;
    public String imgurl1;
    public String imgurl2;
    public int isCollect;
    public String newsAppId;
    public String pub_time;
    public String pub_time_new;
    public String source;
    public String title;
    public String upNum;
    public String url;


    public static class Describe {
        public String info;
        public String type;
    }
}
