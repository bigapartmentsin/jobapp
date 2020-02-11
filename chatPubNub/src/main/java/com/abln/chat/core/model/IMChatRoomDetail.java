package com.abln.chat.core.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.abln.chat.core.db.IMConverterArrayList;
import com.abln.chat.core.db.IMConverterHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "chatRoomDetail", indices = {
        @Index(value = {"roomId", "roomName"})})

public class IMChatRoomDetail extends IMChatBaseMessage implements Serializable {
    @NonNull
    @PrimaryKey
    @SerializedName("roomId")
    @Expose
    public String roomId;
    @SerializedName("adminId")
    @Expose
    public String adminId;
    @SerializedName("roomName")
    @Expose
    public String roomName;
    @SerializedName("roomType")
    @Expose
    public String roomType;
    @SerializedName("roomInfoUpdatedBy")
    @Expose
    public String roomInfoUpdatedBy;
    @SerializedName("roomInfoUpdatedTime")
    @Expose
    public long roomInfoUpdatedTime;
    @SerializedName("archiveKeyId")
    @Expose
    public String archiveKeyId;
    @SerializedName("isArchived")
    @Expose
    public boolean isArchived;

    @TypeConverters(IMConverterArrayList.class)
    @SerializedName("roomMembers")
    @Expose
    public ArrayList<IMChatRoomMember> roomMembers = new ArrayList<>();

    @Entity(tableName = "chatRoomDetailMember",
            foreignKeys = @ForeignKey(entity = IMChatRoomDetail.class,
                    parentColumns = "roomId",
                    childColumns = "roomId",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE),
            indices = @Index(value = "roomId"))
    public static class IMChatRoomMember implements Serializable {
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("userId")
        @Expose
        public String userId;
        @SerializedName("isDeleted")
        @Expose
        public boolean isDeleted;
        @SerializedName("isMute")
        @Expose
        public boolean isMute;
        @SerializedName("lastUpdatedTime")
        @Expose
        public long lastUpdatedTime;
        @SerializedName("roomId")
        @Expose
        public String roomId;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        public boolean isMute() {
            return isMute;
        }

        public void setMute(boolean mute) {
            isMute = mute;
        }

        public long getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        public void setLastUpdatedTime(long lastUpdatedTime) {
            this.lastUpdatedTime = lastUpdatedTime;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public Integer getReadCount() {
            return readCount;
        }

        public void setReadCount(Integer readCount) {
            this.readCount = readCount;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

        public Integer getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(Integer isAdmin) {
            this.isAdmin = isAdmin;
        }

        @SerializedName("lastMessage")
        @Expose
        private String lastMessage;
        @SerializedName("readCount")
        @Expose
        private Integer readCount;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;
        @SerializedName("isAdmin")
        @Expose
        private Integer isAdmin;


        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            boolean bool = false;
            if (obj instanceof IMChatRoomMember) {
                IMChatRoomMember chatRoomMember = (IMChatRoomMember) obj;
                bool = chatRoomMember.userId.equals(this.userId);
            }

            return bool;
        }

        @Override
        public int hashCode() {
            // 7 or 17 is Prime numbers, Prime number results in distinct hashcode for distinct object
            int hash = 7;
            hash = 17 * hash + (this.userId != null ? this.userId.hashCode() : 0);
            return hash;
        }
    }

    @TypeConverters(IMConverterHashMap.class)
    public HashMap<String, String> roomMetaData = new HashMap<>();

    public String lastTypedText = ""; // for locally use
    public boolean isChatRoomDeleted = false; // for locally use
    public boolean isChatRoomMuted = false; // for locally use

}
