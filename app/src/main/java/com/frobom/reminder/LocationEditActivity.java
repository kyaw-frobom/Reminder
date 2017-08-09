package com.frobom.reminder;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationEditActivity extends AppCompatActivity {

    private LocationAttributes attLoc;
    private TextView txtTitle;
    private TextView txtDescription;
    private Button btnUpdate;
    private Button btnCancel;
    private TextView txtaddress;
    private Spinner spinner;
    int PLACE_PICKER_REQUEST = 1;
    private String title;
    private String description;
    private String addressLocation;
    private String radius;
    private String latitude;
    private String longitude;
    private String PathHolder;
    private String fileName;
    private Uri uri;

    public DatabaseAccessAdapter4Loc datasource;
    public LocationAttributes locAtt = new LocationAttributes();
    private AddActivity addObj = new AddActivity();
    private ListView itemsListView;
    private ArrayList<Item> itemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle data = getIntent().getExtras();
        attLoc = (LocationAttributes) data.getParcelable("locationObject");
        locAtt = attLoc;
        datasource = new DatabaseAccessAdapter4Loc(this);
        datasource.open();

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        txtaddress = (TextView) findViewById(R.id.Address);
        btnUpdate = (Button) findViewById(R.id.btnAdd);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        spinner = (Spinner) findViewById(R.id.spinner1);

        PathHolder = attLoc.getAlarmPath();
        File file=new File(PathHolder);
        String fileNameFrom=file.getName();

        btnUpdate.setText("Update");
        txtTitle.setText(attLoc.getTitle());
        txtDescription.setText(attLoc.getDescription());
        txtaddress.setText(attLoc.getAlarmLocation());
        radius = String.valueOf(attLoc.getRadius());
        addressLocation = attLoc.getAlarmLocation();
        latitude = attLoc.getLatitude();
        longitude = attLoc.getLongitude();

        if( radius.equals("100")) { spinner.setSelection(0);}
        else if(radius.equals("150")) { spinner.setSelection(1); }
        else if(radius.equals("200")) { spinner.setSelection(2); }
        else if(radius.equals("250")) { spinner.setSelection(3); }
        else { spinner.setSelection(4);}

        itemList.add(0, new Item("Alarm", fileNameFrom));
        CustomListAdapter adapter = new CustomListAdapter(this,itemList);
        // get the ListView and attach the adapter
        itemsListView  = (ListView) findViewById(R.id.listview);
        itemsListView.setAdapter(adapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create the ACTION_GET_CONTENT INTENT to open file explorer

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                startActivityForResult(intent, 7);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                radius = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button placeMarkerButton = (Button) findViewById(R.id.searchLocation);
        placeMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   /* //Location loc = GPS.getLastLocation(getApplicationContext());
                   location = getLocation();
                    //using google maps you will create a map setting lat & long.
                    String urlAddress = "http://maps.google.com/maps?q="+ latitude +"," + longitude +"("+ "hello" + ")&iwloc=A&hl=es";
                    //second option:: urlAddress = "http://maps.googleapis.com/maps/api/streetview?size=500x500&location=" + myLatitude + "," + myLongitude + "&fov=90&heading=235&pitch=10&sensor=false";
                    //third option:: urlAddress = "geo:<" + myLatitude + ">,<" + myLongitude +">?q=<" + latitude + ">,<" + longitude +">(this is my currently location)"
                    Intent maps = new Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress));
                    startActivity(maps);*/
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    //builder.setLatLngBounds();
                    startActivityForResult(builder.build(LocationEditActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    Log.e("Error: ", e.getMessage());
                } catch (GooglePlayServicesNotAvailableException e) {
                    Log.e("Error : ", e.getMessage());
                }

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txtTitle.getText().toString();
                description = txtDescription.getText().toString();

                if(title.matches("")||radius.matches("")||addressLocation.matches("")||PathHolder.matches("")){
                    Toast.makeText(LocationEditActivity.this, "You must be added the required field", Toast.LENGTH_SHORT).show();
                }
                else {
                    locAtt.setTitle(title);
                    locAtt.setDescription(description);
                    locAtt.setAlarmLocation(addressLocation);
                    locAtt.setLatitude(latitude);
                    locAtt.setLongitude(longitude);
                    locAtt.setRadius(Integer.parseInt(radius));
                    locAtt.setAlarmPath(PathHolder);
                    locAtt.setEnabled("true");
                }
                try {
                    Log.e("Path for Location LocAc", PathHolder);
                    locAtt = datasource.updateAttributes(locAtt, attLoc.getId());
                    Toast.makeText(LocationEditActivity.this, "Location Update Added!", Toast.LENGTH_SHORT).show();
                    Intent refresh = new Intent(LocationEditActivity.this, LocationDetailActivity.class);
                    refresh.putExtra("location_Update", locAtt);
                    refresh.putExtra("calling_activity", ActivityConstants.EDIT_ACTIVITY_2);
                    startActivity(refresh);
                    finish();
                } catch (DatabaseException e) {
                    Log.e("Database Exception ", e.getMessage());
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refresh = new Intent(LocationEditActivity.this, LocationDetailActivity.class);
                refresh.putExtra("location_Update", locAtt);
                refresh.putExtra("calling_activity", ActivityConstants.EDIT_ACTIVITY_2);
                startActivity(refresh);
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 7:
                Log.e("Build version ", " " + Build.VERSION.SDK_INT);
                String PathHolder1 = "";
                uri = data.getData();
                if (resultCode == RESULT_OK) {

                    try {
                        PathHolder1 = addObj.getPath(this, data.getData());

                    } catch (Exception e) {
                    }

                    File file = new File(PathHolder1);
                    String fileName1 = file.getName();
                    String extension = fileName1.substring(fileName1.lastIndexOf(".") + 1, fileName1.length());
                    Log.e("Path : ", PathHolder1);
                    //if(extension.equals("mp3")|| extension.equals("m4a") || extension.equals("ogg")||
                    //       extension.equals("wma")) {
                    PathHolder = PathHolder1;
                    fileName = fileName1;
                    itemList.set(0, new Item("Alarm", fileName));
                    itemsListView.setAdapter(new CustomListAdapter(LocationEditActivity.this, itemList));

                }
                break;
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                LatLng address = place.getLatLng();

                latitude = Double.toString(address.latitude);
                longitude = Double.toString(address.longitude);
                addressLocation = (String) place.getAddress();
                txtaddress.setText(addressLocation);

                Toast.makeText(this, "Address: " + latitude + "/" + longitude + "/" + addressLocation, Toast.LENGTH_SHORT).show();
                // ask for geolocation data
                Geocoder gcd = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses.size() > 0) {
                    String toastMsg1 = String.format("Place: %s", address + " - " + addresses.get(0).getCountryName() + " - " + addresses.get(0).getCountryCode());
                    Toast.makeText(this, toastMsg1, Toast.LENGTH_LONG).show();

                }
            }
        }
    }

    }

