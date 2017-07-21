package com.frobom.reminder;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Lenovo on 7/21/2017.
 */

public class GPS {

    private static final String TAG = "GPS";
    private static boolean pGps, pNetwork;
    private static LocationManager locManager;
    private static String provider;
    private static double longitude;
    private static double latitude;


    private static void updateAvailability(){
        try {
            pNetwork = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            provider = LocationManager.NETWORK_PROVIDER;
        } catch (Exception ex) {
            Log.w(TAG,"Ex getting NETWORK provider");
        }
        try {
            pGps = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            provider = LocationManager.GPS_PROVIDER;
        } catch (Exception ex) {
            Log.w(TAG,"Ex getting GPS provider");
        }
    }

    public static Location getLastLocation(Context ctx){
        Location loc = null;
        if(ctx != null){
            if(locManager == null){
               // locManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
                //Criteria criteria = new Criteria();
                //bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                locManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            }
            updateAvailability();
            if(provider!=null){
                try {

                    loc = locManager.getLastKnownLocation(provider);
                }
                catch (SecurityException e){

                }
            }
        }
        return loc;
    }


}
