package com.liwenquan.wakeup.splash;

import android.app.Activity;
import android.os.Bundle;

import com.liwenquan.wakeup.ClockListActivity;
import com.liwenquan.wakeup.R;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();
    }

    private void initData() {
        ClockListActivity.goTo(this);
        finish();
    }
}
