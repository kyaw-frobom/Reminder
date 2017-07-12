package com.frobom.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ソーイエーリン on 12-Jul-17.
 */

public class RefreshReciver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.stopService(new Intent(context, ReminderAlarmManger.class));
        context.startService(new Intent(context, ReminderAlarmManger.class));
    }
}
