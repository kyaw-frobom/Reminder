package com.frobom.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ソーイエーリン on 10-Jul-17.
 */

public class AlarmReceiver extends BroadcastReceiver
{
    int DD = 0;

    @Override
    public void onReceive(Context context, Intent intent)
    {

        DD = 0;
        Bundle b = new Bundle();
        b.clear();
        b = intent.getExtras();
        DD = b.getInt("id");
        b.clear();

        Log.d("AlarmReceiver", String.valueOf(DD));

        Intent Alarm = new Intent(context,AlarmService.class);
        Bundle d = new Bundle();
        d.putInt("id1",DD);
        Alarm.putExtras(d);
        context.startService(Alarm);
        d.clear();
    }
}
