package com.abln.chat.core.db;


import com.abln.chat.core.model.IMChatMessage.IMMsgMember;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;

import java.util.ArrayList;

import androidx.room.TypeConverter;


public class IMConverterArrayList {

    @TypeConverter
    public static ArrayList<IMChatRoomMember> toChatRoomMemberList(byte[] roomMembersBytes) {
        try {
            return ((ArrayList<IMChatRoomMember>) IMChatDBHelper.deserialize(roomMembersBytes));

        } catch (Exception ex) {
            return new ArrayList<>();
        }

    }

    @TypeConverter
    public static byte[] toChatRoomMemberByteArray(ArrayList<IMChatRoomMember> roomMembers) {
        return IMChatDBHelper.serialize(roomMembers);
    }

    @TypeConverter
    public static ArrayList<IMMsgMember> toChatMsgMemberList(byte[] msgMembersBytes) {
        try {
            return ((ArrayList<IMMsgMember>) IMChatDBHelper.deserialize(msgMembersBytes));

        } catch (Exception ex) {
            return new ArrayList<>();
        }

    }

    @TypeConverter
    public static byte[] toChatMsgMemberByteArray(ArrayList<IMMsgMember> msgMembers) {
        return IMChatDBHelper.serialize(msgMembers);
    }
}
