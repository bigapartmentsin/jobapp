package com.abln.futur.module.job.activities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyjobPost implements Serializable {


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

        @SerializedName("user_list")
        @Expose
        private List<UserList> userList = null;

        public List<UserList> getUserList() {
            return userList;
        }

        public void setUserList(List<UserList> userList) {
            this.userList = userList;
        }

    }


    public class UserList {

        @SerializedName("job_experience_from")
        @Expose
        public String job_experience_to;
        @SerializedName("job_experience_to")
        @Expose
        public String job_experience_from;

        @SerializedName("dcount")
        @Expose
        public String dcount;
        @SerializedName("exp_status")
        @Expose
        public String exp_status;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("post_id")
        @Expose
        private String postId;
        @SerializedName("user_api_key")
        @Expose
        private String userApiKey;
        @SerializedName("job_title")
        @Expose
        private String jobTitle;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("job_experience")
        @Expose
        private String jobExperience;
        @SerializedName("job_post_image1")
        @Expose
        private String jobPostImage1;
        @SerializedName("job_post_image2")
        @Expose
        private String jobPostImage2;
        @SerializedName("job_post_image3")
        @Expose
        private String jobPostImage3;
        @SerializedName("job_post_image4")
        @Expose
        private String jobPostImage4;
        @SerializedName("job_post_image5")
        @Expose
        private String jobPostImage5;
        @SerializedName("job_post_image6")
        @Expose
        private String jobPostImage6;
        @SerializedName("job_post_image7")
        @Expose
        private String jobPostImage7;
        @SerializedName("job_post_image8")
        @Expose
        private String jobPostImage8;
        @SerializedName("job_post_image9")
        @Expose
        private String jobPostImage9;
        @SerializedName("job_post_image10")
        @Expose
        private String jobPostImage10;
        @SerializedName("job_pdf")
        @Expose
        private String jobPdf;
        @SerializedName("job_location _lat")
        @Expose
        private String jobLocationLat;
        @SerializedName("job_location_lng")
        @Expose
        private String jobLocationLng;
        @SerializedName("job_exp_date")
        @Expose
        private String jobExpDate;
        @SerializedName("job_status")
        @Expose
        private String jobStatus;
        @SerializedName("created_date")
        @Expose
        private String createdDate;


        @SerializedName("pid")
        @Expose
        public  String pid;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getUserApiKey() {
            return userApiKey;
        }

        public void setUserApiKey(String userApiKey) {
            this.userApiKey = userApiKey;
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

        public String getJobPostImage1() {
            return jobPostImage1;
        }

        public void setJobPostImage1(String jobPostImage1) {
            this.jobPostImage1 = jobPostImage1;
        }

        public String getJobPostImage2() {
            return jobPostImage2;
        }

        public void setJobPostImage2(String jobPostImage2) {
            this.jobPostImage2 = jobPostImage2;
        }

        public String getJobPostImage3() {
            return jobPostImage3;
        }

        public void setJobPostImage3(String jobPostImage3) {
            this.jobPostImage3 = jobPostImage3;
        }

        public String getJobPostImage4() {
            return jobPostImage4;
        }

        public void setJobPostImage4(String jobPostImage4) {
            this.jobPostImage4 = jobPostImage4;
        }

        public String getJobPostImage5() {
            return jobPostImage5;
        }

        public void setJobPostImage5(String jobPostImage5) {
            this.jobPostImage5 = jobPostImage5;
        }

        public String getJobPostImage6() {
            return jobPostImage6;
        }

        public void setJobPostImage6(String jobPostImage6) {
            this.jobPostImage6 = jobPostImage6;
        }

        public String getJobPostImage7() {
            return jobPostImage7;
        }

        public void setJobPostImage7(String jobPostImage7) {
            this.jobPostImage7 = jobPostImage7;
        }

        public String getJobPostImage8() {
            return jobPostImage8;
        }

        public void setJobPostImage8(String jobPostImage8) {
            this.jobPostImage8 = jobPostImage8;
        }

        public String getJobPostImage9() {
            return jobPostImage9;
        }

        public void setJobPostImage9(String jobPostImage9) {
            this.jobPostImage9 = jobPostImage9;
        }

        public String getJobPostImage10() {
            return jobPostImage10;
        }

        public void setJobPostImage10(String jobPostImage10) {
            this.jobPostImage10 = jobPostImage10;
        }

        public String getJobPdf() {
            return jobPdf;
        }

        public void setJobPdf(String jobPdf) {
            this.jobPdf = jobPdf;
        }

        public String getJobLocationLat() {
            return jobLocationLat;
        }

        public void setJobLocationLat(String jobLocationLat) {
            this.jobLocationLat = jobLocationLat;
        }

        public String getJobLocationLng() {
            return jobLocationLng;
        }

        public void setJobLocationLng(String jobLocationLng) {
            this.jobLocationLng = jobLocationLng;
        }

        public String getJobExpDate() {
            return jobExpDate;
        }

        public void setJobExpDate(String jobExpDate) {
            this.jobExpDate = jobExpDate;
        }

        public String getJobStatus() {
            return jobStatus;
        }

        public void setJobStatus(String jobStatus) {
            this.jobStatus = jobStatus;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

    }
}