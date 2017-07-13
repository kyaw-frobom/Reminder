package com.frobom.reminder;


import android.content.Context;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.Toast;

import java.util.List;

import java.util.Random;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {

    public DatabaseAccessAdapter datasource;
    private ListView listViewToday, listViewTomorrow, listViewUpcoming;
    public Attributes att;
    private Attributes returnValue;
    private ArrayAdapter<Attributes> adapter1, adapter2, adapter3;
    private String TO = "eiyadanr70@gmial.com";
    public final static String ID_EXTRA = "com.frobom.reminder_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start Alarm Manager
        startService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, RefreshManager.class));

        listViewToday = (ListView) findViewById(R.id.list_today);
        listViewTomorrow = (ListView) findViewById(R.id.list_tomorrow);
        listViewUpcoming = (ListView) findViewById(R.id.list_upcoming);

        //Start Alarm Manager
        startService(new Intent(this, ReminderAlarmManger.class));

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

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TODAY");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("TOMORROW");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("UPCOMING");

        tab1.setIndicator("TODAY");
        tab1.setContent(R.id.tab1);

        tab2.setIndicator("TOMORROW");
        tab2.setContent(R.id.tab2);

        tab3.setIndicator("UPCOMING");
        tab3.setContent(R.id.tab3);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

//        List<Attributes> values = datasource.getAllAttributes();
        List<Attributes> todayList = datasource.getTodayAttributes();
        List<Attributes> tomorrowList = datasource.getTomorrowAttributes();
        List<Attributes> upcomingList = datasource.getUpcomingAttributes();
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        adapter1 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, todayList);

        adapter2 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, tomorrowList);

        adapter3 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, upcomingList);

        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();

        listViewToday.setAdapter(adapter1);
        listViewToday.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                att = (Attributes) listViewToday.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class );
                i.putExtra("attributeObject", att);
                startActivity(i);
            }
        });

        listViewTomorrow.setAdapter(adapter2);
        listViewTomorrow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                att = (Attributes) listViewTomorrow.getItemAtPosition(position);
                Intent i = new Intent(MainActivity.this, EditActivity.class );
                i.putExtra("attributeObject", att);
                startActivity(i);
            }
        });

        listViewUpcoming.setAdapter(adapter3);
        listViewUpcoming.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                att = (Attributes) listViewUpcoming.getItemAtPosition(position);
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
        //Start Alarm Manager
        //stopService(new Intent(this, ReminderAlarmManger.class));
        //startService(new Intent(this, ReminderAlarmManger.class));
        datasource.open();
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //Start Alarm Manager
        //stopService(new Intent(this, ReminderAlarmManger.class));
        //startService(new Intent(this, ReminderAlarmManger.class));
        //datasource.close();
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
            Intent intent = new Intent(MainActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_feedback) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + "eiyadanar70@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
            intent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));

                Log.i("Finished sending email!", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }

        }
        return super.onOptionsItemSelected(item);
    }

}