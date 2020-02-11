package com.abln.chat.core.db;

import android.content.Context;

import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessage.IMMsgMember;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMLog;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {IMChatRoomDetail.class,
        IMChatRoomMember.class,
        IMChatMessage.class,
        IMMsgMember.class,
        IMChatUser.class},
        version = 2, exportSchema = false)
public abstract class IMAppDatabase extends RoomDatabase {
    private static final String TAG = IMAppDatabase.class.getSimpleName();


    private static final String CHAT_DATABASE_NAME = "IMChat";

    private static IMAppDatabase INSTANCE;

    public static void initDatabase(Context context) {
        if (null == INSTANCE) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), IMAppDatabase.class,
                    CHAT_DATABASE_NAME).allowMainThreadQueries().build();
        }
    }

    public static IMAppDatabase getDatabase() {
        return INSTANCE;
    }

    public abstract IMChatRoomDetailDao getIMChatRoomDetailDao();

    public abstract IMChatRoomMemberDao getIMChatRoomMemberDao();

    public abstract IMChatMsgDao getIMChatMessageDao();

    public abstract IMChatMsgMemberDao getIMChatMsgMemberDao();

    public abstract IMChatUserDao getIMChatUserDao();

    public void clearAllChatData() {
        try {
            getDatabase().getIMChatRoomDetailDao().clearTable();
            getDatabase().getIMChatMsgMemberDao().clearTable();
            getDatabase().getIMChatMessageDao().clearTable();
            getDatabase().getIMChatMsgMemberDao().clearTable();
            getDatabase().getIMChatUserDao().clearTable();

        } catch (Exception ex) {
            ex.printStackTrace();
            IMLog.d(TAG, "Failed to clearAllChatData");
        }
    }
}
