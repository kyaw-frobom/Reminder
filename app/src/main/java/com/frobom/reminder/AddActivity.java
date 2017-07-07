package com.frobom.reminder;


import android.content.Intent;

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

import java.util.ArrayList;
import java.util.Calendar;

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
    private Attributes attReturn;

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
                        intent.setType("*/*");
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
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddActivity.this, MainActivity.class );
                startActivity(i);
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date = "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

        //update itemList at the field of Date
        itemList.set(0, new Item("Date", date));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList));

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String AM_PM ;
        if(hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            hourOfDay -= 12;
            AM_PM = "PM";
        }
        time = hourOfDay + " : " + minute + " " + AM_PM ;

        //update itemList at the field of Time
        itemList.set(1,new Item("Time",time));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode == RESULT_OK){

                    PathHolder = data.getData().getPath();
                    String[] filePath = PathHolder.split("/");
                    String fileName = filePath[filePath.length-1];
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    Log.e("Extension ",extension);
                    if(extension.equals("mp3")||extension.equals("m4a")||extension.equals("m4b")||
                            extension.equals("ogg")||extension.equals("3gp")||extension.equals("wma")||
                            extension.equals("msv")){
                        Toast.makeText(AddActivity.this, PathHolder, Toast.LENGTH_LONG).show();
                        //update itemList1 at the field of Alarm
                        itemList.set(2, new Item("Alarm", fileName));
                        itemsListView.setAdapter(new CustomListAdapter(AddActivity.this, itemList));

                    }
                    else {
                        Toast.makeText(this, "You file extension must be audio file!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }
    public ArrayList<Item> generateItemsList(){
        //setup data of add page
        return itemList;
    }
}
