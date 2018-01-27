package com.github.bilbobx182.mirrorm1rr0r;

/**
 * Created by CiaranLaptop on 27/01/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class DBManager {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Messages.db";

    private static final String KEY_ID = "_id";
    private static final String KEY_MESSAGEID = "messageID";
    private static final String TABE_MESSAGE = "Message";
    private static final String CREATE_SENSITIVITY_TABLE = "CREATE TABLE " + TABE_MESSAGE + "(_id INTEGER PRIMARY KEY autoincrement," + KEY_MESSAGEID + "TEXT);";

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
            db.execSQL(CREATE_SENSITIVITY_TABLE);

        }
        
        public void insertMessageToDatabase(SQLiteDatabase db, String value) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_MESSAGEID, value);
            db.insert(TABE_MESSAGE, null, initialValues);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABE_MESSAGE);
            onCreate(db);
        }
    }

    public DBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public void insertMessageToDatabase(String value) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MESSAGEID, value);
        db.insert(TABE_MESSAGE, null, initialValues);
      //  db.execSQL("UPDATE " + TABLE_STAT + " SET " + KEY_STAT + " = " + value + ";");
    }


    public Cursor getAll() {
        Cursor mCursor = db.rawQuery("SELECT * FROM " + TABE_MESSAGE, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public boolean deleteSen(String name) {
        return db.delete(TABE_MESSAGE, KEY_MESSAGEID + "=" + name, null) > 0;
    }

}