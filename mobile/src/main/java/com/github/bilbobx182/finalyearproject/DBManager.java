package com.github.bilbobx182.finalyearproject;

/**
 * Created by CiaranLaptop on 27/01/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashMap;

public class DBManager {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SMWS.db";

    private static final String TABLE_MESSAGE = "Message";

    private static final String MESSAGE_ID = "_id";
    private static final String SENT_MESSAGE = "messageID";
    private static final String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGE + " ( " + MESSAGE_ID + " INTEGER PRIMARY KEY autoincrement, " + SENT_MESSAGE + " TEXT);";
    private static final String DROP_MESSAGES_TABLE = "DROP TABLE " + TABLE_MESSAGE + " ;";

    private final Context context;
    private MyDatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new MyDatabaseHelper(context);
    }

    private static class MyDatabaseHelper extends SQLiteOpenHelper {

        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_MESSAGES_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
            onCreate(db);
        }
    }

    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public boolean insertValue(String messageInput) {

        ContentValues values = new ContentValues();
        values.put(SENT_MESSAGE, messageInput);

        long newRowId = db.insert(TABLE_MESSAGE, null, values);
        if (newRowId >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public void close() {
        DBHelper.close();
    }

    public Cursor getMessages() {
        Cursor result = db.rawQuery("Select * from Message; ", null);
        try {
            while (result.moveToNext()) {
                String combined = "Hello " + result.getString(0);
                Log.d("Hello", combined);
            }

        } finally {
            result.close();
        }

        return result;
    }

    public HashMap<Integer, String> getMessagesHashMap() {
        Cursor result = db.rawQuery("Select * from Message; ", null);

        HashMap<Integer, String> values = new HashMap<>();

        try {
            int hashIndex = 0;
            while (result.moveToNext()) {
                values.put(hashIndex, result.getString(1));
                hashIndex++;
            }

        } finally {
            result.close();
        }

        return values;
    }
}

    /*
    To be used later in another class whenever that happens

       Cursor result = db.rawQuery("Select * from Message; ", null);
            try {
                while (result.moveToNext()) {
                    String combined = "Hello " + result.getString(0);
                    Log.d("Hello", combined);
                }
            } finally {
                result.close();
            }
    */

