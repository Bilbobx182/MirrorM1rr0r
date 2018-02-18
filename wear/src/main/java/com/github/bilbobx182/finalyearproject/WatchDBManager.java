package com.github.bilbobx182.finalyearproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class WatchDBManager {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SMWSWatch.db";

    // Only Updates will ever be made. Ie new queue.

    private static final String TABLE_QUEUE = "Queue";
    public static final String QUEUE_ID = "_id";
    public static final String QUEUE_URL = "queueurl";


    private static final String CREATE_QUEUE_TABLE = "CREATE TABLE " + TABLE_QUEUE + " ( " + QUEUE_ID
            + " INTEGER PRIMARY KEY autoincrement, "
            + QUEUE_URL + " TEXT" + ");";

    private final Context context;
    private MyDatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public WatchDBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new MyDatabaseHelper(context);
    }

    private static class MyDatabaseHelper extends SQLiteOpenHelper {

        public MyDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUEUE_TABLE);

            ContentValues values = new ContentValues();

            // Sample value will be https://sqs.eu-west-1.amazonaws.com/186314837751/queuename.fifo
            values.put(QUEUE_URL, " ");


            long newRowId = db.insert(TABLE_QUEUE, null, values);
            if (newRowId >= 1) {
                Log.d("DB", "Created Sucessfully");
            } else {
                Log.d("DB,", "WOMP WOMP");
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEUE);
            onCreate(db);
        }
    }

    public WatchDBManager open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public boolean updateQueueURL(String queueURL) {

        String where = "_id == 1";
        ContentValues values = new ContentValues();
        values.put(QUEUE_URL, queueURL);

        db.update(TABLE_QUEUE, values, where, null);

        Cursor result = db.rawQuery("select * from " + TABLE_QUEUE, null);
        return true;
    }

    public String getQueueURL() {
        Cursor result = db.rawQuery("Select " + QUEUE_URL + " from " + TABLE_QUEUE + " ;", null);
        String columnData = null;
        while (result.moveToNext()) {
            columnData = result.getString(0);
            result.close();
        }
        return columnData;
    }

    public boolean isQueueURLSet() {
        Cursor result = db.rawQuery("Select " + QUEUE_URL + " from " + TABLE_QUEUE + " ;", null);
        boolean isQueueURLSet = false;

        while (result.moveToNext()) {
            if(result.getString(0).contains("http")) {
                isQueueURLSet = true;
            }
            result.close();
        }

        return isQueueURLSet;
    }

    public void close() {
        DBHelper.close();
    }
}
