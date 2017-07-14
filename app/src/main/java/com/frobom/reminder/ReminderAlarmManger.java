package com.frobom.reminder;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.AlarmManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ReminderAlarmManger extends Service
{

    public DatabaseAccessAdapter datasource;
    private AlarmManager Alarm_manager;
    private PendingIntent alarmIntent;
    private int Aid;
    private int hr;
    private int min;
    private int AlarmSetTrigger = 0;

    public int trigger=0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {

        trigger = 0;

        if (Alarm_manager!= null)
        {
            Alarm_manager.cancel(alarmIntent);
        }


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateF.format(calendar.getTime());

        SimpleDateFormat timeF = new SimpleDateFormat("kk:mm");
        String formattedTime = timeF.format(calendar.getTime());

        String[] separatedTimeN = formattedTime.split(":");
        int TimeN = Integer.parseInt(separatedTimeN[0]+separatedTimeN[1]);


        datasource = new DatabaseAccessAdapter(this);
        datasource.open();

        List<Attributes> values = datasource.getAllAttributes();


        if(values.size()>0)
        {
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

                if ( formattedDate.equals(values.get(i).getAlarmDate()) && TimeN < TimeS)
                {
                    if (PM.equals(separatedTimeS[1]) && TimeS>=100 && trigger == 0)
                    {
                        hr = Integer.parseInt(separatedTimeSR[0]) + 12;
                    } else
                    {
                        hr = Integer.parseInt(separatedTimeSR[0]);
                    }

                    min = Integer.parseInt(separatedTimeSR[1]);

                    Aid = values.get(i).getId();

                    AlarmSetTrigger = 1;
                    trigger=0;
                    break;
                }
            }
        }


        if(AlarmSetTrigger==1)
        {
            Alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            Bundle c = new Bundle();
            c.remove("id");
            c.putInt("id",Aid);

            Log.d("ReminderManager", String.valueOf(Aid));

            intent.putExtras(c);
            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);


            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR));
            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE));
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, hr);
            calendar.set(Calendar.MINUTE, min);
            calendar.set(Calendar.SECOND, 0);


            Alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

            AlarmSetTrigger=0;
        }

        datasource.close();
    }
}
