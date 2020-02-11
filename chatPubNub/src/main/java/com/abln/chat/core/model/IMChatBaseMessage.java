package com.abln.chat.core.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class IMChatBaseMessage implements Serializable {
    @SerializedName("origin")
    @Expose
    public String origin;
    @SerializedName("msgType")
    @Expose
    public String msgType;
    @SerializedName("msgVersion")
    @Expose
    public String msgVersion;
    @SerializedName("timeToken")
    @Expose
    public long timeToken;
}
