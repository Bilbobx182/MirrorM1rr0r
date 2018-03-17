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
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class DBManager {
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "SMWS.db";

    private static final String TABLE_MESSAGE_NAME = "Message";
    private static final String TABLE_USER_NAME = "User";

    private static final String MESSAGE_ID = "_id";
    public static final String SENT_MESSAGE = "message";
    public static final String SENT_COORDS = "location";
    public static final String SENT_COLOR = "fontColour";
    public static final String SENT_SIZE = "fontSize";

    public static final String USER_ID = "_id";
    public static final String USER_FIRSTNAME = "firstname";
    public static final String USER_SURNAME = "surname";
    public static final String USER_IMEI = "imei";
    public static final String USER_QUEUE = "queue";
    public static final String USER_PROFILE_PATH = "profilePath";


    private static final String CREATE_MESSAGES_TABLE = "CREATE TABLE " + TABLE_MESSAGE_NAME + " ( " + MESSAGE_ID
            + " INTEGER PRIMARY KEY autoincrement, "
            + SENT_MESSAGE + " TEXT,"
            + SENT_COLOR + " TEXT,"
            + SENT_SIZE + " TEXT,"
            + SENT_COORDS + " TEXT" + ");";

    private static final String DROP_MESSAGES_TABLE = "DROP TABLE " + TABLE_MESSAGE_NAME + " ;";


    // Only Updates will ever be made to the user table. Intial empty data set.

    private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER_NAME + " ( " + USER_ID
            + " INTEGER PRIMARY KEY autoincrement, "
            + USER_FIRSTNAME + " TEXT,"
            + USER_SURNAME + " TEXT,"
            + USER_IMEI + " TEXT,"
            + USER_QUEUE + " TEXT,"
            + USER_PROFILE_PATH + " TEXT" + ");";

    private static final String DROP_MESSAGES_USERS = "DROP TABLE " + TABLE_USER_NAME + " ;";

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
            db.execSQL(CREATE_USER_TABLE);

            ContentValues values = new ContentValues();
            values.put(USER_FIRSTNAME, "Enter name");
            values.put(USER_SURNAME, "Enter surname");
            values.put(USER_IMEI, "358098070043286");
            values.put(USER_QUEUE, "");
            values.put(USER_PROFILE_PATH, "");


            long newRowId = db.insert(TABLE_USER_NAME, null, values);
            if (newRowId >= 1) {
                Log.d("DB", "Created Sucessfully");
            } else {
                Log.d("DB,", "WOMP WOMP");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
            onCreate(db);
        }
    }

    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        ciaranTest();
        return this;
    }

    private void ciaranTest() {
        Cursor result = db.rawQuery("Select * from user; ", null);
        String columnData = null;
        while (result.moveToNext()) {
            columnData = result.getString(0);
            Log.d("DB", columnData);
        }

    }

    public boolean insertValue(HashMap<String, String> messageValues) {

        ContentValues values = new ContentValues();

        for (Map.Entry<String, String> entry : messageValues.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }

        long newRowId = db.insert(TABLE_MESSAGE_NAME, null, values);
        if (newRowId >= 1) {
            return true;

        } else {
            return false;
        }
    }

    public boolean updateUserInformation(String column, String message) {

        String where = "_id == 1";
        ContentValues values = new ContentValues();
        values.put(column, message);
        db.update(TABLE_USER_NAME, values, where, null);

        Cursor result = db.rawQuery("select * from " + TABLE_USER_NAME, null);
        int counter = 0;
        while (result.moveToNext()) {
            String combined = "Hello " + result.getString(counter);
            counter++;
            Log.d("Hello", combined);
        }
        return true;
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

    public int getMessageCount() {
        int maxMessageCountError = -1;

        Cursor result = db.rawQuery("Select count(_id) from Message; ", null);
        try {
            while (result.moveToNext()) {
                return Integer.parseInt(result.getString(0));
            }
        } finally {
            result.close();
        }

        return maxMessageCountError;
    }

    public HashMap<Integer, String> getMessagesHashMap(String column) {
        Cursor result = db.rawQuery("Select " + column + " from Message; ", null);

        HashMap<Integer, String> values = new HashMap<>();

        try {
            int hashIndex = 0;

            result.moveToLast();
            do {
                values.put(hashIndex, result.getString(0));
                hashIndex++;
            }
            while (result.moveToPrevious());

        } finally {
            result.close();
        }

        return values;
    }

    public HashMap<String, String> getMessageByIndex(int indexFromRecyclerView) {
        Cursor result = db.rawQuery("Select * from Message where ( _id == " + String.valueOf(indexFromRecyclerView) + ");", null);
        HashMap<String, String> values = new HashMap<>();
        try {
            result.moveToLast();

            do {
                //ToDo Refactor this, make it better. It's not the best right now
                values.put(SENT_MESSAGE.toLowerCase(), result.getString(1));
                values.put(SENT_COLOR.toLowerCase(), result.getString(2));
                values.put(SENT_SIZE.toLowerCase(), result.getString(3));
                values.put(SENT_COORDS.toLowerCase(), result.getString(4));
            }
            while (result.moveToPrevious());

        } finally {
            result.close();
        }

        return values;
    }


    public String getUserInformationByColumn(String column) {
        Cursor result = db.rawQuery("Select " + column + " from user; ", null);
        String columnData = null;
        while (result.moveToNext()) {
            columnData = result.getString(0);
            result.close();
        }
        return columnData;
    }

    public boolean isFirstBoot() {
        return context.getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("initialBoot", true);
    }
}
