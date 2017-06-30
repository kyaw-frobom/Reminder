package com.frobom.reminder;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
    public DatabaseAccessAdapter datasource;
    private ListView listView;
    private Attributes att;
    private Attributes returnValue;
    private ArrayAdapter<Attributes> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        adapter = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
      //  @SuppressWarnings("unchecked")
        //ArrayAdapter<String> adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,null);
       //List<String> adapter =new ArrayList<String>();
       // Attributes att1 = null;

        switch (view.getId()) {

            case R.id.add:
                att=new Attributes();
                att.setTitle("To go out");
                att.setDescription("To meet friends at the front Tower in 4pm!!Excited");
                att.setTime("2:30PM");
                att.setDate("3:30PM");
                att.setRemindUri("sdcard/songs/party.mp3");
                // save the new attribute to the database
                returnValue = datasource.createAttributes(att);
                Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_LONG).show();
                break;
            case R.id.delete:
             //   if (listView.getCount() > 0) {
                // att1 = (Attributes) listView.getItemAtPosition(0);
                    datasource.deleteAttributes(returnValue);
                Toast.makeText(getApplicationContext(),"Data Deleted",Toast.LENGTH_LONG).show();
                  // adapter.remove(att1);
               // }
                break;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
