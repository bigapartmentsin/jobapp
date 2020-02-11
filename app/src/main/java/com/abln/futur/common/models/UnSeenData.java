
package com.abln.futur.common.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class UnSeenData   implements Serializable {

    @SerializedName("data")
    public Data mData;
    @SerializedName("statusMessage")
    private String mStatusMessage;
    @SerializedName("statuscode")
    private Long mStatuscode;

    public Data getData() {
        return mData;
    }

    public void setData(Data data) {
        mData = data;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        mStatusMessage = statusMessage;
    }

    public Long getStatuscode() {
        return mStatuscode;
    }

    public void setStatuscode(Long statuscode) {
        mStatuscode = statuscode;
    }

}
