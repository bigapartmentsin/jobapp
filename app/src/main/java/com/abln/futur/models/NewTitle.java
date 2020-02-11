
package com.abln.futur.models;


import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class NewTitle {

    @SerializedName("data")
    private Data mData;
    @SerializedName("statusMessage")
    private String mStatusMessage;
    @SerializedName("statuscode")
    private Long mStatuscode;

    public Data getData() {
        return mData;
    }

    public String getStatusMessage() {
        return mStatusMessage;
    }

    public Long getStatuscode() {
        return mStatuscode;
    }

    public static class Builder {

        private Data mData;
        private String mStatusMessage;
        private Long mStatuscode;

        public NewTitle.Builder withData(Data data) {
            mData = data;
            return this;
        }

        public NewTitle.Builder withStatusMessage(String statusMessage) {
            mStatusMessage = statusMessage;
            return this;
        }

        public NewTitle.Builder withStatuscode(Long statuscode) {
            mStatuscode = statuscode;
            return this;
        }

        public NewTitle build() {
            NewTitle newTitle = new NewTitle();
            newTitle.mData = mData;
            newTitle.mStatusMessage = mStatusMessage;
            newTitle.mStatuscode = mStatuscode;
            return newTitle;
        }

    }





}
