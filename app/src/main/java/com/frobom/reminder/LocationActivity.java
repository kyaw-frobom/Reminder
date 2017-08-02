package com.frobom.reminder;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseException;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity  {

    //public double latitude;
   // public double longitude;

    private TextView txtTitle;
    private TextView txtDescription;
    private TextView txtRadius;
    private Button btnAdd;
    private Button btnCancel;
    private TextView txtaddress;
    int PLACE_PICKER_REQUEST = 1;

    private String title;
    private String description;
    private String addressLocation;
    private String radius;
    private String latitude;
    private String longitude;
    String voice2text; //added

    public DatabaseAccessAdapter4Loc datasource;
    public LocationAttributes locAtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtDescription = (TextView)findViewById(R.id.txtDescription);
        txtRadius = (TextView)findViewById(R.id.radius);
        txtaddress = (TextView)findViewById(R.id.Address);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnCancel = (Button)findViewById(R.id.btnCancel);

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
                        startActivityForResult(builder.build(LocationActivity.this), PLACE_PICKER_REQUEST);
                    }
                    catch (GooglePlayServicesRepairableException e){
                        Log.e("Error: ", e.getMessage());
                    }
                    catch (GooglePlayServicesNotAvailableException e){
                        Log.e("Error : ", e.getMessage());
                    }

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = txtTitle.getText().toString();
                description = txtDescription.getText().toString();
                radius = txtRadius.getText().toString();

                locAtt = new LocationAttributes();
                locAtt.setTitle(title);
                locAtt.setDescription(description);
                locAtt.setAlarmLocation(addressLocation);
                locAtt.setLatitude(latitude);
                locAtt.setLongitude(longitude);
                locAtt.setEnabled("true");

                try{
                    locAtt = datasource.createAttributes(locAtt);
                    Toast.makeText(LocationActivity.this, "Location Data Added!", Toast.LENGTH_SHORT).show();
                }
                catch (DatabaseException e){
                    Log.e("Database Exception ", e.getMessage());
                }
            }
        });

    }
   /* public static boolean isLocationEnabled(Context context)
    {
        //...............
        return true;
    }

    public Location getLocation() {
        if (isLocationEnabled(LocationActivity.this)) {
            locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            try {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    Log.e("TAG", "GPS is on");
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(LocationActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
                    searchNearestPlace(voice2text);
                } else {
                    //This is what you need:
                    locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
                }
            }
            catch (SecurityException e){}
        }
        else
        {
            //prompt user to enable location....
            //.................
        }
        return location;
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Toast.makeText(LocationActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
        searchNearestPlace(voice2text);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void searchNearestPlace(String v2txt) {
        //.....
    }
*/
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == PLACE_PICKER_REQUEST) {
           if (resultCode == RESULT_OK) {
               Place place = PlacePicker.getPlace(this, data);
                LatLng address = place.getLatLng();

               latitude = Double.toString(address.latitude);
               longitude = Double.toString(address.longitude);
               addressLocation = (String) place.getAddress();
                txtaddress.setText(addressLocation);
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


                   // NOW SET HERE CORRECT DATA
                   //location.setText(place.getName());

               }
           }
       }
   }
}
