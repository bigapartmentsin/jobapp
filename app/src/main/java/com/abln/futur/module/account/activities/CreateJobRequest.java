package com.abln.futur.module.account.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateJobRequest implements Serializable {

    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("job_title")
    @Expose
    private String jobTitle;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("job_experience")
    @Expose
    private String jobExperience;
    @SerializedName("img1")
    @Expose
    private String img1;
    @SerializedName("img10")
    @Expose
    private String img10;
    @SerializedName("pdf")
    @Expose
    private String pdf;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("expdate")
    @Expose
    private String expdate;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg10() {
        return img10;
    }

    public void setImg10(String img10) {
        this.img10 = img10;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getExpdate() {
        return expdate;
    }

    public void setExpdate(String expdate) {
        this.expdate = expdate;
    }
}