package com.frobom.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import java.util.Calendar;

public class RefreshManager extends Service
{

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public RefreshManager()
    {

        Intent intent = new Intent(this, RefreshReciver.class);
        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


        alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the alarm to start at approximately 2:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 24);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
