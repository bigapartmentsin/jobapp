package com.abln.futur.common.models;

import java.io.Serializable;

public class UpdateProfessionDetails  implements Serializable {

    public String apikey;
    public String techonlogy;
    public String occupation;
    public String experience;
    public String university;





    public String getApikey(){
        return apikey;
    }

    public void setApikey(String apikey){
        this.apikey = apikey;

    }


    public String getTechonlogy(){
        return techonlogy;
    }

    public void setTechonlogy(String techonlogy){
        this.techonlogy = techonlogy;
    }


    public String getOccupation(){
        return  occupation;
    }

    public void setOccupation(String occupation){
        this.occupation = occupation;

    }


    public String getExperience(){
        return experience;
    }


    public void setExperience(String experience){
        this.experience = experience;

    }


    public String getUniversity(){
        return university;
    }


    public void setUniversity(String university){
       this.university =  university ;
    }

}
