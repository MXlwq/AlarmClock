package com.liwenquan.sleep;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.Toast;

public class ClockSetAty extends AppCompatActivity {

    private static final int Alarm = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_set_clock);
        Spinner spinner = (Spinner) findViewById(R.id.sp);
        spinner.setSelection(2, true);//将“每天”设置为默认

        findViewById(R.id.clockset).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.btnDelClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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


