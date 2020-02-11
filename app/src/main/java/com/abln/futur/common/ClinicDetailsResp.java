package com.abln.futur.common;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClinicDetailsResp {

    @SerializedName("lng")
    private String lng;

    @SerializedName("files")
    private List<ClinicFilesItem> clinicFiles;

    @SerializedName("updated_at")
    private Object updatedAt;


    @SerializedName("fee")
    private String fee;

    @SerializedName("name")
    private String name;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("clinic_id")
    private String clinicId;

    @SerializedName("lat")
    private String lat;

    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public List<ClinicFilesItem> getClinicFiles() {
        return clinicFiles;
    }

    public void setClinicFiles(List<ClinicFilesItem> clinicFiles) {
        this.clinicFiles = clinicFiles;
    }

    public Object getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getClinicId() {
        return clinicId;
    }

    public void setClinicId(String clinicId) {
        this.clinicId = clinicId;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}