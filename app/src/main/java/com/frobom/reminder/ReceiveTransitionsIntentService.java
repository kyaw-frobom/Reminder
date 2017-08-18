package com.frobom.reminder;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ReceiveTransitionsIntentService extends IntentService{

    public DatabaseAccessAdapter4Loc datasourceLoc;
    float now = (float)1000000000;
    int idget;

    public ReceiveTransitionsIntentService()
    {
        super("ReceiveTransitionsIntentService");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Log.e("Track Service","Track Service started!");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        Intent Alarm = new Intent(this, alarm.class);

        datasourceLoc = new DatabaseAccessAdapter4Loc(this);
        datasourceLoc.open();

        List<LocationAttributes> valuesLoc = datasourceLoc.getAllAttributes();

        int transition = geofencingEvent.getGeofenceTransition();
        if ((transition == Geofence.GEOFENCE_TRANSITION_ENTER) || (transition == Geofence.GEOFENCE_TRANSITION_EXIT))
        {
            if (transition == Geofence.GEOFENCE_TRANSITION_ENTER)
            {/*
                AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVice);
                Intent broadcast_intent = new Intent(this, GeofencigNotificationReciever.class);
                broadcast_intent.putExtra("EventId", eventId);
                //data to pass
                broadcast_intent.putExtra("TransitionType", transitionType);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(this, 0, broadcast_intent, 0);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);*/

                double curlacla = geofencingEvent.getTriggeringLocation().getLatitude();
                double curlaclo = geofencingEvent.getTriggeringLocation().getLongitude();


                Location curlocal = new Location("current_location");
                curlocal.setLatitude(curlacla);
                curlocal.setLongitude(curlaclo);

                for (int i = 0; i < valuesLoc.size(); i++)
                {
                    Location deslocal = new Location("target_location");
                    double terla = Double.parseDouble(valuesLoc.get(i).getLatitude());
                    double terlo = Double.parseDouble(valuesLoc.get(i).getLongitude());
                    deslocal.setLatitude(terla);
                    deslocal.setLongitude(terlo);
                    float v = curlocal.distanceTo(deslocal);

                    if (now > v) {
                        now = v;
                        idget = valuesLoc.get(i).getId();
                    }
                }
            }
            Log.e("GeoF","Entered to the area!");
            Alarm.putExtra("id",idget);
            startActivity(Alarm);

        if (transition == Geofence.GEOFENCE_TRANSITION_EXIT)
        {
                Log.e("GeoF","Exited the area!");
        }
        }
    }
}
