package com.abln.futur.module.account.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetLoginResponse implements Serializable {

    @SerializedName("statuscode")
    @Expose
    private Integer statuscode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("apikey")
        @Expose
        private String apikey;
        @SerializedName("mobile")
        @Expose
        private String mobile;

        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

    }
}


