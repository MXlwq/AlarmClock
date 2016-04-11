package com.liwenquan.sl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by LWQ on 2016/4/11.
 */
public class ClockReceiver extends BroadcastReceiver {
    public final static String ACTION_SEND = "com.liwenquan.sl.ACTION_SEND";

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_SEND.equals(action)) {
            Log.i("SendReceiver", "send a message");
        }
    }
}