package com.abln.futur.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetUserRequest implements Serializable {

    @SerializedName("doc_id")
    @Expose
    private String docId;
    @SerializedName("count")
    @Expose
    private int count;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
