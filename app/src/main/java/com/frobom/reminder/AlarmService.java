package com.frobom.reminder;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
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

        //String filePath = Environment.getExternalStorageDirectory()+path;
        //mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.redzonefullmix);

        // mediaPlayer = MediaPlayer.create(AlarmService.this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ path));
        //mediaPlayer.setDataSource(filePath);

        try{
            Log.e("Path of alarm ", path);
           // mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
            mediaPlayer = new MediaPlayer();
           // mediaPlayer.setDataSource("file://"+path);
            File file = new File(path);
            FileInputStream inputStream = new FileInputStream(file);
            mediaPlayer.setDataSource(inputStream.getFD());

            // mediaPlayer.prepare();;
            inputStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioStreamType(AudioTrack.MODE_STREAM);

        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        if(mediaPlayer != null && mediaPlayer.isPlaying() != true)
        {
            mediaPlayer.setVolume(50, 50);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mp.release();

                };
            });

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
       // mediaPlayer.stop();
        mediaPlayer.release();
        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

    public AlarmService()
    {
        super();
    }
}
