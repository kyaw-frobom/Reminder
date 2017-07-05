package com.frobom.reminder;

import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private CustomListAdapter adapter;
    private  ArrayList<Item> itemList=new ArrayList<>();
    DatePickerDialog datePickerDialog;
    private EditText edtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        edtText=(EditText)findViewById(R.id.btnName);
        // Setup the data source
        ArrayList<Item> itemsArrayList = generateItemsList(); // calls function to get items list

// instantiate the custom list adapter
        CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);

// get the ListView and attach the adapter
        ListView itemsListView  = (ListView) findViewById(R.id.listview);
        itemsListView.setAdapter(adapter);
      Button   date=(Button)findViewById(R.id.btnSave);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
    }
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String date = "You picked the following date: "+dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                edtText.setText(date);
            }

    public ArrayList<Item> generateItemsList(){
        String title="Time";
        String description="12:30pm";

        Item it1=new Item(title,description);
        Item it2=new Item("Date","12/4/2017");
        Item it3=new Item("Alarm","morning.mp3");
        itemList.add(it1);
        itemList.add(it2);
        itemList.add(it3);

        return itemList;
    }
}
