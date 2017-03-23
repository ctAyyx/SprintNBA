package com.ct.sprintnba_demo01.base.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/18.
 */

public class BaseNBAResponse<T> implements Serializable {
    @SerializedName("code")
    public int resCode;
    @SerializedName("version")
    public String msg;
    @SerializedName("data")
    public T data;
}
