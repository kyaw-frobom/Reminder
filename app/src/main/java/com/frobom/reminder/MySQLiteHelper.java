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

    public static final String TABLE_NAME = "reminder_table";
    public static final String id = "id";
    public static final String title = "title";
    public static final String description = "description";
    public  static final String time = "time";
    public static  final String date = "date";
    public static final String alarm_path = "alarm_path";
    public static final String is_delete = "is_delete";
    public static SQLiteDatabase db;
    private static final String DATABASE_NAME = "reminderDB.db";
    private static final int DATABASE_VERSION = 1;

    private Activity activity;
    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( "
            + id + " integer primary key autoincrement, "
            + title + " text not null, "
            + description + " text not null, "
            + time + " text not null, "
            + date + " text not null, "
            + alarm_path + " text not null, "
            + is_delete + " text not null );";

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