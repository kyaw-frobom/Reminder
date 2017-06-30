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

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "reminder";
    public static final String COLUMN_ID = "id";
    public static final String REMIND_TITLE = "title";
    public static final String REMIND_CONTENT="description";
    public  static final String TIME="time";
    public static  final String DATE="date";
    public static final String REMIND_URI="remindUri";
    public static SQLiteDatabase db;
    private static final String DATABASE_NAME = "reminderDB.db";
    private static final int DATABASE_VERSION = 1;

    private Activity activity;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + REMIND_TITLE
            + " text not null, " + REMIND_CONTENT + " text not null, "+ TIME + " text not null, " +
            DATE + " text not null, " + REMIND_URI + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("Test","Database created");

    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        Log.e("Test","Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}