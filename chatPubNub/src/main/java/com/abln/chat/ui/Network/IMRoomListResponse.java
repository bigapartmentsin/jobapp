
package com.abln.chat.ui.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.abln.chat.core.model.IMChatRoomDetail;

import java.io.Serializable;
import java.util.List;

public class IMRoomListResponse implements Serializable {

    @SerializedName("roomList")
    @Expose
    private List<RoomList> roomList = null;

    public List<RoomList> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<RoomList> roomList) {
        this.roomList = roomList;
    }

    public static class RoomList extends IMChatRoomDetail implements Serializable {

    }


}

