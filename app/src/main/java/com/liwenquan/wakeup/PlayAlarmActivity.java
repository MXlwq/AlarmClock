package com.liwenquan.wakeup;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.TextView;

public class PlayAlarmActivity extends Activity {
    public static final String PALY_ALARM = "com.liwenquan.sl.playalarm";
    private static final String TAG = "PalyAlarmTAG";
    private MediaPlayer mp;
    private TextView tvStop;
    private Button mputoff;
    private GestureDetector mGestureDetector;
    private Clock mclock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_alarm);
        String clockId = getIntent().getStringExtra(ClockListActivity.EXTRA_CRIME_ID);
        mclock = ClockLab.get(PlayAlarmActivity.this).getClock(clockId);
        mp = MediaPlayer.create(PlayAlarmActivity.this, RingtoneManager.getActualDefaultRingtoneUri(PlayAlarmActivity.this,
                RingtoneManager.TYPE_ALARM));
        mp.start();

        tvStop = (TextView) findViewById(R.id.stopclock);

        //1.重写 GestureDetector的onFling方法
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 控制只右滑
                if (e2.getX() - e1.getX() > 0
                        && (e1.getX() >= 0 && e1.getX() <= 500)) {
                    if (Math.abs(e2.getX() - e1.getX()) > Math.abs(e2.getY() - e1.getY())
                            && Math.abs(velocityX) > 100) {
                        //此处有问题，待解决√，已解决
                        //需要注释
                        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), AddAlarmActivity.AlarmID, new Intent(getApplicationContext(), AlarmReceiver.class), 0);
                        am.cancel(pi);
                        mp.stop();
                        PlayAlarmActivity.this.finish();
                        onBackPressed();
                    }
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    //2.让手势识别器 工作起来,当activity被触摸的时候调用的方法.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        // TODO 自动生成的方法存根
        super.onBackPressed();
        //overridePendingTransition(0, R.anim.base_slide_right_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.release();
    }
}
