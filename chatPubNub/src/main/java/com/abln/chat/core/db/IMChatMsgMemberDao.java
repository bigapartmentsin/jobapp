package com.abln.chat.core.db;


import com.abln.chat.core.model.IMChatMessage.IMMsgMember;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.utils.IMConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public abstract class IMChatMsgMemberDao {

    // Adds MsgMember to the database
    @Insert(onConflict = REPLACE)
    public abstract void insert(IMMsgMember... msgMember);

    @Insert(onConflict = REPLACE)
    public abstract void insertAll(List<IMMsgMember> chatMsgMemberList);

    // Update MsgMember in the database
    @Update
    public abstract void update(IMMsgMember... msgMember);

    // Removes MsgMember from the database
    @Delete
    abstract void delete(IMMsgMember... msgMember);

    // Clear all data from chatMessageMember Table
    @Query("DELETE FROM chatMessageMember")
    public abstract void clearTable();

    // Gets ChatMessages from database for a room Id
    @Query("SELECT * FROM chatMessageMember WHERE msgId = :msgId")
    public abstract List<IMMsgMember> getChatMessageMemberListByRoomId(String msgId);

    // Update ChatMessage Delivered Status in chatMessageMember Table in database for a msg and msgUser id
    @Query("UPDATE chatMessageMember SET msgDelivered = :msgDelivered WHERE msgId = :msgId AND msgUserId =:msgUserId")
    abstract void updateChatMessageDeliveredStatus(long msgDelivered, String msgId, String msgUserId);

    // Update ChatMessage Read Status in chatMessageMember Table in database for a msg and msgUser id
    @Query("UPDATE chatMessageMember SET msgRead = :msgRead WHERE msgId = :msgId AND msgUserId =:msgUserId")
    abstract void updateChatMessageReadStatus(long msgRead, String msgId, String msgUserId);

    public void updateChatMessageStatus(IMChatMessageStatus chatMessageStatus) {
        if (chatMessageStatus.msgStatus.equals(IMConstants.CHAT_MESSAGE_DELIVERED_STATUS)) {
            updateChatMessageDeliveredStatus(chatMessageStatus.timeToken, chatMessageStatus.msgId, chatMessageStatus.msgSenderId);

        } else if (chatMessageStatus.msgStatus.equals(IMConstants.CHAT_MESSAGE_READ_STATUS)) {
            updateChatMessageReadStatus(chatMessageStatus.timeToken, chatMessageStatus.msgId, chatMessageStatus.msgSenderId);
        }

        ArrayList<IMMsgMember> msgMemberList = new ArrayList<>(getChatMessageMemberListByRoomId(chatMessageStatus.msgId));
        IMAppDatabase.getDatabase().getIMChatMessageDao().updateChatMessageMemberBytesByMsgId(
                IMConverterArrayList.toChatMsgMemberByteArray(msgMemberList), chatMessageStatus.msgId);
    }
}
