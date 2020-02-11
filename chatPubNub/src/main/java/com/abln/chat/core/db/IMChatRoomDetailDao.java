package com.abln.chat.core.db;

import android.os.AsyncTask;

import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.utils.IMLog;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public abstract class IMChatRoomDetailDao {

    private static final String TAG = IMChatRoomDetailDao.class.getSimpleName();

    // Adds ChatRoomDetail to the database
    @Insert(onConflict = REPLACE)
    abstract void insert(IMChatRoomDetail... chatRoomDetail);

    // Update ChatRoomDetail in the database
    @Update
    abstract void update(IMChatRoomDetail... chatRoomDetail);

    // Removes ChatRoomDetail from the database
    @Delete
    abstract void delete(IMChatRoomDetail... chatRoomDetail);

    // Clear all data from ChatRoomDetail Table
    @Query("DELETE  FROM chatRoomDetail")
    public abstract void clearTable();

    // Update ChatRoom Mute Status in database for a room Id
    @Query("UPDATE chatRoomDetail SET isChatRoomMuted = :isChatRoomMuted WHERE roomId = :roomId")
    public abstract void updateChatRoomMuteStatus(String roomId, boolean isChatRoomMuted);

    // Update ChatRoom Delete Status in database for a room Id
    @Query("UPDATE chatRoomDetail SET isChatRoomDeleted = :isChatRoomDeleted WHERE roomId = :roomId")
    public abstract void updateChatRoomDeleteStatus(String roomId, boolean isChatRoomDeleted);

    // Update ChatRoom last typed text in database for a room Id
    @Query("UPDATE chatRoomDetail SET lastTypedText = :lastTypedText WHERE roomId = :roomId")
    public abstract void updateChatRoomLastTypedText(String roomId, String lastTypedText);

    // Update ChatRoom Name Status in database for a room Id
    @Query("UPDATE chatRoomDetail SET roomName = :roomName WHERE roomId = :roomId")
    public abstract void updateChatRoomName(String roomId, String roomName);

    // Gets all ChatRoomDetail from the database
    @Query("SELECT * FROM chatRoomDetail")
    public abstract List<IMChatRoomDetail> getAllChatRoomDetail();

    // Gets ChatRoomDetail from database for a room Id
    @Query("SELECT * FROM chatRoomDetail WHERE roomId = :roomId LIMIT 1")
    public abstract IMChatRoomDetail getChatRoomDetail(String roomId);

    public void createOrUpdateChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
            chatRoomMember.roomId = chatRoomDetail.roomId;
        }
        if (isChatRoomExist(chatRoomDetail.roomId)) {
            update(chatRoomDetail);
            IMAppDatabase.getDatabase().getIMChatRoomMemberDao().updateAll(chatRoomDetail.roomMembers);

        } else {
            insert(chatRoomDetail);
            IMAppDatabase.getDatabase().getIMChatRoomMemberDao().insertAll(chatRoomDetail.roomMembers);
        }
    }

    public boolean isChatRoomExist(String chatRoomId) {
        return (null != getChatRoomDetail(chatRoomId));
    }

    public ArrayList<String> getActiveChatRoomIdList(String loggedInUserId) {
        IMChatRoomMember chatRoomMember = new IMChatRoomMember();
        chatRoomMember.userId = loggedInUserId;

        ArrayList<String> activeChatRoomIdList = new ArrayList<>();
        try {
            ArrayList<IMChatRoomDetail> chatRoomDetailList = new ArrayList<>();
            List<IMChatRoomDetail> list = getAllChatRoomDetail();
            if (null != list) {
                chatRoomDetailList.addAll(list);
            }

            for (IMChatRoomDetail chatRoomDetail : chatRoomDetailList) {
                if (chatRoomDetail.roomMembers.contains(chatRoomMember)) {
                    activeChatRoomIdList.add(chatRoomDetail.roomId);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            IMLog.d(TAG, "Failed to getActiveChatRoomIdList");
        }

        return activeChatRoomIdList;
    }

    public ArrayList<String> getInActiveChatRoomIdList(String loggedInUserId) {
        ArrayList<String> inActiveChatRoomIdList = new ArrayList<>();
        ArrayList<String> activeChatRoomIdList = getActiveChatRoomIdList(loggedInUserId);

        try {
            ArrayList<IMChatRoomDetail> chatRoomDetailList = new ArrayList<>();
            List<IMChatRoomDetail> list = getAllChatRoomDetail();
            if (null != list) {
                chatRoomDetailList.addAll(list);
            }

            for (IMChatRoomDetail chatRoomDetail : chatRoomDetailList) {
                inActiveChatRoomIdList.add(chatRoomDetail.roomId);
            }

            inActiveChatRoomIdList.removeAll(activeChatRoomIdList);

        } catch (Exception ex) {
            ex.printStackTrace();
            IMLog.d(TAG, "Failed to getInActiveChatRoomIdList");
        }

        return inActiveChatRoomIdList;
    }

    public void deleteChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        new deleteAsyncTask().execute(chatRoomDetail);
    }

    private class deleteAsyncTask extends AsyncTask<Object, Void, Void> {

        deleteAsyncTask() {
        }

        @Override
        protected Void doInBackground(final Object... params) {
            if (params[0] instanceof IMChatRoomDetail) {
                IMChatRoomDetail chatRoomDetail = (IMChatRoomDetail) params[0];
                delete(chatRoomDetail);
            }
            return null;
        }

    }
}
