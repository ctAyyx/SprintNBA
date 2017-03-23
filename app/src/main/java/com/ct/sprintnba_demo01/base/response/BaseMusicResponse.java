package com.ct.sprintnba_demo01.base.response;

import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.MusicBillboardEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mentity.SongInfo;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ct on 2017/2/13.
 */

public class BaseMusicResponse<T> implements Serializable {
    @SerializedName("billboard")
    public MusicBillboardEntity billboard;
    @SerializedName("error_code")
    public int status;
    @SerializedName("song_list")
    public T data;

    @SerializedName("songinfo")
    public SongInfo songInfo;

    @SerializedName("bitrate")
    public Bitrate bitrate;
}
