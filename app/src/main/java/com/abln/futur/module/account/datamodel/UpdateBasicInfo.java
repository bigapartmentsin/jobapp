package com.abln.futur.module.account.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateBasicInfo implements Serializable {

    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("gender")
    @Expose
    private String gender;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}
