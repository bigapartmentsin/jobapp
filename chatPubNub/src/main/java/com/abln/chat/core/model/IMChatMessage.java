package com.abln.chat.core.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.abln.chat.core.db.IMConverterArrayList;
import com.abln.chat.core.db.IMConverterHashMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "chatMessage", indices = {
        @Index(value = {"msgId", "roomId"})})

public class IMChatMessage extends IMChatBaseMessage implements Serializable {
    @NonNull
    @PrimaryKey
    @SerializedName("msgId")
    @Expose
    public String msgId;
    @SerializedName("roomId")
    @Expose
    public String roomId;
    @SerializedName("msgSenderId")
    @Expose
    public String msgSenderId;
    @SerializedName("msgText")
    @Expose
    public String msgText;

    @SerializedName("msgMembers")
    @Expose
    @TypeConverters(IMConverterArrayList.class)
    public ArrayList<IMMsgMember> msgMembers = new ArrayList<>();

    @Entity(tableName = "chatMessageMember",
            foreignKeys = @ForeignKey(entity = IMChatMessage.class,
                    parentColumns = "msgId",
                    childColumns = "msgId",
                    onDelete = ForeignKey.CASCADE,
                    onUpdate = ForeignKey.CASCADE),
            indices = @Index(value = "msgId"))
    public static class IMMsgMember implements Serializable {
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id")
        @Expose
        private long id;
        @SerializedName("msgUserId")
        @Expose
        public String msgUserId;
        @SerializedName("msgDelivered")
        @Expose
        public long msgDelivered;
        @SerializedName("msgRead")
        @Expose
        public long msgRead;
        @SerializedName("msgId")
        @Expose
        public String msgId;

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    @Embedded
    public IMMultimediaInfo msgMultimediaInfo = new IMMultimediaInfo();

    public static class IMMultimediaInfo implements Serializable {
        public String imageOrVideoThumb;
        public String imageHeight;
        public String imageWidth;
        public String videoDuration;
    }

    @TypeConverters(IMConverterHashMap.class)
    public HashMap<String, String> msgMetaData = new HashMap<>();

    public boolean isChatMsgSent; // for locally use
    public boolean isChatMsgSeen; // for locally use
    public String imageOrVideoFilePath; // for locally use

    @Override
    public boolean equals(Object object) {
        if (object instanceof IMChatMessage) {
            IMChatMessage chatMessage = (IMChatMessage) object;
            return this.msgId.equals(chatMessage.msgId);

        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.msgId.hashCode();
    }

}
