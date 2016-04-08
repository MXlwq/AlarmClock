package com.liwenquan.sl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class AddAlarmActivity extends AppCompatActivity {
    public static final String KEY = "alarmList";
    private static final String TAG = "CrimeListFragment";
    private static final int Alarm = 1;
    static PendingIntent pi;
    SharedPreferences.Editor editor;
    StringBuffer sb;
    private TextView mtvalarmlable;
    private AudioManager audiomanger;
    private int maxVolume, currentVolume;
    private SeekBar seekBar;
    private int hour, minute;
    private TextView mtvLable;
    private String mTime;
    private Calendar calendar;
    private AlarmManager alarmManager;
    Clock clock;
    Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private static final int INTERVAL = 1000 * 60 * 60 * 24;// 24h
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加闹钟");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);


        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        clock=new Clock(Calendar.getInstance().getTime());

        //mTime = formattime(hour, minute);
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        timePicker.setIs24HourView(true);//是否显示24小时制？默认false
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //mTime = formattime(hourOfDay, minute);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                clock=new Clock(calendar.getTime());
            }
        });
        findViewById(R.id.chooseSong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(
//                        RingtoneManager.ACTION_RINGTONE_PICKER);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
//                        RingtoneManager.TYPE_ALARM);
//                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
//                        "设置闹钟铃声");
//                startActivityForResult(intent, Alarm);
                Intent intent = new Intent();
                intent.setAction(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹玲铃声");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALL);
                Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(AddAlarmActivity.this,RingtoneManager.TYPE_ALARM);
                if (pickedUri!=null) {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,pickedUri);
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
                        clock.setLable(lable);
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
                finish();
            }
        });
        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(AddAlarmActivity.this, PlayAlarmActivity.class);
                PendingIntent pi = PendingIntent.getActivity(AddAlarmActivity.this, 0, i, 0);
                alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        INTERVAL, pi);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), pi);
                //Toast.makeText(AddAlarmActivity.this," "+calendar.getTimeInMillis(),Toast.LENGTH_SHORT).show();
//                MainActivity.list.add(mTime);
//                saveAlarmList(MainActivity.list);
                ClockLab.get(getApplicationContext()).addClock(clock);
                Intent intent = new Intent(AddAlarmActivity.this, ClockListActivity.class);
                startActivity(intent);
                ClockLab.get(getApplicationContext()).saveClocks();
                if(clock.getLable()==null){
                    clock.setLable("闹钟");
                }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                //获取选中的铃声的URI
                Uri pickedURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Log.i(TAG,pickedURI.toString());
                clock.setRing(pickedURI.toString());

                getName(RingtoneManager.TYPE_ALARM);
                break;

            default:
                break;
        }
    }
    private void getName(int type){
        Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this, type);
        Log.i(TAG,pickedUri.toString());
        Cursor cursor = this.getContentResolver().query(pickedUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
        if (cursor!=null) {
            if (cursor.moveToFirst()) {
                String ring_name = cursor.getString(0);
                Log.i(TAG,ring_name);
                String[] c = cursor.getColumnNames();
                for (String string : c) {
                    Log.i(TAG,string);
                }
            }
            cursor.close();
        }
    }

}


