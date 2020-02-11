package com.abln.futur.module.chats.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetStoriesRequest implements Serializable {

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("location_lat")
    @Expose
    private String lat;


    @SerializedName("location_lan")
    @Expose
    private String lan;

    public String getUserID() {
        return userId;
    }

    public void setUserID(String docId) {
        this.userId = docId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }


    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }
}
