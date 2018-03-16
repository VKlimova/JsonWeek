package com.amargodigits.weekdaycalendar;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amargodigits.weekdaycalendar.WDScheduleContract;

public class WDScheduleDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "waitlist.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public WDScheduleDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold waitlist data
        final String SQL_CREATE_WD_TABLE = "CREATE TABLE " + WDScheduleContract.ScheduleEntry.TABLE_NAME + " (" +
                WDScheduleContract.ScheduleEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WDScheduleContract.ScheduleEntry.COLUMN_DAYID + " INTEGER ," +
                WDScheduleContract.ScheduleEntry.COLUMN_DAYNAME + " TEXT NOT NULL, " +
                WDScheduleContract.ScheduleEntry.COLUMN_DAYSCHEDULE + " TEXT NOT NULL, " +
                WDScheduleContract.ScheduleEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one. This means if you change the
        // DATABASE_VERSION the table will be dropped.
        // In a production app, this method might be modified to ALTER the table
        // instead of dropping it, so that existing data is not deleted.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WDScheduleContract.ScheduleEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}