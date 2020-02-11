
package com.abln.futur.common.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@SuppressWarnings("unused")
public class Data  implements Serializable {

    @SerializedName("address")
    public String mAddress;
    @SerializedName("apikey")
    public String mApikey;
    @SerializedName("first_name")
    public String mFirstName;
    @SerializedName("gender")
    public String mGender;
    @SerializedName("lat")
    public String mLat;
    @SerializedName("lng")
    public String mLng;

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getApikey() {
        return mApikey;
    }

    public void setApikey(String apikey) {
        mApikey = apikey;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLng() {
        return mLng;
    }

    public void setLng(String lng) {
        mLng = lng;
    }

}
