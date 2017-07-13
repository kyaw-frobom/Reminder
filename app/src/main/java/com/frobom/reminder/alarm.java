package com.frobom.reminder;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class alarm extends AppCompatActivity {

    public DatabaseAccessAdapter datasource;
    private int idgot;

    public TextView Title;
    public TextView Clock;
    public TextView Date;
    public TextView Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        Bundle b = this.getIntent().getExtras();
        int id = b.getInt("id");

        setData(id);

        closeActivity();
    }

    private void setData(int idGet)
    {

        setScreen();

        idgot=idGet;
        Title = (TextView)findViewById(R.id.titleAlarm);
        Clock = (TextView)findViewById(R.id.clockAlarm);
        Date = (TextView)findViewById(R.id.dateAlarm);
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
                    String date = values.get(i).getAlarmDate();
                    String description = values.get(i).getDescription();

                    Title.setText(title);
                    Clock.setText(time);
                    Date.setText(date);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(alarm.this, AlarmService.class);
        alarm.this.stopService(intent);

        stopService(new Intent(this, ReminderAlarmManger.class));
        startService(new Intent(this, ReminderAlarmManger.class));
    }

}