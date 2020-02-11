package com.abln.futur.module.account.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateProfessionInfo implements Serializable {
    @SerializedName("apikey")
    @Expose
    private String apikey;
    @SerializedName("technology")
    @Expose
    private String technology;
    @SerializedName("occupation")
    @Expose
    private String occupation;
    @SerializedName("experience")
    @Expose
    private String experience;
    @SerializedName("university")
    @Expose
    private String university;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
