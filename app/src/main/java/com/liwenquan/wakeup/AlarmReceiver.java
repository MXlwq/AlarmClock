package com.liwenquan.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by LWQ on 2016/4/11.
 */
public class AlarmReceiver extends BroadcastReceiver {
    public static final String PLAY_ALARM = "com.liwenquan.wakeup.playalarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(PLAY_ALARM);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
