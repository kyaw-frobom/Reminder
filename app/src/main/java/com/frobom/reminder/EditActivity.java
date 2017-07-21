package com.frobom.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public  Attributes attToDB;
    public ListView itemsListView;

    private  ArrayList<Item> itemList=new ArrayList<>();
    public DatabaseAccessAdapter datasource;

    private TextView edtTitle;
    private TextView edtDescription;
    public Button btnSave;
    public Button btnCancel;

    private String title;
    private String description;
    private String time;
    private String date;
    private String PathHolder;
    private String fileName;
    private String fileNameFrom;

    public Attributes att;
    private Attributes updateReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Bundle data = getIntent().getExtras();
        att = (Attributes) data.getParcelable("attributeObject");
        datasource = new DatabaseAccessAdapter(this);
        datasource.open();
        attToDB = att;

        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PathHolder = att.getAlarmPath();
        String[] filePath = PathHolder.split("/");
        File file=new File(PathHolder);
        fileNameFrom=file.getName();

        date = att.getAlarmDate();
        time = att.getAlarmTime();
        Log.e("att.getAlarmTime ()", att.getAlarmTime());
        itemList.add(0, new Item("Date", date));
        itemList.add(1, new Item("Time", time));
        itemList.add(2, new Item("Alarm", fileNameFrom));

        edtTitle = (TextView) findViewById(R.id.txtTitle);
        edtDescription = (TextView) findViewById(R.id.txtDescription);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        edtTitle.setText(att.getTitle());
        edtTitle.setFocusable(false);
        edtDescription.setText(att.getDescription());
        edtDescription.setFocusable(false);

        btnCancel.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);

        // Setup the data source to show at start
        final ArrayList<Item> itemsArrayList = generateItemsList(); // calls function to get items list

        // instantiate the custom list adapter
        final CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);

        // get the ListView and attach the adapter
        itemsListView = (ListView) findViewById(R.id.listview);
        itemsListView.setAdapter(adapter);
        itemsListView.setFocusable(false);
        itemsListView.setEnabled(false);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent;
                Calendar now = Calendar.getInstance();

                switch (position) {

                    case 0:
                        DatePickerDialog dpd = DatePickerDialog.newInstance(
                                EditActivity.this,
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
                                EditActivity.this,
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

                if(title.matches("")||time == null||date == null||PathHolder == null) {
                    Toast.makeText(EditActivity.this, "You must add all of data field!", Toast.LENGTH_SHORT).show();
                }
                else {
                    updateReturn = datasource.updateAttributes(attToDB, att.getId());
                    Toast.makeText(getApplicationContext(), "Data Updated!", Toast.LENGTH_LONG).show();
                    // Refresh main activity upon close of dialog box
                    //edtDescription.setEnabled(false);
                    edtDescription.setFocusableInTouchMode(false);
                   // edtTitle.setEnabled(false);
                    edtTitle.setFocusable(false);
                    edtDescription.setFocusable(false);
                    edtTitle.setFocusableInTouchMode(false);
                    btnCancel.setVisibility(View.GONE);
                    btnSave.setVisibility(View.GONE);
                    itemsListView.setEnabled(false);
                    setTitle("Detail");
                }

                stopService(new Intent(EditActivity.this, ReminderAlarmManger.class));
                startService(new Intent(EditActivity.this, ReminderAlarmManger.class));

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(new Intent(EditActivity.this, ReminderAlarmManger.class));
                startService(new Intent(EditActivity.this, ReminderAlarmManger.class));

                edtTitle.setText(attToDB.getTitle());
                edtDescription.setText(attToDB.getDescription());
                itemList.set(0, new Item("Date", attToDB.getAlarmDate()));
                itemList.set(1, new Item("Time", attToDB.getAlarmTime()));

                String[] filePath = attToDB.getAlarmPath().split("/");
                File file = new File(PathHolder);
                fileNameFrom = file.getName();
                itemList.set(2, new Item("Alarm", fileNameFrom));

                //CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);
                itemsListView.setAdapter(new CustomListAdapter(EditActivity.this,itemList));
                adapter.notifyDataSetChanged();

                edtTitle.setFocusable(false);
                edtDescription.setFocusable(false);
                edtTitle.setFocusableInTouchMode(false);
                btnCancel.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
                itemsListView.setEnabled(false);
                setTitle("Detail");

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
        itemList.set(1, new Item("Time", time));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        String PathHolder1 = "";
        switch(requestCode){

            case 7:

                if(resultCode == RESULT_OK){

                    try {
                         PathHolder1 = (new AddActivity()).getPath(this, data.getData());
                    }
                    catch (Exception msg){
                        msg.printStackTrace();
                    }
                    File file = new File(PathHolder1);
                    String fileName1 = file.getName();

                        PathHolder = PathHolder1;
                        fileName = fileName1;
                        //update itemList1 at the field of Alarm
                        itemList.set(2, new Item("Alarm", fileName));
                        itemsListView.setAdapter(new CustomListAdapter(EditActivity.this, itemList));
                        Toast.makeText(EditActivity.this, PathHolder , Toast.LENGTH_LONG).show();


                }
                break;

        }
    }
    public ArrayList<Item> generateItemsList(){
        //setup data of add page
        return itemList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.btn_edit: {

                edtTitle.setFocusableInTouchMode(true);
                edtDescription.setFocusableInTouchMode(true);
                btnSave.setText("Save");
                btnCancel.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.VISIBLE);
                itemsListView.setEnabled(true);
                setTitle("Edit");
                break;
            }

            case R.id.btn_delete:
                AlertDialog dialog = AskOption();
                dialog.show();
                return true;

        }

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Warning")
                .setMessage("Are you sure to delete?")
                .setIcon(R.drawable.ic_action_warning)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        datasource.deleteAttributes(att);
                        dialog.dismiss();
                        Toast.makeText( EditActivity.this, "Successfully Deleted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditActivity.this, MainActivity.class );
                        startActivity(i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return myQuittingDialogBox;
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
}
