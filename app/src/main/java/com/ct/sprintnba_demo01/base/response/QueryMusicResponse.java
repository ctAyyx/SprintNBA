package com.ct.sprintnba_demo01.base.response;

import com.ct.sprintnba_demo01.mentity.QuerySongEntity;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ct on 2017/2/24.
 */

public class QueryMusicResponse implements Serializable {

    @SerializedName("error_code")
    public String code;
    @SerializedName("order")
    public String order;
    @SerializedName("song")
    public List<QuerySongEntity> data;
}
