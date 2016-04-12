package com.liwenquan.sl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddAlarmActivity extends AppCompatActivity {
    //public static final String ACTION_SEND = "com.liwenquan.sl.ACTION_SEND";
    public static final String PALY_ALARM = "com.liwenquan.sl.playalarm";
    public static final long INTERVAL_TIME = 1000 * 60 * 60 * 24;
    private static final String TAG = "AddAlarmTAG";
    private TextView mtvalarmlable;
    private AudioManager audiomanger;
    private int maxVolume, currentVolume;
    private SeekBar seekBar;
    private Calendar calendar;
    public static AlarmManager alarmManager;
    private Clock mclock;
    public static PendingIntent pi;
    private TextView mRingName;
    private int yourChose;
    public static int AlarmID;
    Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_add_clock);//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.linearadd).setVisibility(View.VISIBLE);
        findViewById(R.id.linearset).setVisibility(View.GONE);

        mRingName = (TextView) findViewById(R.id.ring_name);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Calendar c = Calendar.getInstance();
        mclock = new Clock(Calendar.getInstance().getTime());

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setIs24HourView(true);//24小时制
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                mclock = new Clock(calendar.getTime());
            }
        });
        findViewById(R.id.how_to_shutdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinChosDia();
            }
        });
        findViewById(R.id.chooseSong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹玲铃声");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
                Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(AddAlarmActivity.this, RingtoneManager.TYPE_ALARM);
                if (pickedUri != null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, pickedUri);
                    ringUri = pickedUri;
                }
                startActivityForResult(intent, 1);
            }
        });
        findViewById(R.id.lllable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAlarmActivity.this);
                builder.setTitle("标签");
                //    通过LayoutInflater来加载一个xml的布局文件作为一个View对象
                View view = LayoutInflater.from(AddAlarmActivity.this).inflate(R.layout.dialog_lable, null);
                //    设置我们自己定义的布局文件作为弹出框的Content
                builder.setView(view);

                final EditText metLable = (EditText) view.findViewById(R.id.etLable);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String lable = metLable.getText().toString().trim();
                        mtvalarmlable = (TextView) findViewById(R.id.tvalarmlable);
                        mtvalarmlable.setText(lable);
                        mclock.setLable(lable);
                        Toast.makeText(AddAlarmActivity.this, "已设定标签：" + lable, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //deleteAlarm();
                finish();
            }
        });
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"闹钟的ID:"+mclock.getId(),Toast.LENGTH_SHORT).show();
                Log.e(TAG, mclock.getId().hashCode() + "");
                //i = new Intent(AddAlarmActivity.this, PlayAlarmActivity.class);
                Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
                AlarmID = mclock.getId().hashCode();
                pi = PendingIntent.getBroadcast(getApplicationContext(), AlarmID, i, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                //设置每天重复闹钟
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pi);
                //设置精准闹钟
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                        calendar.getTimeInMillis(), pi);
                //Toast.makeText(getApplicationContext(), "闹钟ID" + mclock.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddAlarmActivity.this, ClockListActivity.class);
                startActivity(intent);
                ClockLab.get(getApplicationContext()).addClock(mclock);
                if (mclock.getLable() == null) {
                    mclock.setLable("闹钟");
                }
                mclock.setOn(true);
                ClockLab.get(getApplicationContext()).saveClocks();
                AddAlarmActivity.this.finish();

            }
        });

        audiomanger = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanger.getStreamMaxVolume(AudioManager.STREAM_ALARM);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setMax(maxVolume);
        seekBar.setProgress(currentVolume);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audiomanger.setStreamVolume(AudioManager.STREAM_ALARM, progress, 0);
                currentVolume = audiomanger.getStreamVolume(AudioManager.STREAM_ALARM);
                seekBar.setProgress(currentVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

//    private void deleteAlarm() {
//        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
//        pi = PendingIntent.getBroadcast(getApplicationContext(), AlarmID, i, 0);
//        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.cancel(pi);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                //获取选中的铃声的URI
                //当外部应用需要对ContentProvider中的数据进行添加、删除、修改和查询操作
                /*
                query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) 用于查询指定Uri的ContentProvider
                 */
                Uri pickedURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
//                Cursor cursor = this.getContentResolver().query(pickedURI, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
                ContentResolver musicResolver = this.getContentResolver();
                Cursor musicCursor = musicResolver.query(
                        pickedURI, null, null, null,
                        null);
                int musicColumnIndex;
                if (null != musicCursor && musicCursor.getCount() > 0) {
                    for (musicCursor.moveToFirst(); !musicCursor.isAfterLast(); musicCursor
                            .moveToNext()) {
                        Map musicDataMap = new HashMap();
                        Random random = new Random();
                        int musicRating = Math.abs(random.nextInt()) % 10;
                        musicDataMap.put("musicRating", musicRating);// 取得音乐播放路径
                        musicColumnIndex = musicCursor
                                .getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
                        String musicPath = musicCursor.getString(musicColumnIndex);
                        musicDataMap.put("musicPath", musicPath);// 取得音乐的名字
                        musicColumnIndex = musicCursor
                                .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
                        String musicName = musicCursor.getString(musicColumnIndex);
                        musicDataMap.put("musicName", musicName);
                        mRingName.setText(musicName);
                        mclock.setRing(musicName);
                    }
                }
//                if (cursor != null) {
//                    if (cursor.moveToFirst()) {
//                        String ring_name = cursor.getString(0);
//                        mRingName.setText(ring_name);
//                        mclock.setRing(ring_name);
////                        String[] c = cursor.getColumnNames();
////                        for (String string : c) {
////
////                        }
//                    }
//                    cursor.close();
//                }
                break;

            default:
                break;
        }
    }

    private void getName(int type) {
        Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this, type);
        Cursor cursor = this.getContentResolver().query(pickedUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String ring_name = cursor.getString(0);
                mRingName.setText(ring_name);
                mclock.setRing(ring_name);
                String[] c = cursor.getColumnNames();
                for (String string : c) {

                }
            }
            cursor.close();
        }
    }

    private void showSinChosDia() {
        final String[] mList = {"普通", "扫码", "疯狂摇手机", "算术题"};
        yourChose = 3;
        AlertDialog.Builder sinChosDia = new AlertDialog.Builder(AddAlarmActivity.this);
        sinChosDia.setTitle("选择闹钟停止方式");
        sinChosDia.setSingleChoiceItems(mList, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                yourChose = which;

            }
        });
        sinChosDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                showClickMessage(mList[yourChose]);

            }
        });
        sinChosDia.create().show();
    }

    /*显示点击的内容*/
    private void showClickMessage(String message) {

        if (message == "疯狂摇手机")
            showshakeChos();
        else
            //待补充
            Toast.makeText(AddAlarmActivity.this, "你选择的是: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showshakeChos() {
        final String[] mList = {"疯狂摇手机变换次数"};
        yourChose = 0;
        AlertDialog.Builder sinChosDia = new AlertDialog.Builder(AddAlarmActivity.this);
        sinChosDia.setTitle("设置摇动次数");
        //待修改为输入文本，或者选择！
        sinChosDia.setSingleChoiceItems(mList, 0, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                yourChose = which;

            }
        });
        sinChosDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                showClickMessage(mList[yourChose]);

            }
        });
        sinChosDia.create().show();
    }


}


