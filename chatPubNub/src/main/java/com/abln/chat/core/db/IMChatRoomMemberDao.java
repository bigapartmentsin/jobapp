package com.abln.chat.core.db;


import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public abstract class IMChatRoomMemberDao {
    // Adds ChatRoomMember to the database
    @Insert(onConflict = REPLACE)
    public abstract void insert(IMChatRoomMember... chatRoomMember);

    @Insert(onConflict = REPLACE)
    public abstract void insertAll(List<IMChatRoomMember> chatRoomMemberList);

    // Update ChatRoomMember in the database
    @Update
    public abstract void update(IMChatRoomMember... chatRoomMember);

    @Update
    public abstract void updateAll(List<IMChatRoomMember> chatRoomMemberList);

    // Removes ChatRoomMember from the database
    @Delete
    abstract void delete(IMChatRoomMember... chatRoomMember);

    // Clear all data from ChatRoomMember Table
    @Query("DELETE FROM chatRoomDetailMember")
    public abstract void clearTable();
}
