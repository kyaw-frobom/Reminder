package com.frobom.reminder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class alarm extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public DatabaseAccessAdapter datasource;
    private int idgot =0;
    private int trigger = 0;
    private int id=0;

    private static final String tag = "alarm";

    MediaPlayer mediaPlayer;

    public String path;

    public TextView Title;
    public TextView Clock;
    public TextView Content;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        trigger=0;
        idgot=0;
        id=0;

        Bundle f = new Bundle();
        f.clear();
        f = this.getIntent().getExtras();
        id = f.getInt("id2");
        f.clear();

        Log.d(tag, String.valueOf(id));

        verifyStoragePermissions(alarm.this);

        setData(id);

        mediaPlay();

        closeActivity();
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private void mediaPlay()
    {
        mediaPlayer = new MediaPlayer();
        AudioManager audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        try
        {
            mediaPlayer.setDataSource(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        mediaPlayer.setAudioStreamType(AudioTrack.MODE_STREAM);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setVolume(50,50);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(alarm.this);
        mediaPlayer.prepareAsync();
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }

    private void setData(int idGet)
    {

        setScreen();

        idgot=idGet;
        Title = (TextView)findViewById(R.id.titleAlarm);
        Clock = (TextView)findViewById(R.id.clockAlarm);
        Content = (TextView)findViewById(R.id.contentAlarm);

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();


        if(values.size()>0) {
            for (int i = 0; i < values.size(); i++)
            {
                if(idgot == values.get(i).getId())
                {
                    String title = values.get(i).getTitle();
                    String time = values.get(i).getAlarmTime();
                    String description = values.get(i).getDescription();
                    path = values.get(i).getAlarmPath();

                    Title.setText(title);
                    Clock.setText(time);
                    Content.setText(description);

                    break;
                }
            }
        }

        datasource.close();
    }

    private void setScreen()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Window window = this.getWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_alarm);

    }


    private void closeActivity()
    {

        final Button btnOK = (Button)findViewById(R.id.buttonOk);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger=1;
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        Intent intent = new Intent(alarm.this, AlarmService.class);
        alarm.this.stopService(intent);

        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mediaPlayer != null && trigger==0)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent intent = new Intent(alarm.this, AlarmService.class);
        alarm.this.stopService(intent);

        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

}