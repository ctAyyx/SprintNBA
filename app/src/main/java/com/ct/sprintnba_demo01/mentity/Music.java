package com.ct.sprintnba_demo01.mentity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by ct on 2017/2/15.
 * ===========================
 * 音乐播放时的实体类
 * <p>
 * ===========================
 */

public class Music implements Serializable{

    // 歌曲类型 本地/网络
    public Type type;
    // [本地歌曲]歌曲id
    public long id;
    // 音乐标题
    public String title;
    // 艺术家
    public String artist;
    // 专辑
    public String album;
    // 持续时间
    public long duration;
    // 音乐路径
    public String uri;
    // [本地歌曲]专辑封面路径
    public String coverUri;
    // 文件名
    public String fileName;
    // [网络歌曲]专辑封面bitmap
    public Bitmap cover;
    // 文件大小
    public long fileSize;
    // 发行日期
    public String year;

    @Override
    public String toString() {
        return "Music: id=" + id +
                "\r\n title=" + title +
                "\r\n artist=" + artist +
                "\r\n album=" + album +
                "\r\n duration=" + duration +
                "\r\n uri=" + uri +
                "\r\n coverUri=" + coverUri +
                "\r\n fileName=" + fileName +
                "\r\n cover=" + cover +
                "\r\n year=" + year + "\r\n";
    }

    public enum Type {
        ONLINE, LOCAL;
    }
}
