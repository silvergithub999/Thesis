package com.example.thesis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This class handles database related functions.
 * The database just holds just values for PIN 1 and PIN 2.
 *
 * thesis.db:
 * id | value
 * 1  | 1234
 * 2  | 12345
 */
public class DatabaseService extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "thesis.db";
    public static final String TABLE_NAME = "pins_table";
    public static final String ID = "ID";
    public static final String PIN = "PIN_VALUE";


    /**
     * The constructor.
     * @param context - the app context.
     */
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


    /**
     * Inserts value into the database. If the value already exists then it updates the value.
     * @param id - id of the PIN, 1 or 2.
     * @param value - value of the PIN.
     * @return true if succeeded, false if not.
     */
    public boolean insertData(int id, String value) {
        if (getPIN(id) == null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, id);
            contentValues.put(PIN, value);
            long result = db.insert(TABLE_NAME, null, contentValues);
            return result != -1;
        } else {
            return updateData(id, value);
        }
    }


    /**
     * Updates the value in the table.
     * @param id - id of the PIN, 1 or 2.
     * @param value - value of the PIN.
     * @return true if succeeded, false if not.
     */
    private boolean updateData(int id, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, id);
        contentValues.put(PIN, value);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[]{Integer.toString(id)});
        return true;
    }


    /**
     * Finds PIN X from the database.
     * @param id - which PIN, 1 or 2.
     * @return the PIN value or if it isn't there, then it returns null.
     */
    public String getPIN(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select PIN_VALUE from " + TABLE_NAME +
                " where ID = " + id, null);

        if (result.getCount() != 0) {
            result.moveToNext();
            return result.getString(0);
        }
        return null;
    }
}
