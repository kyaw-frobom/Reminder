package com.frobom.reminder;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Path;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public Attributes attToDB;
    private ListView itemsListView;

    private  ArrayList<Item> itemList=new ArrayList<>();
    public DatabaseAccessAdapter datasource;

    private  TextView edtTitle;
    private  TextView edtDescription;

    private String title;
    private String description;
    private String time;
    private String date;
    private String PathHolder;
    private String fileName;
    private Attributes attReturn;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize itemList1 to update data
        itemList.add(0, new Item("Date", ""));
        itemList.add(1, new Item("Time", ""));
        itemList.add(2, new Item("Alarm", ""));

        edtTitle = (TextView) findViewById(R.id.txtTitle);
        edtDescription = (TextView) findViewById(R.id.txtDescription);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();
        // Setup the data source to show at start
        final ArrayList<Item> itemsArrayList = generateItemsList(); // calls function to get items list

        // instantiate the custom list adapter
        CustomListAdapter adapter = new CustomListAdapter(this,itemsArrayList);

        // get the ListView and attach the adapter
        itemsListView  = (ListView) findViewById(R.id.listview);
        itemsListView.setAdapter(adapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?>adapter,View v, int position,long id){
                Intent intent;
                Calendar now = Calendar.getInstance();

                switch(position){
                    case 0:
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                AddActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.setThemeDark(true);
                        dpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                        dpd.setVersion(DatePickerDialog.Version.VERSION_1);
                        break;
                    case 1:
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                AddActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                now.get(Calendar.SECOND), false
                        );
                        tpd.setAccentColor(getResources().getColor(R.color.mdtp_accent_color));
                        tpd.setThemeDark(true);
                        tpd.show(getFragmentManager(), "Timepickerdialog");
                        tpd.setVersion(TimePickerDialog.Version.VERSION_1);
                        break;

                    case 2:
                        // Create the ACTION_GET_CONTENT INTENT to open file explorer

                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("audio/*");
                        startActivityForResult(intent, 7);
                        break;
                    //add more if you have more items in listview
                    //0 is the first item 1 second and so on...
                }

            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = edtTitle.getText().toString();
                description = edtDescription.getText().toString();

                attToDB = new Attributes();
                attToDB.setTitle(title);
                attToDB.setDescription(description);
                attToDB.setAlarmTime(time);
                attToDB.setAlarmDate(date);
                attToDB.setAlarmPath(PathHolder);
                attToDB.setEnabled("true");
               // Log.e("PathHolder ", PathHolder);
               // Toast.makeText(getApplicationContext(), PathHolder,Toast.LENGTH_LONG).show();

                if(title.matches("")||time == null||date == null||PathHolder == null) {
                    Toast.makeText(AddActivity.this, "You must add all of data field!", Toast.LENGTH_SHORT).show();
                }
                else{
                    attReturn = datasource.createAttributes(attToDB);
                    Toast.makeText(getApplicationContext(), "Data Added", Toast.LENGTH_LONG).show();
                    // Refresh main activity upon close of dialog box
                    Intent refresh = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }

                AddActivity.this.stopService(new Intent(AddActivity.this, ReminderAlarmManger.class));
                AddActivity.this.startService(new Intent(AddActivity.this, ReminderAlarmManger.class));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddActivity.this.stopService(new Intent(AddActivity.this, ReminderAlarmManger.class));
                AddActivity.this.startService(new Intent(AddActivity.this, ReminderAlarmManger.class));

                Intent i = new Intent(AddActivity.this, MainActivity.class );
                startActivity(i);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date = String.format("%02d",dayOfMonth) + "/" + String.format("%02d",(monthOfYear + 1)) + "/" + year;
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date today = c.getTime();

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        Date dateSpecified = c.getTime();

        if(dateSpecified.before(today)){
            Toast.makeText(this, "You cannot set the date that are expired!", Toast.LENGTH_SHORT).show();
        }
        else {
            //update itemList at the field of Date
            itemList.set(0, new Item("Date", date));
            itemsListView.setAdapter(new CustomListAdapter(this, itemList));
        }

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

        int hour = hourOfDay % 12;
        time = String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM");

        //update itemList at the field of Time
        itemList.set(1,new Item("Time",time));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        Log.e ("Build version " , " "+Build.VERSION.SDK_INT);
        String PathHolder1="";
        uri = data.getData();
        switch(requestCode){

            case 7:

                if(resultCode == RESULT_OK){

                    try {

                       /* if (Build.VERSION.SDK_INT < 11)
                            PathHolder1 = RealPathUtils.getRealPathFromURI_BelowAPI11(AddActivity.this, uri);

                            // SDK >= 11 && SDK < 19
                        else if (Build.VERSION.SDK_INT < 19)
                            PathHolder1 = RealPathUtils.getRealPathFromURI_API11to18(AddActivity.this, uri);

                            // SDK > 19 (Android 4.4)
                        else
                            PathHolder1 = RealPathUtils.getRealPathFromURI_API19(AddActivity.this, uri);
                        Log.d("File Path: " , PathHolder1);
                        // Get the file instance*/
                        Log.e("Uri auh before", uri.getAuthority());
                        PathHolder1 = getPath(this, data.getData());
                        //PathHolder1 = data.getData().getPath();
                    }
                    catch (Exception e) {
                    }

                    File file = new File(PathHolder1);
                    String fileName1 = file.getName();
                    String extension = fileName1.substring(fileName1.lastIndexOf(".") + 1, fileName1.length());
                    Log.e("Path : ",PathHolder1);
                        //if(extension.equals("mp3")|| extension.equals("m4a") || extension.equals("ogg")||
                         //       extension.equals("wma")) {
                            PathHolder = PathHolder1;
                            fileName = fileName1;
                            //update itemList1 at the field of Alarm
                            itemList.set(2, new Item("Alarm", fileName));
                            itemsListView.setAdapter(new CustomListAdapter(AddActivity.this, itemList));
                       // }
                        //else {
                          //  Toast.makeText(this, "You file must be audio file", Toast.LENGTH_SHORT).show();
                        //}
                }
                break;

        }
    }

    public ArrayList<Item> generateItemsList(){
        //setup data of add page
        return itemList;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }
    @SuppressLint("NewApi")
    public String getPath(Context context, Uri uri) throws URISyntaxException {
        Log.e("Uri auh ", uri.getAuthority());
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                Log.e("Uri authority external ", uri.getAuthority());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                Log.e("Uri internal ", uri.getAuthority());
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                return uri.getPath();
                /*Log.e("uri contents ", ""+uri);
                Log.e("Uri authority media ", uri.getAuthority());
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
               selection = "_id=?";
                selectionArgs = new String[]{split[1]};*/

            }

        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Log.e("Content verify ", ""+uri);
            String[] projection = {MediaStore.Audio.Media.DATA};
            Cursor cursor = null;
            try {

                cursor = managedQuery(uri, projection, selection, selectionArgs, null);
                Log.e("Return Path Last1" , ""+cursor);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

                if (cursor.moveToFirst()) {

                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            Log.e("File verify ", ""+uri.getPath());
            return uri.getPath();
        }
        else{
            Log.e("Other  ", ""+uri.getPath());
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @SuppressLint("NewApi")
    public String getRealPathFromURI(Uri contentUri)
    {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}


