package com.ct.sprintnba_demo01.base.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ct on 2017/3/1.
 */

public class MusicLrcResponse implements Serializable {

    @SerializedName("lrcContent")
    public String lrcContent;
}
