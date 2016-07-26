package com.liwenquan.wakeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    private static final int NOTIFICATION_FLAG = 1;
    int yourChose = 1;
    PickerView hour_pv;
    PickerView minute_pv;
    String[] mList = {"1分钟", "5分钟", "10分钟", "30分钟"};
    private TextView mTvSnoozeTime;
    private Switch mNotificationSwitch;
    private NotificationManager manager;
    private Clock mclock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_setting);//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        setSupportActionBar(toolbar);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationSwitch = (Switch) findViewById(R.id.notification_switch);
        mNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    PendingIntent pendingIntent3 = PendingIntent.getActivity(SettingActivity.this, 0,
                            new Intent(SettingActivity.this, ClockListActivity.class), 0);
                    // 通过Notification.Builder来创建通知，注意API Level
                    // API16之后支持
                    //Calendar calendar = Calendar.getInstance();
                    //calendar.setTimeInMillis(System.currentTimeMillis());
                    Notification notify3 = new Notification.Builder(SettingActivity.this)
                            .setSmallIcon(R.drawable.message)
                            .setTicker("闹钟")
                            .setContentTitle("WakeUp闹钟")
                            .setContentText("Hello~")
                            .setContentIntent(pendingIntent3).setNumber(1).build(); // 需要注意build()是在API
                    // level16及之后增加的，API11可以使用getNotificatin()来替代
                    notify3.flags |= Notification.FLAG_AUTO_CANCEL; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
                    manager.notify(NOTIFICATION_FLAG, notify3);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        hour_pv = (PickerView) findViewById(R.id.hour_pv);
//        minute_pv = (PickerView) findViewById(R.id.minute_pv);
//        List<String> data = new ArrayList<String>();
//        List<String> seconds = new ArrayList<String>();
//        for (int i = 0; i < 24; i++)
//        {
//            data.add(i < 10 ? "0" + i : "" + i);
//
//        }
//        for (int i = 0; i < 60; i++)
//        {
//            seconds.add(i < 10 ? "0" + i : "" + i);
//        }
//        hour_pv.setData(data);
//
//
//        hour_pv.setOnSelectListener(new PickerView.onSelectListener()
//        {
//
//            @Override
//            public void onSelect(String text)
//            {
//                Toast.makeText(SettingActivity.this, "选择了 " + text + " 时",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        hour_pv.mCurrentSelected=calendar.get(Calendar.HOUR_OF_DAY);
//        minute_pv.setData(seconds);
//        minute_pv.setOnSelectListener(new PickerView.onSelectListener()
//        {
//
//            @Override
//            public void onSelect(String text)
//            {
//                Toast.makeText(SettingActivity.this, "选择了 " + text + " 分",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//        minute_pv.mCurrentSelected=calendar.get(Calendar.MINUTE);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("我是睡眠时间", mList[yourChose]);
    }

//    private void showSinChosDia() {
//
//        yourChose = 1;
//        AlertDialog.Builder singleChoiseDialog = new AlertDialog.Builder(SettingActivity.this);
//        singleChoiseDialog.setTitle("小睡时间");
//
//        singleChoiseDialog.setSingleChoiceItems(mList, 1, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                yourChose = which;
//            }
//        });
//        singleChoiseDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                mTvSnoozeTime = (TextView) findViewById(R.id.tvSnoozeTime);
//                mTvSnoozeTime.setText(mList[yourChose]);
//
//            }
//        });
//        singleChoiseDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // TODO Auto-generated method stub
//                showClickMessage(mList[yourChose]);
//                mTvSnoozeTime = (TextView) findViewById(R.id.tvSnoozeTime);
//                mTvSnoozeTime.setText(mList[yourChose]);
//            }
//        });
//        singleChoiseDialog.show();
//
//    }
//
//    /*显示点击的内容*/
//    private void showClickMessage(String message) {
//        Toast.makeText(SettingActivity.this, "小睡时间设定为：" + message, Toast.LENGTH_SHORT).show();
//    }


}
