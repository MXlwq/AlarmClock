package com.liwenquan.sl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    int yourChose = 1;
    String[] mList = {"1分钟", "5分钟", "10分钟", "30分钟"};
    private TextView mTvSnoozeTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        setSupportActionBar(toolbar);
        mTvSnoozeTime = (TextView) findViewById(R.id.tvSnoozeTime);
        if (savedInstanceState != null) {
            mTvSnoozeTime.setText(savedInstanceState.getString("我是睡眠时间"));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.snoozeTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinChosDia();

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("我是睡眠时间", mList[yourChose]);
    }

    private void showSinChosDia() {

        yourChose = 1;
        AlertDialog.Builder singleChoiseDialog = new AlertDialog.Builder(SettingActivity.this);
        singleChoiseDialog.setTitle("小睡时间");

        singleChoiseDialog.setSingleChoiceItems(mList, 1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                yourChose = which;
            }
        });
        singleChoiseDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                mTvSnoozeTime = (TextView) findViewById(R.id.tvSnoozeTime);
                mTvSnoozeTime.setText(mList[yourChose]);

            }
        });
        singleChoiseDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                showClickMessage(mList[yourChose]);
                mTvSnoozeTime = (TextView) findViewById(R.id.tvSnoozeTime);
                mTvSnoozeTime.setText(mList[yourChose]);
            }
        });
        singleChoiseDialog.show();

    }

    /*显示点击的内容*/
    private void showClickMessage(String message) {
        Toast.makeText(SettingActivity.this, "小睡时间设定为：" + message, Toast.LENGTH_SHORT).show();
    }


}
