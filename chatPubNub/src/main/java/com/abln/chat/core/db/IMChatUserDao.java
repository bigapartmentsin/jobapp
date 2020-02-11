package com.abln.chat.core.db;


import com.abln.chat.core.model.IMChatUser;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public abstract class IMChatUserDao {
    private static final String TAG = IMChatUserDao.class.getSimpleName();

    // Adds ChatMessage to the database
    @Insert(onConflict = REPLACE)
    public abstract void insert(IMChatUser... chatUser);

    @Insert(onConflict = REPLACE)
    public abstract void insertAll(List<IMChatUser> chatUserList);

    // Update ChatMessage in the database
    @Update
    public abstract void update(IMChatUser... chatUser);

    // Removes ChatMessage from the database
    @Delete
    public abstract void delete(IMChatUser... chatUser);

    // Clear all data from chatUser Table
    @Query("DELETE FROM chatUser")
    public abstract void clearTable();

    // Gets All ChatUser from database
    @Query("SELECT * FROM chatUser")
    public abstract List<IMChatUser> getAllChatUsers();

    // Gets ChatUser from database for a user Id
    @Query("SELECT * FROM chatUser WHERE userId = :userId LIMIT 1")
    public abstract IMChatUser getChatUserById(String userId);


    public ArrayList<IMChatUser> getChatUserListByUserIds(ArrayList<String> userIdList) {
        ArrayList<IMChatUser> chatUserList = new ArrayList<>();

        ArrayList<IMChatUser> list = new ArrayList<>();
        List<IMChatUser> chatAllUserList = getAllChatUsers();
        if (null != chatAllUserList) {
            list.addAll(chatAllUserList);
        }

        for (IMChatUser chatUser : list) {
            if (userIdList.contains(chatUser.userId)) {
                chatUserList.add(chatUser);
            }
        }

        return chatUserList;
    }
}
