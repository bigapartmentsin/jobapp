package com.abln.futur.module.job.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyPostedJobListRequest implements Serializable {

    public String postid;
    public String date;
    public String akey;
    @SerializedName("apikey")
    @Expose
    private String apikey;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
