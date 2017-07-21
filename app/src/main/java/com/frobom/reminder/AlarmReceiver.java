package com.frobom.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ソーイエーリン on 10-Jul-17.
 */

public class AlarmReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent Alarm = new Intent(context,AlarmService.class);
        context.startService(Alarm);
    }
}
