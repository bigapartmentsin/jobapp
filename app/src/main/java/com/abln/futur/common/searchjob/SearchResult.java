package com.abln.futur.common.searchjob;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchResult implements Serializable {


    private String id;
    private String pid;
    private String uid;
    private String job_title;
    private String companyName;
    private String job_experience;
    private String jobPostImage1;
    private String jobPostImage2;
    private String jobPostImage3;
    private String jobPostImage4;
    private String jobPostImage5;
    private String jobPostImage6;
    private String jobPostImage7;
    private String jobPostImage8;
    private String jobPostImage9;
    private String jobPostImage10;
    private String jobPdf;
    private String lat;
    private String lng;
    private String jobExpDate;
    private String jobStatus;
    private String createdDate;
    private String industryType;
    private String firstPic;
    private String location;
    private String expStatus;
    private String distance;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUserApiKey() {
        return uid;
    }

    public void setUserApiKey(String uid) {
        this.uid = uid;
    }

    public String getJobTitle() {
        return job_title;
    }

    public void setJobTitle(String job_title) {
        this.job_title = job_title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobExperience() {
        return job_experience;
    }

    public void setJobExperience(String job_experience) {
        this.job_experience = job_experience;
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

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getFirstPic() {
        return firstPic;
    }

    public void setFirstPic(String firstPic) {
        this.firstPic = firstPic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExpStatus() {
        return expStatus;
    }

    public void setExpStatus(String expStatus) {
        this.expStatus = expStatus;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
