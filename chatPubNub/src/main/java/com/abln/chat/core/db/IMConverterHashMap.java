package com.abln.chat.core.db;


import java.util.HashMap;

import androidx.room.TypeConverter;


public class IMConverterHashMap {
    @TypeConverter
    public static HashMap<String, String> toHashMap(byte[] roomMetaDataBytes) {
        try {
            return ((HashMap<String, String>) IMChatDBHelper.deserialize(roomMetaDataBytes));

        } catch (Exception ex) {
            return new HashMap<>();
        }

    }

    @TypeConverter
    public static byte[] toByteArray(HashMap<String, String> roomMetaData) {
        return IMChatDBHelper.serialize(roomMetaData);
    }
}
