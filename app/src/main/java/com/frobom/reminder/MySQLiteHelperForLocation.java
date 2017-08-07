package com.frobom.reminder;

/**
 * Created by Lenovo on 6/29/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class MySQLiteHelperForLocation extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "location_reminder_table";
    public static final String id = "id";
    public static final String title = "title";
    public static final String description = "description";
    public static final String alarmLocation = "alarmLocation";
    public static final String latitude = "latitude";
    public static final String longitude = "longitude";
    public static final String radius = "radius";
    public static final String alarmPath = "alarmPath";
    public static final String enabled = "enabled";

    public static final String DATABASE_NAME = "reminderLoc.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( "
            + id + " integer primary key autoincrement, "
            + title + " text not null, "
            + description + " text not null, "
            + alarmLocation + " text not null, "
            + latitude + " text not null, "
            + longitude + " text not null, "
            + radius + " integer not null, "
            + alarmPath + " text not null, "
            + enabled + " text not null );";

    public MySQLiteHelperForLocation(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("Test","Location Database created");

    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.e("Test","Location Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelperForLocation.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}