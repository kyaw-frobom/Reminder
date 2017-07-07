package com.frobom.reminder;
 
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class alarm extends AppCompatActivity {
 
    //MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        /*mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.redzonefullmix);

        mediaPlayer.setScreenOnWhilePlaying(true);
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);*/

        //Remove title bar
                //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Window window = this.getWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        window.addFlags(LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(LayoutParams.FLAG_TURN_SCREEN_ON);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_alarm);

        closeActivity();

    }

    private void closeActivity()
    {

        final Button btnOK = (Button)findViewById(R.id.buttonOk);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent alarm = new Intent(alarm.this, MainActivity.class);
                //Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                //finishAffinity();
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
                super.onPause();
                //mediaPlayer.stop();
                //mediaPlayer.release();
                Intent intent = new Intent(alarm.this, AlarmService.class);
                alarm.this.stopService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if (mediaPlayer != null) mediaPlayer.release();

        Intent intent = new Intent(alarm.this, AlarmService.class);
        alarm.this.stopService(intent);
    }

}