package com.frobom.reminder;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    public DatabaseAccessAdapter datasource;
    private ListView listView;
    public Attributes att;
    private Attributes returnValue;
    private ArrayAdapter<Attributes> adapter;
    public final static String ID_EXTRA = "com.frobom.reminder_ID";
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
                Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(myIntent);
            }
        });

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        adapter = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, values);

        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                att = (Attributes) listView.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class );
                i.putExtra("attributeObject", att);
                startActivity(i);
            }
        });
    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId){
        MenuItem menuItem = menu.findItem(menuItemId);
        SpannableStringBuilder sBuilder = new SpannableStringBuilder("        " + getResources().getString(labelId));
        sBuilder.setSpan(new ImageSpan(this, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        menuItem.setTitle(sBuilder);
    }

    @Override
    protected void onResume() {
        datasource.open();
        adapter.notifyDataSetChanged();
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

        setIconInMenu(
                menu,
                R.id.action_help,
                R.string.action_help,
                R.drawable.ic_action_help
        );

        setIconInMenu(
                menu,
                R.id.action_feedback,
                R.string.action_feedback,
                R.drawable.ic_action_email
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            return true;
        }

        if (id == R.id.action_feedback) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}