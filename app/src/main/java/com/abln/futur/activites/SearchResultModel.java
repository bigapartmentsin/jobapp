package com.abln.futur.activites;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class SearchResultModel implements Serializable {




    public String mobile_code ;
    public String mobile_flag;
    public String mobile ;
    public String gender;
    public String first_name;
    public String technology;
    public String occupation ;
    public String expericence ;
    public String university;
    public String avatar;
    public String apikey;
    public String resume;
    public String filename;
    public String distance ;

    public String getMobile_code(){
        return  mobile_code;

    }



    public void setMobile_code(String mobile_code){
        this.mobile_code = mobile_code;
    }


    public String getMobile_flag(){
        return mobile_flag;
    }

    public void setMobile_flag(String mobile_flag){
        this.mobile_flag = mobile_flag;
    }
    public String getMobile(){
        return mobile;
    }


    public void setMobile(String mobile){
        this.mobile = mobile;
    }


    public String getGender(){
        return  gender;
    }


    public void setGender(String gender){
        this.gender = gender;
    }

    public String getFirst_name(){
        return first_name;
    }

    public void setFirst_name(String first_name){

        this.first_name = first_name;

    }


    public String getTechnology(){
        return  technology;
    }


    public void setTechnology(String technology){
        this.technology = technology;
    }


    public String getOccupation(){
        return occupation;
    }

    public void setOccupation(String occupation){
        this.occupation = occupation;
    }



    public String getAvatar(){
        return avatar;
    }


    public void setAvatar(){
        this.avatar = avatar;
    }


    public String getApikey(){
        return  apikey;
    }


    public void setApikey(){

        this.apikey = apikey;
    }


    public String getResume(){
        return resume;
    }


    public void setResume(String resume){
        this.resume = resume;
    }

    public String getFilename(){
        return filename;

    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }


    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }



}


