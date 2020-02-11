package com.abln.futur.module.account.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetSignUpOTPRequest implements Serializable {

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("mobile_code")
    @Expose
    private String mobile_code;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile_code() {
        return mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }

}