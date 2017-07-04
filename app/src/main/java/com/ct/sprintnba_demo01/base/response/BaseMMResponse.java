package com.ct.sprintnba_demo01.base.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ct on 2017/1/18.
 * =================================
 * 美女图片 响应类
 * =================================
 */

public class BaseMMResponse<T> implements Serializable {

    public int status;
    public String msg;
    @SerializedName("items")
    public T data;
}
