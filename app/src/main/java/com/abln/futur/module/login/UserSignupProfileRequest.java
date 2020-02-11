package com.abln.futur.module.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserSignupProfileRequest implements Serializable {

    @SerializedName("apikey")
    @Expose
    private String apiKey;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("name")
    @Expose
    private String name;
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
        return apiKey;
    }

    public void setApikey(String apikey) {
        this.apiKey = apikey;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
