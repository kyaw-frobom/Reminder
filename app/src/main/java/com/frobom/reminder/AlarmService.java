package com.frobom.reminder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class AlarmService extends Service
{

    MediaPlayer mediaPlayer;
    int DD;

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
        startAlarm(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    void startAlarm(Intent ID)
    {
        Bundle a = ID.getExtras();
        DD = a.getInt("id");

        Intent intent = new Intent(AlarmService.this, alarm.class);
        Bundle b = new Bundle();
        b.putInt("id", DD);
        intent.putExtras(b);


        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Music/intro.mp3"));
                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.redzonefullmix);
                mediaPlayer.setVolume(50,50);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();


                //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

    public AlarmService()
    {
        super();
    }
}
