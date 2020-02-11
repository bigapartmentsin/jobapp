package com.abln.futur.common;

import com.google.gson.annotations.SerializedName;

public class ClinicFilesItem {

    @SerializedName("file")
    private String file;

    @SerializedName("id")
    private String id;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}