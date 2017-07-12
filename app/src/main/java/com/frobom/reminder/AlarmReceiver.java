package com.frobom.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ソーイエーリン on 10-Jul-17.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    int DD;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Bundle b = intent.getExtras();
        DD = b.getInt("id");

        Intent Alarm = new Intent(context,AlarmService.class);
        Bundle c = new Bundle();
        c.putInt("id",DD);
        Alarm.putExtras(c);
        context.startService(Alarm);
    }
}
