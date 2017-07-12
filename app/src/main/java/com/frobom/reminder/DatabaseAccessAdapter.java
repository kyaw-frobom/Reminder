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

public class DatabaseAccessAdapter {
        private Activity activity;
        // Database fields
        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String[] allColumns = {
                MySQLiteHelper.id,
                MySQLiteHelper.title,
                MySQLiteHelper.description,
                MySQLiteHelper.alarmTime,
                MySQLiteHelper.alarmDate,
                MySQLiteHelper.alarmPath,
                MySQLiteHelper.enabled
        };

        public DatabaseAccessAdapter(Context context) {
            dbHelper = new MySQLiteHelper(context);
        }

        public void open() throws SQLException {
            database = dbHelper.getWritableDatabase();
        }

        public void close() {
            dbHelper.close();
        }

        public Attributes createAttributes(Attributes att) {
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.title, att.getTitle());
            values.put(MySQLiteHelper.description, att.getDescription());
            values.put(MySQLiteHelper.alarmTime, att.getAlarmTime());
            values.put(MySQLiteHelper.alarmDate, att.getAlarmDate());
            values.put(MySQLiteHelper.alarmPath, att.getAlarmPath());
            values.put(MySQLiteHelper.enabled, att.isEnabled());

            long insertId = database.insert( MySQLiteHelper.TABLE_NAME, null, values);
            Log.e("insertId ",""+insertId);
            if(insertId < -1 )
                Log.e("Test","Not added");
            else
                Log.e("Test","Successfully added");

            Cursor cursor = database.query( MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.id + " = " + insertId, null,
                    null, null, null);
            Log.e("Test","Successfully query");

            cursor.moveToFirst();
            Attributes newAtti = cursorToAttribute(cursor);
            cursor.close();
            return newAtti;
        }

        public void deleteAttributes(Attributes att) {
            long id = att.getId();
            System.out.println("Comment deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.id
                    + " = " + id, null);
        }

        public List<Attributes> getAllAttributes() {
            List<Attributes> attributeList = new ArrayList<Attributes>();

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attributes atti = cursorToAttribute(cursor);
                attributeList.add(atti);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
            return attributeList;
        }

        public Attributes updateAttributes(Attributes att,int id){
            ContentValues values = new ContentValues();
            values.put(MySQLiteHelper.title, att.getTitle());
            values.put(MySQLiteHelper.description, att.getDescription());
            values.put(MySQLiteHelper.alarmTime, att.getAlarmTime());
            values.put(MySQLiteHelper.alarmDate, att.getAlarmDate());
            values.put(MySQLiteHelper.alarmPath, att.getAlarmPath());
            values.put(MySQLiteHelper.enabled, att.isEnabled());

            Log.e("att.getTitle()  ",""+ id +"  "+att.getTitle()+"  "+att.getAlarmTime());
            long insertId= database.update( MySQLiteHelper.TABLE_NAME,
                    values,
                    MySQLiteHelper.id + " = " + id,
                    null);

            Log.e("insertId"," "+insertId);
            Log.e("Test","Successfully Updated!");
            if(insertId < -1 )
                Log.e("Test","Not added");
            else
                Log.e("Test","Successfully updated!");

            Cursor cursor = database.query( MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.id + " = " + id, null,
                    null, null, null);
            Log.e("Test","Successfully query");
            Log.e("Cusor size",""+cursor.getCount());

            cursor.moveToFirst();
            Attributes newAtti = cursorToAttribute(cursor);
            cursor.close();
            return newAtti;
        }

        private Attributes cursorToAttribute(Cursor cursor) {
            Attributes att = new Attributes();
            att.setId(cursor.getInt(0));
            att.setTitle(cursor.getString(1));
            att.setDescription(cursor.getString(2));
            att.setAlarmDate(cursor.getString(3));
            att.setAlarmTime(cursor.getString(4));
            att.setAlarmPath(cursor.getString(5));
            att.setEnabled(cursor.getString(6));
            return att;
        }


        public List<Attributes> getTodayAttributes() {
            List<Attributes> attributeList = new ArrayList<Attributes>();

            Calendar c = Calendar.getInstance();
            int day = c.get (Calendar.DATE);
            int month = c.get(Calendar.MONTH ) + 1;
            int year = c.get(Calendar.YEAR);
            String TodayDate = day + "/" + month + "/" + year;

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.alarmDate + " = " + TodayDate , null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attributes atti = cursorToAttribute(cursor);
                attributeList.add(atti);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
            return attributeList;
        }

        public List<Attributes> getTomorrowAttributes() {
            List<Attributes> attributeList = new ArrayList<Attributes>();

            Calendar c = Calendar.getInstance();
            int day = c.get (Calendar.DATE) + 1;
            int month = c.get(Calendar.MONTH ) + 1;
            int year = c.get(Calendar.YEAR);
            String TomorrowDate = day + "/" + month + "/" + year;

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.alarmDate + " = " + TomorrowDate, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attributes atti = cursorToAttribute(cursor);
                attributeList.add(atti);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
            return attributeList;
        }

        public List<Attributes> getUpcomingAttributes() {
            List<Attributes> attributeList = new ArrayList<Attributes>();

            Calendar c = Calendar.getInstance();
            int day = c.get (Calendar.DATE);
            int month = c.get(Calendar.MONTH ) + 1;
            int year = c.get(Calendar.YEAR);
            String TodayDate = day + "/" + month + "/" + year;
            String TomorrowDate = (day + 1) + "/" + month + "/" + year;
            Log.e("Today Date : " , TodayDate);
            Log.e("Tomorrow Date : " , TomorrowDate);

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.alarmDate + " != " + TodayDate + " AND "
                            + MySQLiteHelper.alarmDate + " != " + TomorrowDate,
                    null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attributes atti = cursorToAttribute(cursor);
                attributeList.add(atti);
                cursor.moveToNext();
            }

            // make sure to close the cursor
            cursor.close();
            return attributeList;
        }
    }

