package com.frobom.reminder;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.zip.Inflater;

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
        a.remove("id1");
        a = ID.getExtras();
        DD = a.getInt("id1");
        a.clear();

        Log.d("AlarmService", String.valueOf(DD));

        Intent intent = new Intent(AlarmService.this, alarm.class);
        Bundle e = new Bundle();
        e.remove("id2");
        e.putInt("id2", DD);
        intent.putExtras(e);

        lockedscreen();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        e.clear();
    }

    private void lockedscreen()
    {
        LayoutInflater minflater = (LayoutInflater)this.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        WindowManager mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        View mView = minflater.inflate(R.layout.activity_alarm, null);

        WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0, 0,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
/* | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON */,
                PixelFormat.RGBA_8888);

        mWindowManager.addView(mView, mLayoutParams);
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
