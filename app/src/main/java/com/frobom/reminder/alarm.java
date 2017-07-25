package com.frobom.reminder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v13.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class alarm extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public DatabaseAccessAdapter datasource;
    private int trigger = 0;
    private int mediatrigger = 0;

    MediaPlayer mediaPlayer;

    public String path;

    public TextView Title;
    public TextView Clock;
    public TextView Content;

    boolean doubleBackToExitPressedOnce = false;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        trigger=0;
        mediatrigger=0;

        permission_relay();

        setData();

        if(mediatrigger==0)
        {
            mediaPlay();
        }

        closeActivity();
    }

    public void permission_relay()
    {
        verifyStoragePermissions(alarm.this);
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

    private void setData()
    {

        setScreen();

        Title = (TextView)findViewById(R.id.titleAlarm);
        Clock = (TextView)findViewById(R.id.clockAlarm);
        Content = (TextView)findViewById(R.id.contentAlarm);

        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateF.format(calendar.getTime());

        SimpleDateFormat timeF = new SimpleDateFormat("kk:mm");
        String formattedTime = timeF.format(calendar.getTime());

        String[] separatedTimeN = formattedTime.split(":");
        int TimeN = Integer.parseInt(separatedTimeN[0]+separatedTimeN[1]);


        if(values.size()>0) {
            for (int i = 0; i < values.size(); i++)
            {

                String[] separatedTimeS = values.get(i).getAlarmTime().split(" ");
                String separatedTimeSA = separatedTimeS[0];
                String[] separatedTimeSR = separatedTimeSA.split(":");
                int TimeS = Integer.parseInt(separatedTimeSR[0]+separatedTimeSR[1]);
                String AA = separatedTimeS[1];
                String PM = "PM";

                if(PM.equals(AA) && 100<=TimeS && TimeS>=1200)
                {
                    trigger= 1 ;
                }

                if (PM.equals(AA)&& TimeS>=100 && trigger==0)
                {
                    TimeS += 1200;
                }

                if( formattedDate.equals(values.get(i).getAlarmDate()) && TimeN == TimeS)
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
        else if (values.size()==0)
        {
            mediatrigger=1;
        }
        datasource.close();
    }

    private void setScreen()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        Window window = this.getWindow();
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setType(LayoutParams.TYPE_SYSTEM_ALERT);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_alarm);
    }
/*
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(LayoutParams.TYPE_KEYGUARD_DIALOG);
    }
*/
    @Override
    protected void onResume()
    {
        super.onResume();
        //startActivity(new Intent (this,alarm.class));
    }

    private void closeActivity()
    {

        final Button btnOK = (Button)findViewById(R.id.buttonOk);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigger=1;
                if(mediaPlayer != null)
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
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

        //finish();
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

    @Override
    public void onBackPressed() {if (doubleBackToExitPressedOnce) {
        super.onBackPressed();
        return;
    }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}