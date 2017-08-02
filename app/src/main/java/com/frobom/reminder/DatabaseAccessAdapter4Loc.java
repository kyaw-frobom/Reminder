package com.frobom.reminder;

/**
 * Created by Lenovo on 6/29/2017.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;
import android.util.Log;
import android.widget.Toast;

import org.w3c.dom.Attr;

public class DatabaseAccessAdapter4Loc {
    private Activity activity;
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelperForLocation dbHelper;
    private String[] allColumns = {
            MySQLiteHelperForLocation.id,
            MySQLiteHelperForLocation.title,
            MySQLiteHelperForLocation.description,
            MySQLiteHelperForLocation.alarmLocation,
            MySQLiteHelperForLocation.latitude,
            MySQLiteHelperForLocation.longitude,
            MySQLiteHelperForLocation.enabled
    };

    public DatabaseAccessAdapter4Loc(Context context) {
        dbHelper = new MySQLiteHelperForLocation(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public LocationAttributes createAttributes(LocationAttributes att) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelperForLocation.title, att.getTitle());
        values.put(MySQLiteHelperForLocation.description, att.getDescription());
        values.put(MySQLiteHelperForLocation.alarmLocation, att.getAlarmLocation());
        values.put(MySQLiteHelperForLocation.longitude, att.getLatitude());
        values.put(MySQLiteHelperForLocation.longitude, att.getLongitude());
        values.put(MySQLiteHelper.enabled, att.isEnabled());

        long insertId = database.insert( MySQLiteHelperForLocation.TABLE_NAME, null, values);
        Log.e("insertId ",""+insertId);
        if(insertId < -1 )
            Log.e("Test","Not added");
        else
            Log.e("Test","Successfully added");

        Cursor cursor = database.query( MySQLiteHelperForLocation.TABLE_NAME,
                allColumns, MySQLiteHelper.id + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        LocationAttributes newAtti = cursorToAttribute(cursor);
        cursor.close();
        return newAtti;
    }

    public void deleteAttributes(LocationAttributes att) {
        long id = att.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelperForLocation.TABLE_NAME, MySQLiteHelperForLocation.id
                + " = " + id, null);
    }

    public List<LocationAttributes> getAllAttributes() {
        List<LocationAttributes> attributeList = new ArrayList<LocationAttributes>();

        Cursor cursor = database.query(MySQLiteHelperForLocation.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocationAttributes atti = cursorToAttribute(cursor);
            attributeList.add(atti);
            cursor.moveToNext();
        }

        // make sure to close the cursor
        cursor.close();
        return attributeList;
    }

    public LocationAttributes updateAttributes(Attributes att,int id){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.title, att.getTitle());
        values.put(MySQLiteHelper.description, att.getDescription());
        values.put(MySQLiteHelper.alarmTime, att.getAlarmTime());
        values.put(MySQLiteHelper.alarmDate, att.getAlarmDate());
        values.put(MySQLiteHelper.alarmPath, att.getAlarmPath());
        values.put(MySQLiteHelper.enabled, att.isEnabled());

        long insertId= database.update( MySQLiteHelper.TABLE_NAME,
                values,
                MySQLiteHelper.id + " = " + id,
                null);

        if(insertId < -1 )
            Log.e("Test","Not added");
        else
            Log.e("Test","Successfully updated!");

        Cursor cursor = database.query( MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.id + " = " + id, null,
                null, null, null);

        cursor.moveToFirst();
        LocationAttributes newAtti = cursorToAttribute(cursor);
        cursor.close();
        return newAtti;
    }

    private LocationAttributes cursorToAttribute(Cursor cursor) {
        LocationAttributes att = new LocationAttributes();
        att.setId(cursor.getInt(0));
        att.setTitle(cursor.getString(1));
        att.setDescription(cursor.getString(2));
        att.setAlarmLocation(cursor.getString(3));
        att.setLatitude(cursor.getString(4));
        att.setLongitude(cursor.getString(5));
        att.setEnabled(cursor.getString(6));
        return att;
    }



}

