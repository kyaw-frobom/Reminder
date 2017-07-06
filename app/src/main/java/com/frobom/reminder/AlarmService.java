package com.frobom.reminder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import java.io.IOException;

public class AlarmService extends Service {

    CountDownTimer cTimer = null;

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        startTimer();
        return super.onStartCommand(intent, flags, startId);
    }

    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish()
            {
                Intent intent = new Intent(AlarmService.this, alarm.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.redzonefullmix);
                mediaPlayer.setVolume(50,50);


                /*mediaPlayer = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Music/intro.mp3"));
                mediaPlayer.setLooping(true);
                mediaPlayer.start();*/

                mediaPlayer.setScreenOnWhilePlaying(true);
                //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
                mediaPlayer.start();
                mediaPlayer.setLooping(true);

                cancelTimer();
            }
        };
        cTimer.start();
    }

    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();

    }

    /*public AlarmService() {
        super();
    }*/
}
