package com.abln.chat.core.model;


import com.abln.chat.core.db.IMConverterHashMap;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity(tableName = "chatUser", indices = {@Index(value = {"userId"})})
public class IMChatUser {
    @NonNull
    @PrimaryKey
    public String userId;
    public String userName;
    public String userThumbnailURL;
    public byte[] userThumbnailBytes;

    @TypeConverters(IMConverterHashMap.class)
    public HashMap<String, String> chatUserExtras = new HashMap<>();

    public boolean isOnline;
}
