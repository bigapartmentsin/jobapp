package com.abln.futur.common.models;

import java.io.Serializable;

public class GetTotalNumberPost implements Serializable {

    public  String apikey ;

    public String getApikey(){
        return  apikey;
    }

    public void setApikey(){
        this.apikey = apikey;
    }

}
