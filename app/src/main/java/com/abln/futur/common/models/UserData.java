package com.abln.futur.common.models;

import java.io.Serializable;

public class UserData implements Serializable {





public String apikey;
public String gender;
public String first_name ;
public String lat ;
public String lng ;
public String address;

public String getApikey(){
    return  apikey;
}


public void setApikey(String apikey){
    this.apikey = apikey;
}


public String getGender(){
    return  gender;
}



public void setGender(String gender){
    this.gender = gender;
}


public String getFirst_name(){
    return  first_name;
}

public void setFirst_name(String first_name){
    this.first_name = first_name ;
}


public String getLat(){
    return  lat;
}


public void setLat(String lat){
    this.lat = lat;
}


public String getLng(){
    return  lat;
}


public void setLng(String lng){
    this.lng = lng;
}

public String getAddress(){
    return  address;
}


public void setAddress(String address){
    this.address = address ;
}
}
