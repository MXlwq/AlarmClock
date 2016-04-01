package com.liwenquan.sl;

import android.app.Activity;
import android.app.AlarmManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_play_alarm);
        mp = MediaPlayer.create(this, RingtoneManager.getActualDefaultRingtoneUri(this,
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
                /**
                 * 当手势滑动的时候，关闭页面的效果，具体需求具体对待
                 * 向下滑动，向上滑动，向右滑动（常用使用该方式）
                 */

//                // 手势向下 down
//                if ((e2.getRawY() - e1.getRawY()) > 200) {
//                    //finish();//在此处控制关闭
//                    return true;
//                }
//                // 手势向上 up
//                if ((e1.getRawY() - e2.getRawY()) > 100) {
//                    finish();//在此处控制关闭
//                    return true;
//                }
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
