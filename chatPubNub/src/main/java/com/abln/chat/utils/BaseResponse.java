package com.abln.chat.utils;
/**
 * Created by Sandeep padala on 6/29/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {

    @SerializedName("data")
    public T data;
    @SerializedName("statuscode")
    @Expose
    public int statuscode;
    @SerializedName("statusMessage")
    @Expose
    public String statusMessage;
}








