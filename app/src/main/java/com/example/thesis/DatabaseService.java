package com.example.thesis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Queue;

public class DatabaseService extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "thesis.db";

    public static final String TABLE_NAME = "pins_table";

    public static final String ID = "PIN_ID";
    public static final String PIN = "PIN_VALUE";

    public DatabaseService(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +
                " (ID INTEGER PRIMARY KEY, PIN_VALUE STRING)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PIN, value);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result == -1 ? false : true;
    }

    public String getPIN(int id) {
        // TODO: maybe use querybuilder.
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select PIN_VALUE from " + TABLE_NAME +
                "where PIN_ID = " + id, null);

        if (result.getCount() == 0) {
            StringBuffer stringBuffer = new StringBuffer();
            result.moveToNext();
            return result.getString(1);
        }
        return "";
    }
}
