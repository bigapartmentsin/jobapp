package com.abln.futur.module.account.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetSecuredModel  implements Serializable {

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

        @SerializedName("key_list")
        @Expose
        private ArrayList<KeyList> keyList = null;

        public ArrayList<KeyList> getKeyList() {
            return keyList;
        }

        public void setKeyList(ArrayList<KeyList> keyList) {
            this.keyList = keyList;
        }

    }


    public class KeyList {

        @SerializedName("idd")
        @Expose
        private String idd;
        @SerializedName("sercretkey")
        @Expose
        private String sercretkey;
        @SerializedName("ref")
        @Expose
        private String ref;

        public String getIdd() {
            return idd;
        }

        public void setIdd(String idd) {
            this.idd = idd;
        }

        public String getSercretkey() {
            return sercretkey;
        }

        public void setSercretkey(String sercretkey) {
            this.sercretkey = sercretkey;
        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

    }




}
