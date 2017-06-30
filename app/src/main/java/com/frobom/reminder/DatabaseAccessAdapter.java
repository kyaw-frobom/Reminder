package com.frobom.reminder;

/**
 * Created by Lenovo on 6/29/2017.
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DatabaseAccessAdapter {
        private Activity activity;
        // Database fields
        private SQLiteDatabase database;
        private MySQLiteHelper dbHelper;
        private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
                MySQLiteHelper.REMIND_TITLE, MySQLiteHelper.REMIND_CONTENT, MySQLiteHelper.DATE,
                MySQLiteHelper.TIME, MySQLiteHelper.REMIND_URI};

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
            values.put(MySQLiteHelper.REMIND_TITLE, att.getTitle());
            values.put(MySQLiteHelper.REMIND_CONTENT, att.getDescription());
            values.put(MySQLiteHelper.DATE,att.getTitle());
            values.put(MySQLiteHelper.TIME,att.getTime());
            values.put(MySQLiteHelper.REMIND_URI,att.getRemindUri());
            long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
                    values);
            Log.e("TEst","Successfully added");

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                    null, null, null);
            Log.e("TEst","Successfully query");


            cursor.moveToFirst();
            Attributes newComment = cursorToComment(cursor);
            cursor.close();
            return newComment;
        }

        public void deleteAttributes(Attributes comment) {
            long id = comment.getId();
            System.out.println("Comment deleted with id: " + id);
            database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
                    + " = " + id, null);
        }

        public List<Attributes> getAllAttributes() {
            List<Attributes> comments = new ArrayList<Attributes>();

            Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                    allColumns, null, null, null, null, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Attributes comment = cursorToComment(cursor);
                comments.add(comment);
                cursor.moveToNext();
            }
            // make sure to close the cursor
            cursor.close();
            return comments;
        }

        private Attributes cursorToComment(Cursor cursor) {
            Attributes att = new Attributes();
            att.setId(cursor.getInt(0));
            att.setTitle(cursor.getString(1));
            att.setDescription(cursor.getString(2));
            att.setDate(cursor.getString(3));
            att.setTime(cursor.getString(4));
            att.setRemindUri(cursor.getString(5));
            return att;
        }
    }

