package com.frobom.reminder;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class AlarmService extends Service{
    int DD = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // do your jobs here
        DD=0;
        startAlarm(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    void startAlarm(Intent ID)
    {
        Bundle a = new Bundle();
        a.clear();
        a = ID.getExtras();
        DD = a.getInt("id1");
        a.clear();

        Log.d("AlarmService", String.valueOf(DD));

        Intent intent = new Intent(AlarmService.this, alarm.class);
        Bundle e = new Bundle();
        e.putInt("id2", DD);
        intent.putExtras(e);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        e.clear();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

    public AlarmService()
    {
        super();
    }
}
