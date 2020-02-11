package com.abln.chat.ui.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import com.abln.chat.core.model.IMChatMessage;


public class IMMessageListResponse extends IMChatMessage implements Serializable {

    @SerializedName("messageList")
    @Expose
    private List<IMChatMessage> messageList = null;

    public List<IMChatMessage> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<IMChatMessage> messageList) {
        this.messageList = messageList;
    }


}






