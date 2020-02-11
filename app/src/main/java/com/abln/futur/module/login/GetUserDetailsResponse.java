package com.abln.futur.module.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetUserDetailsResponse implements Serializable {

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


    public class Data {

        @SerializedName("apikey")
        @Expose
        private String apikey;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("mobile_code")
        @Expose
        private String mobileCode;
        @SerializedName("mobile_flag")
        @Expose
        private String mobileFlag;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("techonlogy")
        @Expose
        private String techonlogy;
        @SerializedName("occupation")
        @Expose
        private String occupation;
        @SerializedName("university")
        @Expose
        private String university;
        @SerializedName("user_resume")
        @Expose
        private Object userResume;




        @SerializedName("experience")
        @Expose
        public  String exp;


        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobileCode() {
            return mobileCode;
        }

        public void setMobileCode(String mobileCode) {
            this.mobileCode = mobileCode;
        }

        public String getMobileFlag() {
            return mobileFlag;
        }

        public void setMobileFlag(String mobileFlag) {
            this.mobileFlag = mobileFlag;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getTechonlogy() {
            return techonlogy;
        }

        public void setTechonlogy(String techonlogy) {
            this.techonlogy = techonlogy;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getUniversity() {
            return university;
        }

        public void setUniversity(String university) {
            this.university = university;
        }

        public Object getUserResume() {
            return userResume;
        }

        public void setUserResume(Object userResume) {
            this.userResume = userResume;
        }

    }
}