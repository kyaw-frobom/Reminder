package com.frobom.reminder;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationProvider;

import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class ReceiveTransitionsIntentService extends IntentService{

    public ReceiveTransitionsIntentService()
    {
        super("ReceiveTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

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
            }
        }
    }
}
