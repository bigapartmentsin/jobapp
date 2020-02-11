package com.abln.futur.module.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetLoginOtpResponse implements Serializable {

    @SerializedName("statuscode")
    @Expose
    private Integer statuscode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private Integer data;

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}
