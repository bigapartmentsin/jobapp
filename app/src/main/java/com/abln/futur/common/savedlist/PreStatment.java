package com.abln.futur.common.savedlist;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.nio.ByteBuffer;

public class PreStatment {


    private boolean isFinalized = false;
    private long sqliteStatementHandle;
    private boolean finalizeAfterQuery;


    public PreStatment(SQLiteDatabase db, String sql, boolean finalize) throws SQLiteException {
        finalizeAfterQuery = finalize;
        // sqliteStatementHandle = prepare(db, sql);


    }

    public long getStatementHandle() {
        return sqliteStatementHandle;
    }

//    public SQLiteCursor query(Object[] args) throws SQLiteException {
//        if (args == null) {
//            throw new IllegalArgumentException();
//        }
//
//        checkFinalized();
//
//        reset(sqliteStatementHandle);
//
//        int i = 1;
//        for (int a = 0; a < args.length; a++) {
//            Object obj = args[a];
//            if (obj == null) {
//                bindNull(sqliteStatementHandle, i);
//            } else if (obj instanceof Integer) {
//                bindInt(sqliteStatementHandle, i, (Integer) obj);
//            } else if (obj instanceof Double) {
//                bindDouble(sqliteStatementHandle, i, (Double) obj);
//            } else if (obj instanceof String) {
//                bindString(sqliteStatementHandle, i, (String) obj);
//            } else if (obj instanceof Long) {
//                bindLong(sqliteStatementHandle, i, (Long) obj);
//            } else {
//                throw new IllegalArgumentException();
//            }
//            i++;
//        }
//
//       // return new SQLiteCursor();
//    }

    public void finalizeQuery() {
        if (isFinalized) {
            return;
        }
        try {
            /*if (BuildVars.DEBUG_VERSION) {
                hashMap.remove(this);
            }*/
            isFinalized = true;
            finalize(sqliteStatementHandle);
        } catch (SQLiteException e) {


        }
    }


    public int step() throws SQLiteException {
        return step(sqliteStatementHandle);
    }


    public PreStatment stepThis() throws SQLiteException {
        step(sqliteStatementHandle);
        return this;
    }


    public void requery() throws SQLiteException {
        checkFinalized();
        reset(sqliteStatementHandle);
    }


    public void dispose() {
        if (finalizeAfterQuery) {
            finalizeQuery();
        }
    }


    void checkFinalized() throws SQLiteException {
        if (isFinalized) {
            throw new SQLiteException("Prepared query finalized");
        }
    }


    public void bindInteger(int index, int value) throws SQLiteException {
        bindInt(sqliteStatementHandle, index, value);
    }

    public void bindDouble(int index, double value) throws SQLiteException {
        bindDouble(sqliteStatementHandle, index, value);
    }

    public void bindByteBuffer(int index, ByteBuffer value) throws SQLiteException {
        bindByteBuffer(sqliteStatementHandle, index, value, value.limit());
    }

    public void bindByteBuffer(int index, NativeByteBuffer value) throws SQLiteException {
        bindByteBuffer(sqliteStatementHandle, index, value.buffer, value.limit());
    }

    public void bindString(int index, String value) throws SQLiteException {
        bindString(sqliteStatementHandle, index, value);
    }

    public void bindLong(int index, long value) throws SQLiteException {
        bindLong(sqliteStatementHandle, index, value);
    }

    public void bindNull(int index) throws SQLiteException {
        bindNull(sqliteStatementHandle, index);
    }


    native void bindByteBuffer(long statementHandle, int index, ByteBuffer value, int length) throws SQLiteException;

    native void bindString(long statementHandle, int index, String value) throws SQLiteException;

    native void bindInt(long statementHandle, int index, int value) throws SQLiteException;

    native void bindLong(long statementHandle, int index, long value) throws SQLiteException;

    native void bindDouble(long statementHandle, int index, double value) throws SQLiteException;

    native void bindNull(long statementHandle, int index) throws SQLiteException;

    native void reset(long statementHandle) throws SQLiteException;

    native long prepare(long sqliteHandle, String sql) throws SQLiteException;

    native void finalize(long statementHandle) throws SQLiteException;

    native int step(long statementHandle) throws SQLiteException;

}
