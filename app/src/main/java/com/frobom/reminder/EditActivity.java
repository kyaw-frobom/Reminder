package com.frobom.reminder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.Calendar;

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
    private String AlarmName;

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


        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PathHolder = att.getAlarmPath();
        String[] filePath = PathHolder.split("/");
        File file=new File(PathHolder);
        String fileName=file.getName();

        date = att.getAlarmDate();
        time = att.getAlarmTime();
        Log.e("att.getAlarmTime ()", att.getAlarmTime());
        itemList.add(0, new Item("Date", date));
        itemList.add(1, new Item("Time", time));
        itemList.add(2, new Item("Alarm", fileName));

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
        CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);

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

                //Log.e("att.getId()",String.valueOf(att.getId()));
                //Log.e("Titile",title);
               // Log.e("Description",description);
                //Log.e("time",time);
               // Log.e("Date",date);
               Log.e("attdb",""+attToDB.getDescription()+"   "+attToDB.getAlarmTime());


                    updateReturn = datasource.updateAttributes(attToDB,att.getId());
                    Toast.makeText(getApplicationContext(), "Data Updated!", Toast.LENGTH_LONG).show();
                    // Refresh main activity upon close of dialog box
                    Intent refresh = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(refresh);
                    finish();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditActivity.this, MainActivity.class );
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
        itemList.set(1, new Item("Time", time));
        itemsListView.setAdapter(new CustomListAdapter(this,itemList));
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode == RESULT_OK){

                    PathHolder = data.getData().getPath();
                    //String[] filePath = PathHolder.split("/");
                   // String fileName = filePath[filePath.length-1];
                    File file=new File(PathHolder);
                    String fileName=file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
                    Log.e("Extension", extension);

                    if(extension.equals("mp3")||extension.equals("m4a")||extension.equals("m4b")||
                            extension.equals("ogg")||extension.equals("3gp")||extension.equals("wma")||
                            extension.equals("msv")){

                        //update itemList1 at the field of Alarm
                        itemList.set(2, new Item("Alarm", fileName));
                        itemsListView.setAdapter(new CustomListAdapter(EditActivity.this, itemList));

                    }
                    else {
                        Toast.makeText(this, "You file extension must be audio file!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(EditActivity.this, PathHolder , Toast.LENGTH_LONG).show();
                    }

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
}
