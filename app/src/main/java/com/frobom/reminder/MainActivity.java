package com.frobom.reminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private static final int PERMISSION_REQUEST_CODE = 1;
    public DatabaseAccessAdapter datasource;
    public DatabaseAccessAdapter4Loc datasourceLoc;
    private ListView listViewToday, listViewTomorrow, listViewUpcoming, listViewLoc;
    public Attributes att;
    public LocationAttributes attLoc;
    private Attributes returnValue;
    private ArrayAdapter<Attributes> adapter1, adapter2, adapter3;
    private ArrayAdapter<LocationAttributes> adapter4;
    private String TO = "ei.yadanar.myint@frobom.com";
    public final static String ID_EXTRA = "com.frobom.reminder_ID";
    public SharedPreferences prefs = null;
    private AlertDialog.Builder myDialog;
    private String tab1 = "TODAY", tab2 = "TOMORROW", tab3 = "UPCOMING", tab4 = "LOCATION";
    private String[] array = new String[]{"Time Alarm ","Location Alarm"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkpermissionhere();

        prefs = getSharedPreferences("com.forbom.reminder", MODE_PRIVATE);

        listViewToday = (ListView) findViewById(R.id.list_today);
        listViewTomorrow = (ListView) findViewById(R.id.list_tomorrow);
        listViewUpcoming = (ListView) findViewById(R.id.list_upcoming);
        listViewLoc = (ListView) findViewById(R.id.list_location);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Choose Alarm Type !")
                        .setItems(array , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which == 0)
                                {
                                    Intent myIntent = new Intent(MainActivity.this, AddActivity.class);
                                    startActivity(myIntent);
                                }
                                else
                                    {
                                        Intent myIntent = new Intent(MainActivity.this, LocationActivity.class);
                                        startActivity(myIntent);
                                    }
                            }
                        });
                myDialog.show();
            }
        });

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("TODAY");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("TOMORROW");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("UPCOMING");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("LOCATION");

        tab1.setIndicator("TODAY");
        tab1.setContent(R.id.tab1);

        tab2.setIndicator("TOMORROW");
        tab2.setContent(R.id.tab2);

        tab3.setIndicator("UPCOMING");
        tab3.setContent(R.id.tab3);

        tab4.setIndicator("LOCATION");
        tab4.setContent(R.id.tab4);

        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

      /*  for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            //tv.setTextColor(Color.BLUE);
            tv.setTextSize(9);
        }*/
        datasource = new DatabaseAccessAdapter(this);
        datasource.open();
        datasourceLoc = new DatabaseAccessAdapter4Loc(this);
        datasourceLoc.open();

//        List<Attributes> values = datasource.getAllAttributes();
        List<Attributes> todayList = datasource.getTodayAttributes();
        List<Attributes> tomorrowList = datasource.getTomorrowAttributes();
        List<Attributes> upcomingList = datasource.getUpcomingAttributes();
        List<LocationAttributes> locationList = datasourceLoc.getAllAttributes();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        adapter1 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, todayList);

        adapter2 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, tomorrowList);

        adapter3 = new ArrayAdapter<Attributes>(this,
                android.R.layout.simple_list_item_1, upcomingList);

        adapter4 = new ArrayAdapter<LocationAttributes>(this,
                android.R.layout.simple_list_item_1, locationList);


        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        adapter3.notifyDataSetChanged();
        adapter4.notifyDataSetChanged();

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

        listViewLoc.setAdapter(adapter4);
        listViewLoc.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                attLoc = (LocationAttributes) listViewLoc.getItemAtPosition(position);
                Log.e("Path for Location Main ", attLoc.getAlarmPath());
                Intent i = new Intent(MainActivity.this, LocationDetailActivity.class );
                i.putExtra("attributeObjectLoc", attLoc);
                i.putExtra("calling_activity", ActivityConstants.MAIN_ACTIVITY);
                startActivity(i);
            }
        });

    }

    private void checkpermissionhere()
    {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        startService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, RefreshManager.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.e("PermissionResult:","GOT!");

                    //Start Alarm Manager
                    startService(new Intent(this, ReminderAlarmManger.class));
                    startService(new Intent(this, RefreshManager.class));

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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

        if(prefs != null)
        {
            if (prefs.getBoolean("firstrun", true)) {
                // Do first run stuff here then set 'firstrun' as false
                // using the following line to edit/commit prefs

                Intent Alarm = new Intent(this, alarm.class);
                Alarm.putExtra("id",-1);
                startActivity(Alarm);

                prefs.edit().putBoolean("firstrun", false).commit();
            }
        }
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

      menu.setGroupEnabled(1,true);
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