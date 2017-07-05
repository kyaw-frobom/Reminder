package com.frobom.reminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public DatabaseAccessAdapter datasource;
    private Attributes att, returnValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.add:

                att = new Attributes();
                att.setTitle("To go out");
                att.setDescription("To meet friends at the front Tower in 4pm!!Excited");
                att.setAlarmTime("2:30PM");
                att.setAlarmDate("4/7/2017");
                att.setAlarmPath("sdcard/songs/party.mp3");
                att.setEnabled("true");
                // save the new attribute to the database
                returnValue = datasource.createAttributes(att);
                Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_LONG).show();
                break;
            case R.id.cancel:
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            //   if (listView.getCount() > 0) {
            // att1 = (Attributes) listView.getItemAtPosition(0);
            //datasource.deleteAttributes(returnValue);
            //Toast.makeText(getApplicationContext(),"Data Deleted",Toast.LENGTH_LONG).show();
            // adapter.remove(att1);
            // }
            //break;
        }
    }
}
