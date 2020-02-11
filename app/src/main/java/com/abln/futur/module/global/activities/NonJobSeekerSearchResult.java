package com.abln.futur.module.global.activities;

import com.abln.futur.module.chats.adapter.GetAll_UserResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NonJobSeekerSearchResult implements Serializable {
    @SerializedName("statuscode")
    @Expose
    private Integer statuscode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private GetAll_UserResponse.Data data;

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

    public GetAll_UserResponse.Data getData() {
        return data;
    }

    public void setData(GetAll_UserResponse.Data data) {
        this.data = data;
    }

    public static class PatientList implements Serializable {

        @SerializedName("patient_id")
        @Expose
        private String patientId;

        @SerializedName("photo")
        @Expose
        private String photo;

        @SerializedName("resume")
        @Expose
        private String resume;

        @SerializedName("name")
        @Expose
        private String name;


        @SerializedName("designation")
        @Expose
        private String designation;


        @SerializedName("distance")
        @Expose
        private String distance;


        @SerializedName("experience")
        @Expose
        private String experience;

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getExperience() {
            return experience;
        }

        public void setExperience(String experience) {
            this.experience = experience;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }


        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }


    }

    public class Data {

        @SerializedName("patient_list")
        @Expose
        private List<GetAll_UserResponse.PatientList> patientList = null;

        public List<GetAll_UserResponse.PatientList> getPatientList() {
            return patientList;
        }

        public void setPatientList(List<GetAll_UserResponse.PatientList> patientList) {
            this.patientList = patientList;
        }

    }
}
