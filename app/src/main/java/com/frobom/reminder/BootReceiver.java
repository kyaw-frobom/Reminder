package com.frobom.reminder;


import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            context.startService(new Intent(context,AlarmManager.class));
            context.startService(new Intent(context,RefreshManager.class));
        }
    }
}
