package com.liwenquan.sl;

/**
 * Created by LWQ on 2016/4/10.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/*
*@author: ZhengHaibo
*web: http://blog.csdn.net/nuptboyzhb
*mail: zhb931706659@126.com
*2013-6-7 Nanjing,njupt,China
*/
public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent StartIntent = new Intent(context, ClockListActivity.class); //接收到广播后，跳转到MainActivity
            StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(StartIntent);
        }

    }

}