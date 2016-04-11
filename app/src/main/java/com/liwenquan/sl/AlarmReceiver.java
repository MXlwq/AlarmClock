package com.liwenquan.sl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by LWQ on 2016/4/11.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String PALY_ALARM = "com.liwenquan.sl.playalarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        //System.out.println("闹钟执行了"+AddAlarmActivity.AlarmID);
//        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pi=PendingIntent.getBroadcast(context,AddAlarmActivity.AlarmID,new Intent(context,AlarmReceiver.class),0);
//        am.cancel(pi);
        Intent i = new Intent(PALY_ALARM);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
