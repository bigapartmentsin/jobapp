package com.abln.futur.module.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VerifyOtpRequest implements Serializable {

    @SerializedName("verifyotp")
    @Expose
    private String verifyotp;

    @SerializedName("apikey")
    @Expose
    private String apikey;

    public String getVerifyotp() {
        return verifyotp;
    }

    public void setVerifyotp(String verifyotp) {
        this.verifyotp = verifyotp;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
