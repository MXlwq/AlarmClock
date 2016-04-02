package com.liwenquan.sl;

import android.app.Activity;
import android.app.AlarmManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayAlarmActivity extends Activity {

    private MediaPlayer mp;
    private TextView tvStop;
    private Button putoff;
    private AlarmManager alarmManager;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_alarm);
        mp = MediaPlayer.create(PlayAlarmActivity.this, RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_ALARM));
        mp.start();
        putoff = (Button) findViewById(R.id.putoff);
        putoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        tvStop = (TextView) findViewById(R.id.stopclock);
//        tvStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                // TODO 自动生成的方法存根
//                startActivity(new Intent(PlayAlarmActivity.this, MainActivity.class));
//            }
//        });
        //1.重写 GestureDetector的onFling方法
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 控制只右滑
                if (e2.getX() - e1.getX() > 0
                        && (e1.getX() >= 0 && e1.getX() <= 500)) {
                    if (Math.abs(e2.getX() - e1.getX()) > Math.abs(e2.getY() - e1.getY())
                            && Math.abs(velocityX) > 100) {
                        // And cancel the alarm.
                        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                        am.cancel(AddAlarmActivity.pi);
                        finish();
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
