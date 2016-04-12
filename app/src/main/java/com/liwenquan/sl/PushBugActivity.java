package com.liwenquan.sl;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class PushBugActivity extends AppCompatActivity {
    EditText mEditTextTitle, mEditTextAddress;
    private String bugContent, mailaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_bug);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_feed_back);//设置主标题
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.setNavigationIcon(R.mipmap.back);//设置导航栏图标
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mEditTextTitle = (EditText) findViewById(R.id.et_bug_title);
        //mEditTextAddress = (EditText) findViewById(R.id.et_email_address);
        mEditTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bugContent = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        mEditTextAddress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mailaddress = s.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        findViewById(R.id.push_bugs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("mailto:liwq@tju.edu.cn");
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "WakeUp闹钟Bug反馈"); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, bugContent); // 正文
                startActivity(Intent.createChooser(intent, "请选择邮件类应用"));


            }
        });
    }
}
