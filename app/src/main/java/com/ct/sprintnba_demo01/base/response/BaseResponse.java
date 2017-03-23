package com.ct.sprintnba_demo01.base.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ct on 2016/12/27.
 * =====================================
 * 数据请求返回基类
 * =====================================
 */

public class BaseResponse<T> implements Serializable {

    @SerializedName("code")
    public int resCode;
    @SerializedName("version")
    public String msg;
    @SerializedName("data")
    public T data;
}
