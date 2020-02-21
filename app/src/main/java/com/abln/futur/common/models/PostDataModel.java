package com.abln.futur.common.models;

import java.io.Serializable;

public class PostDataModel implements Serializable {




    public String pid ;
    public String user_api_key;
    public String job_title;
    public String job_experience_from;
    public String job_experience_to;
    public String job_exp_date;
    public String first_pic;
    public String exp_status;
    public String range;
    public String days;



    public String getPid(){
        return pid;
    }

    public void setPid(String pid){
        this.pid = pid;
    }



    public String getUser_api_key(){
        return  user_api_key;
    }

    public void setUser_api_key(){
        this.user_api_key = user_api_key;
    }


    public String getJob_title(){
        return  job_title;
    }


    public void setJob_title( String job_title){
        this.job_title = job_title;
    }


    public String getJob_experience_from(){
        return  job_experience_from;
    }

    public void setJob_experience_from(String  job_experience_from){



        this.job_experience_from = job_experience_from;
    }

    public String getJob_experience_to(){
        return  job_experience_to;
    }


    public void setJob_experience_to(){
        this.job_experience_to = job_experience_to;
    }


    public String getJob_exp_date(){
        return job_exp_date;
    }


    public void setJob_exp_date(String job_exp_date){

        this.job_exp_date = job_exp_date;
    }

    public String getFirst_pic(){
        return first_pic;
    }


    public void setFirst_pic(String first_pic){
        this.first_pic = first_pic;
    }

    public String getExp_status(){
        return  exp_status;
    }


    public void setExp_status(String exp_status){
        this.exp_status = exp_status;
    }


    public String getRange(){
        return range;
    }


    public void setRange(){
        this.range = range;
    }


    public String getDays(){
  return  days;
    }


    public void setDays(String days){
        this.days = days;
    }


}
