package com.liwenquan.sl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class AddAlarmActivity extends AppCompatActivity {
    private TextView mtvalarmlable;
    private static final int Alarm = 1;
    private AudioManager audiomanger;
    private int maxVolume, currentVolume;
    private SeekBar seekBar;
    private int hour, minute;
    private TextView mtvLable;
    String hourformat, minuteformat;
    SharedPreferences.Editor editor;
    StringBuffer sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        setContentView(R.layout.activity_add_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加闹钟");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        Spinner spinner = (Spinner) findViewById(R.id.sp);
        spinner.setSelection(2, true);//将“每天”设置为默认

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        timePicker.setIs24HourView(true);//是否显示24小时制？默认false
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                AddAlarmActivity.this.hour = hourOfDay;
                AddAlarmActivity.this.minute = minute;
            }
        });
        findViewById(R.id.chooseSong).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                        RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                        "设置闹钟铃声");
                startActivityForResult(intent, Alarm);
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

                final EditText metLable = (EditText)view.findViewById(R.id.etLable);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String lable = metLable.getText().toString().trim();
                        mtvalarmlable= (TextView) findViewById(R.id.tvalarmlable);
                        mtvalarmlable.setText(lable);
                        Toast.makeText(AddAlarmActivity.this, "已设定标签：" + lable, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.btnDelClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //editor.clear();
                finish();
            }
        });
        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.list.add(hour + ":" + minute);
                saveAlarmList(MainActivity.list);
                Intent i = new Intent(AddAlarmActivity.this, MainActivity.class);
                startActivity(i);
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


    private static final String KEY = "alarmList";

    public void saveAlarmList(List<String> list) {
        editor = getSharedPreferences(AddAlarmActivity.class.getName(), MODE_PRIVATE).edit();
        sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            sb.append(MainActivity.list.get(i)).append(",");
        }
        String content = sb.toString().substring(0, sb.length() - 1);
        editor.putString(KEY, content);
        editor.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        } else {
            Uri uri = data
                    .getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                RingtoneManager.setActualDefaultRingtoneUri(this,
                        RingtoneManager.TYPE_ALARM, uri);
                Toast.makeText(getApplicationContext(), "设置闹钟铃声成功！", Toast.LENGTH_SHORT).show();
            }
        }

    }
}


