package com.frobom.reminder;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private CustomListAdapter adapter;
    public DatePickerDialog datePickerDialog;
    private Item item;
    public Attributes attToDB;
    private Attributes attReturn;
    private ListView itemsListView;
    private  static final int REQUEST_CHOOSER = 1234;

    private  ArrayList<Item> itemList=new ArrayList<>();
    private  ArrayList<Item> itemList1=new ArrayList<Item>();
    public DatabaseAccessAdapter datasource;

    private  TextView edtTitle;
    private  TextView edtDescription;

    private String title;
    private String description;
    private String time;
    private String date;
    private String PathHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //initialize itemList1 to update data
        itemList1.add(0,new Item("Time",""));
        itemList1.add(1,new Item("Date",""));
        itemList1.add(2,new Item("Alarm",""));

        edtTitle=(TextView) findViewById(R.id.txtTitle);
        edtDescription=(TextView) findViewById(R.id.txtDescription);
        Button btnSave=(Button)findViewById(R.id.btnSave);

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
                        TimePickerDialog tpd = TimePickerDialog.newInstance(
                                AddActivity.this,
                                now.get(Calendar.HOUR_OF_DAY),
                                now.get(Calendar.MINUTE),
                                now.get(Calendar.SECOND), false
                        );
                        tpd.show(getFragmentManager(), "Timepickerdialog");
                        break;
                    case 1:
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                AddActivity.this,
                                now.get(Calendar.YEAR),
                                now.get(Calendar.MONTH),
                                now.get(Calendar.DAY_OF_MONTH)
                        );
                        dpd.show(getFragmentManager(), "Datepickerdialog");
                        break;
                    case 2:
                        // Create the ACTION_GET_CONTENT INTENT to open file explorer
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, 7);
                        Toast.makeText(getApplicationContext(),"File Opened",Toast.LENGTH_LONG).show();
                        break;
                    //add more if you have more items in listview
                    //0 is the first item 1 second and so on...
                }

            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=edtTitle.getText().toString();
                description=edtDescription.getText().toString();

                attToDB=new Attributes();
                attToDB.setTitle(title);
                attToDB.setDescription(description);
                attToDB.setAlarmTime(time);
                attToDB.setAlarmDate(date);
                attToDB.setAlarmPath(PathHolder);
                attToDB.setEnabled("true");

                if(title==null||description==null||time==null||date==null||PathHolder==null) {
                    Toast.makeText(AddActivity.this, "You must add all of data field!", Toast.LENGTH_SHORT).show();

                }
                else{
                    attReturn=datasource.createAttributes(attToDB);
                    Toast.makeText(getApplicationContext(), "Data Added", Toast.LENGTH_LONG).show();
                    // Refresh main activity upon close of dialog box
                    Intent refresh = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();
                }
            }
        });
    }
    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String AM_PM ;
        if(hourOfDay < 12) {
            AM_PM = "AM";
        } else {
            hourOfDay-=12;
            AM_PM = "PM";
        }

        time=hourOfDay + " : " + minute + " " + AM_PM ;
        itemList1.set(0,new Item("Time",time));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList1));
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        date =""+ dayOfMonth+"/"+(monthOfYear+1)+"/"+year;

        itemList1.set(1,new Item("Date",date));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList1));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    PathHolder = data.getData().getPath();
                    String[] filePath=PathHolder.split("/");
                    String fileName=filePath[filePath.length-1];
                    Toast.makeText(AddActivity.this, PathHolder , Toast.LENGTH_LONG).show();
                    //update itemList1 at the field of Alarm
                    itemList1.set(2,new Item("Alarm",fileName));
                    itemsListView.setAdapter(new CustomListAdapter(AddActivity.this,itemList1));
                }
                break;

        }
    }
    public ArrayList<Item> generateItemsList(){
        //setup data of add page
        String title="Time";
        String description="";

        Item it1=new Item(title,description);
        Item it2=new Item("Date","");
        Item it3=new Item("Alarm","");
        itemList.add(it1);
        itemList.add(it2);
        itemList.add(it3);

        return itemList;
    }
}
