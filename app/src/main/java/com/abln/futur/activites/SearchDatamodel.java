package com.abln.futur.activites;

import java.io.Serializable;

public class SearchDatamodel implements Serializable {

    public String mobile_code;
    public String mobile_flag;
    public String mobile;
    public String gender;
    public String first_name;
    public String technology;
    public String occupation;
    public String experience;
    public String university;
    public String avatar;
    public String apikey;
    public String resume;
    public String distance;

    public String filename;


    private boolean isSelected;

//------- handling the isselected  ---- //

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    //------- handling the isselected  ---- //




    public String getMobile_code() {
        return mobile_code;
    }

    public void setMobile_code(String mobile_code) {
        this.mobile_code = mobile_code;
    }

    public String getMobile_flag() {
        return mobile_flag;
    }

    public void setMobile_flag(String mobile_flag) {
        this.mobile_flag = mobile_flag;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
