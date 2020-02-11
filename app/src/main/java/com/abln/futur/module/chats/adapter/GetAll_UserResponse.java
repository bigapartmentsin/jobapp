package com.abln.futur.module.chats.adapter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class GetAll_UserResponse implements Serializable {

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

    public static class PatientList implements Serializable {

        @SerializedName("patient_id")
        @Expose
        private String patientId;

        @SerializedName("photo")
        @Expose
        private String photo;


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
        private List<PatientList> patientList = null;

        public List<PatientList> getPatientList() {
            return patientList;
        }

        public void setPatientList(List<PatientList> patientList) {
            this.patientList = patientList;
        }

    }


}