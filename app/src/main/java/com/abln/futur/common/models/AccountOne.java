package com.abln.futur.common.models;

import java.io.Serializable;

public class AccountOne implements Serializable
{

    public String apikey ;
    public String account_id;
    public String key;


    public String getApikey(){
        return  apikey;

    }



    public void setApikey(String apikey){
        this.apikey = apikey;

    }


    public String getAccount_id(){
        return  account_id;
    }


    public void setAccount_id(){
        this.account_id = account_id;
    }

}
