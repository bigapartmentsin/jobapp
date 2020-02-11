package com.abln.chat.core.db;

import android.os.AsyncTask;

import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessage.IMMsgMember;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public abstract class IMChatMsgDao {
    private static final String TAG = IMChatMsgDao.class.getSimpleName();

    // Adds ChatMessage to the database
    @Insert(onConflict = REPLACE)
    abstract void insert(IMChatMessage... chatMessage);

    // Update ChatMessage in the database
    @Update
    public abstract void update(IMChatMessage... chatMessage);

    // Removes ChatMessage from the database
    @Delete
    abstract void delete(IMChatMessage... chatMessage);

    // Clear all data from ChatMessage Table
    @Query("DELETE FROM chatMessage")
    public abstract void clearTable();

    // Update ChatMessage Sent Status and Time in database for a msg Id
    @Query("UPDATE chatMessage SET isChatMsgSent = :isChatMsgSent, timeToken = :msgTimeToken WHERE msgId = :msgId")
    public abstract void updateChatMessageSentStatusAndTimeByMsgId(String msgId, boolean isChatMsgSent, long msgTimeToken);

    // Update ChatMessage Seen Status in database for a room Id
    @Query("UPDATE chatMessage SET isChatMsgSeen = :isChatMsgSeen WHERE roomId = :roomId")
    public abstract void updateChatMessageSeenStatusByRoomId(String roomId, boolean isChatMsgSeen);

    // Update ChatMessage Seen Status in database for a msg Id
    @Query("UPDATE chatMessage SET isChatMsgSeen = :isChatMsgSeen WHERE msgId = :msgId")
    public abstract void updateChatMessageSeenStatusByMsgId(String msgId, boolean isChatMsgSeen);

    // Update ChatMessage Seen Status in database for a msg Id
    @Query("UPDATE chatMessage SET msgMembers = :msgMembers WHERE msgId = :msgId")
    public abstract void updateChatMessageMemberBytesByMsgId(byte[] msgMembers, String msgId);

    // Delete ChatMessage from database for a room Id
    @Query("DELETE FROM chatMessage WHERE roomId = :roomId")
    abstract int deleteChatMessageByRoomId(String roomId);

    // Gets all ChatMessage from the database
    @Query("SELECT * FROM chatMessage")
    public abstract LiveData<List<IMChatMessage>> getAllChatMessage();

    // Gets ChatMessage from database for a msg Id
    @Query("SELECT * FROM chatMessage WHERE msgId = :msgId LIMIT 1")
    public abstract IMChatMessage getChatMessageByMsgId(String msgId);

    // Gets ChatMessages from database for a room Id
    @Query("SELECT * FROM chatMessage WHERE roomId = :roomId")
    public abstract List<IMChatMessage> getChatMessageListByRoomId(String roomId);

    // Gets Latest ChatMessages from database for a room id
    @Query("SELECT * FROM chatMessage WHERE roomId = :roomId ORDER BY timeToken DESC LIMIT 1")
    public abstract IMChatMessage getLastChatMessageForRoomId(String roomId);

    // Gets Latest ChatMessages from database for a room id
    @Query("SELECT * FROM chatMessage WHERE roomId = :roomId ORDER BY timeToken")
    public abstract  List<IMChatMessage> getChatMessageForRoomIdInSorted(String roomId);

    // Gets Unseen ChatMessages from database for a room id
    @Query("SELECT * FROM chatMessage WHERE roomId = :roomId AND isChatMsgSeen = 0")
    public abstract List<IMChatMessage> getUnseenChatMessageByRoomId(String roomId);

    // Gets Unseen ChatMessages from database
    @Query("SELECT * FROM chatMessage WHERE isChatMsgSeen = 0")
    public abstract List<IMChatMessage> getTotalUnseenChatMessage();

    // Gets ChatMessages from database for a searched msg text
    @Query("SELECT * FROM chatMessage WHERE msgText LIKE :searchText")
    public abstract List<IMChatMessage> getChatMessageBySearchText(String searchText);

    // Gets ChatMessages from database for a searched msg text
    @Query("SELECT max(timeToken) FROM chatMessage WHERE roomId = :roomId")
    public abstract long getLatestMsgTimeTokenByRoomId(String roomId);


    public void insertChatMessage(IMChatMessage chatMessage) {
        for (IMMsgMember msgMember : chatMessage.msgMembers) {
            msgMember.msgId = chatMessage.msgId;
        }
        insert(chatMessage);
        IMAppDatabase.getDatabase().getIMChatMsgMemberDao().insertAll(chatMessage.msgMembers);
    }


    public IMChatMessage getLatestChatMessageByRoomId(String roomId) {
        return getLastChatMessageForRoomId(roomId);
    }

    public int getUnSeenChatMessageCountByRoomId(String chatRoomId) {
        List<IMChatMessage> list = getUnseenChatMessageByRoomId(chatRoomId);
        if (null == list) {
            return 0;
        } else {
            return list.size();
        }
    }

    public int getTotalUnSeenChatMessageCount() {
        List<IMChatMessage> list = getTotalUnseenChatMessage();
        if (null == list) {
            return 0;
        } else {
            return list.size();
        }
    }


    public void deleteChatMessage(IMChatMessage chatMessage) {
        new deleteAsyncTask().execute(chatMessage);
    }

    public void deleteChatMessagesByRoomId(String roomId) {
        new deleteAsyncTask().execute(roomId);
    }

    private class deleteAsyncTask extends AsyncTask<Object, Void, Void> {

        deleteAsyncTask() {

        }

        @Override
        protected Void doInBackground(final Object... params) {
            if (params[0] instanceof IMChatMessage) {
                IMChatMessage chatMessage = (IMChatMessage) params[0];
                delete(chatMessage);

            } else if (params[0] instanceof String) {
                String roomId = (String) params[0];
                deleteChatMessageByRoomId(roomId);
            }
            return null;
        }

    }

}
