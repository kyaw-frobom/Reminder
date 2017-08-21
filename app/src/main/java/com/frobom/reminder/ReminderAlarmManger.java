package com.frobom.reminder;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.AlarmManager;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

public class ReminderAlarmManger extends Service {

    private static final long NEVER_EXPIRE = -1;
    private static final int PERMISSION_REQUEST_CODE = 1;
    public DatabaseAccessAdapter datasource;
    public DatabaseAccessAdapter4Loc datasourceLoc;
    private AlarmManager Alarm_manager;
    public PendingIntent alarmIntent;
    private int Aid;
    private int hr;
    private int min;
    private int AlarmSetTrigger = 0;

    private long id;
    private int transitionType;
    private float radius;
    private double latitude;
    private double longitude;
    private long expirationDuration;

    public PendingIntent mGeofencePendingIntent;
    public GeofencingClient mGeofencingClient;
    public List<Geofence> mGeofenceList = new ArrayList<Geofence>();

    public Activity activity;
    public Context context;

    public int trigger = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        timeBased();

        locationBased();
    }

    private void locationBased()
    {
        Log.e("Locationbased:","Started!!");
        datasourceLoc = new DatabaseAccessAdapter4Loc(this);
        datasourceLoc.open();

        List<LocationAttributes> valuesLoc = datasourceLoc.getAllAttributes();

        if (valuesLoc.size() > 0) {
            for (int d = 0; d < valuesLoc.size(); d++) {
                // Build a new Geofence object
                id = valuesLoc.get(d).getId();
                transitionType = Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT;
                radius = valuesLoc.get(d).getRadius();
                latitude = Double.parseDouble(valuesLoc.get(d).getLatitude());
                longitude = Double.parseDouble(valuesLoc.get(d).getLongitude());
                toGeofence();
            }
            mGeofencingClient = new GeofencingClient(this);
            try{
            mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener((Executor) this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                        Log.e("Geofence adding :","Success!");
                        callRemove();
                    }
                })
                .addOnFailureListener((Executor) this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                        Log.e("Geofence adding :","Fail!");
                    }
                });
            }
            catch(Exception Error)
            {
                Error.getMessage();
                Log.e("Error!","pending failed!");
                callRemove();
            }
    }
    datasourceLoc.close();
    }

    public void toGeofence()
    {
                mGeofenceList.add(new Geofence.Builder()
                        .setRequestId(Long.toString(id))

                        .setCircularRegion(latitude, longitude, radius)
                        .setExpirationDuration(NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build());

                        /*
                        .setRequestId(Long.toString(id))
                        .setTransitionTypes(transitionType)
                        .setCircularRegion(latitude, longitude, radius)
                        .setExpirationDuration(expirationDuration)
                        .setLoiteringDelay(3000)
                        .build());
                        */
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, ReceiveTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    private void callRemove()
    {
        try {
            mGeofencingClient = new GeofencingClient(this);
            mGeofencingClient.removeGeofences(getGeofencePendingIntent())
                    .addOnSuccessListener((Executor) this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Geofences removed
                            // ...
                            Log.e("GeoRemove", "Geofences are removed!");
                        }
                    })
                    .addOnFailureListener((Executor) this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to remove geofences
                            // ...
                            Log.e("GeoRemove", "Geofences are failed to remove!");
                        }
                    });
        }
        catch (Exception Error)
        {
            Error.getMessage();
            Log.e("Error!", "pending failed!");
        }
    }


    //--------------------------------------------------------------------------------------------------------------------


    private void timeBased()
    {
        trigger = 0;

        if (Alarm_manager!= null)
        {
            Alarm_manager.cancel(alarmIntent);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateF.format(calendar.getTime());

        SimpleDateFormat timeF = new SimpleDateFormat("kk:mm");
        String formattedTime = timeF.format(calendar.getTime());

        String[] separatedTimeN = formattedTime.split(":");
        int TimeN = Integer.parseInt(separatedTimeN[0]+separatedTimeN[1]);


        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();


        if(values.size()>0)
        {
            for (int i = 0; i < values.size(); i++)
            {

                String[] separatedTimeS = values.get(i).getAlarmTime().split(" ");
                String separatedTimeSA = separatedTimeS[0];
                String[] separatedTimeSR = separatedTimeSA.split(":");
                int TimeS = Integer.parseInt(separatedTimeSR[0]+separatedTimeSR[1]);
                String AA = separatedTimeS[1];
                String PM = "PM";

                if(PM.equals(AA) && 100<=TimeS && TimeS>=1200)
                {
                    trigger= 1 ;
                }

                if (PM.equals(AA)&& TimeS>=100 && trigger==0)
                {
                    TimeS += 1200;
                }

                if ( formattedDate.equals(values.get(i).getAlarmDate()) && TimeN < TimeS)
                {
                    if (PM.equals(separatedTimeS[1]) && TimeS>=100 && trigger == 0)
                    {
                        hr = Integer.parseInt(separatedTimeSR[0]) + 12;
                    } else
                    {
                        hr = Integer.parseInt(separatedTimeSR[0]);
                    }

                    min = Integer.parseInt(separatedTimeSR[1]);

                    Aid = values.get(i).getId();

                    AlarmSetTrigger = 1;
                    trigger=0;
                    break;
                }
            }
        }


        if(AlarmSetTrigger==1)
        {
            Alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, hr);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);


            Alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            AlarmSetTrigger=0;
        }

        datasource.close();
    }
}
