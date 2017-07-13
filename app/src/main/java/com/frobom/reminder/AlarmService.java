package com.frobom.reminder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.util.List;

public class AlarmService extends Service
{

    MediaPlayer mediaPlayer;
    int DD;
    int idgot;
    String path="";
    public DatabaseAccessAdapter datasource;

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

        setData(DD);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        mediaPlayer = new MediaPlayer();

        //String filePath = Environment.getExternalStorageDirectory()+path;
        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.redzonefullmix);


        mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ path));
        //mediaPlayer.setDataSource(filePath);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mediaPlayer.setVolume(50,50);
                mediaPlayer.setScreenOnWhilePlaying(true);
                mediaPlayer.setLooping(true);

                mediaPlayer.start();
            }
        }, 10000);

    }

    public void setData(int idGet)
    {
        idgot=idGet;

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();


        if(values.size()>0) {
            for (int i = 0; i < values.size(); i++)
            {
                if(idgot == values.get(i).getId())
                {
                    path = values.get(i).getAlarmPath();
                    break;
                }
            }
        }

        datasource.close();
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
