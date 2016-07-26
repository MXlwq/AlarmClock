package com.liwenquan.wakeup;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {
    public static final String EXTAR_POSITON = "com.lwq.position";
    private static final String TAG = "AlarmClockTime";
    Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    private TextView mtvalarmlable;
    private AudioManager audiomanger;
    private int maxVolume, currentVolume;
    private SeekBar seekBar;
    private EditText metLable;
    private Clock mclock;
    private String lable;
    private Calendar calendar;
    private TextView mRingName;
    private Uri pickedURI;
    private int yourChose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        initView();
        initData();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_setting);//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        findViewById(R.id.linearadd).setVisibility(View.GONE);
        findViewById(R.id.linearset).setVisibility(View.VISIBLE);
        String clockId = getIntent().getStringExtra(ClockListActivity.EXTRA_CRIME_ID);
        mclock = ClockLab.get(SetAlarmActivity.this).getClock(clockId);

        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        mRingName = (TextView) findViewById(R.id.ring_name);
        mRingName.setText(mclock.getRing());

        mtvalarmlable = (TextView) findViewById(R.id.tvalarmlable);
        mtvalarmlable.setText(mclock.getLable());
        final int position = getIntent().getIntExtra(EXTAR_POSITON, 0);
        SimpleDateFormat dateFormater = new SimpleDateFormat("HH:mm");

        String s = dateFormater.format(mclock.getDate());
        String timelist[] = s.split(":");
        timePicker.setCurrentHour(Integer.valueOf(timelist[0]));
        timePicker.setCurrentMinute(Integer.valueOf(timelist[1]));


        calendar = Calendar.getInstance();
        timePicker.setIs24HourView(true);//显示24小时制
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                mclock.setDate(calendar.getTime());
            }
        });
        findViewById(R.id.how_to_shutdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinChosDia();
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
                Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(SetAlarmActivity.this, RingtoneManager.TYPE_ALARM);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(SetAlarmActivity.this);
                builder.setTitle("标签");
                View view = LayoutInflater.from(SetAlarmActivity.this).inflate(R.layout.dialog_lable, null);
                builder.setView(view);
                metLable = (EditText) view.findViewById(R.id.etLable);

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lable = metLable.getText().toString().trim();
                        mtvalarmlable = (TextView) findViewById(R.id.tvalarmlable);
                        mtvalarmlable.setText(lable);
                        mclock.setLable(lable);
                        Toast.makeText(SetAlarmActivity.this, "已设定标签：" + lable, Toast.LENGTH_SHORT);
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
        findViewById(R.id.btnDelClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockLab.get(SetAlarmActivity.this).deleteClock(mclock);
                ClockLab.get(SetAlarmActivity.this).saveClocks();
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), AddAlarmActivity.AlarmID, new Intent(getApplicationContext(), AlarmReceiver.class), 0);
                am.cancel(pi);
                finish();
            }
        });
        findViewById(R.id.btnSaveClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetAlarmActivity.this, ClockListActivity.class);
                startActivity(intent);
                if (mclock.getLable() == null) {
                    mclock.setLable("闹钟");
                }
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), AddAlarmActivity.AlarmID, new Intent(getApplicationContext(), AlarmReceiver.class), 0);
                am.cancel(pi);

                Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
                pi = PendingIntent.getBroadcast(getApplicationContext(), mclock.getId().hashCode(), i, 0);
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AddAlarmActivity.INTERVAL_TIME, AddAlarmActivity.pi);
                ClockLab.get(getApplicationContext()).saveClocks();
                SetAlarmActivity.this.finish();
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
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case 1:
                //获取选中的铃声的URI
                pickedURI = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                mRingName.setText(pickedURI.toString());
                mclock.setRing(pickedURI.toString());
                ClockLab.get(getApplicationContext()).saveClocks();
                //Log.i(TAG,pickedURI.toString());
                getName(RingtoneManager.TYPE_ALARM);

                break;

            default:
                break;
        }
    }

    private void getName(int type) {
        Uri pickedUri = RingtoneManager.getActualDefaultRingtoneUri(this, type);
        //Log.i(TAG,pickedUri.toString());
        Cursor cursor = this.getContentResolver().query(pickedUri, new String[]{MediaStore.Audio.Media.TITLE}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String ring_name = cursor.getString(0);

                //Log.i(TAG,ring_name);
                String[] c = cursor.getColumnNames();
                for (String string : c) {
                    //Log.i(TAG,string);
                }
            }
            cursor.close();
        }
    }

    private void showSinChosDia() {
        final String[] mList = {"普通", "扫码", "拍相同颜色照片", "疯狂摇手机", "算术题"};
        yourChose = 3;
        AlertDialog.Builder sinChosDia = new AlertDialog.Builder(SetAlarmActivity.this);
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
            Toast.makeText(SetAlarmActivity.this, "你选择的是: " + message, Toast.LENGTH_SHORT).show();
    }

    private void showshakeChos() {
        final String[] mList = {"疯狂摇手机变换次数"};
        yourChose = 0;
        AlertDialog.Builder sinChosDia = new AlertDialog.Builder(SetAlarmActivity.this);
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


