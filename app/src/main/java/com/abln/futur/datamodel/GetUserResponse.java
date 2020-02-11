package com.abln.futur.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetUserResponse {

    @SerializedName("statuscode")
    @Expose
    private Integer statuscode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("data")
    @Expose
    private Data data;

    public static List<PatientList> createMovies(int itemCount) {
        List<PatientList> movies = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            GetUserResponse.PatientList movie = new GetUserResponse.PatientList("Movie " + (itemCount == 0 ?
                    (itemCount + 1 + i) : (itemCount + i)));
            movies.add(movie);
        }
        return movies;
    }

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

    public static class PatientList {

        @SerializedName("patient_id")
        @Expose
        private String patientId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("photo")
        @Expose
        private String photo;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("age")
        @Expose
        private String age;
        @SerializedName("last_visit")
        @Expose
        private String lastVisit;
        @SerializedName("mobile")
        @Expose
        private String mobile;

        public PatientList(String s) {
        }

        public PatientList() {

        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPatientId() {
            return patientId;
        }

        public void setPatientId(String patientId) {
            this.patientId = patientId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getLastVisit() {
            return lastVisit;
        }

        public void setLastVisit(String lastVisit) {
            this.lastVisit = lastVisit;
        }

    }

    public class Data {

        @SerializedName("pagelimit")
        @Expose
        private Integer pagelimit;
        @SerializedName("pagecount")
        @Expose
        private String pagecount;
        @SerializedName("patient_list")
        @Expose
        private List<PatientList> patientList = null;

        public Integer getPagelimit() {
            return pagelimit;
        }

        public void setPagelimit(Integer pagelimit) {
            this.pagelimit = pagelimit;
        }

        public String getPagecount() {
            return pagecount;
        }

        public void setPagecount(String pagecount) {
            this.pagecount = pagecount;
        }

        public List<PatientList> getPatientList() {
            return patientList;
        }

        public void setPatientList(List<PatientList> patientList) {
            this.patientList = patientList;
        }

    }
}



