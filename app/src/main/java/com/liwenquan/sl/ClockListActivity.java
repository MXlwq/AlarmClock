package com.liwenquan.sl;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by LWQ on 2016/4/8.
 */
public class ClockListActivity extends Activity implements View.OnClickListener {
    private ArrayList<Clock> mClocks;
    public static final String PLAY_ALARM = "com.liwenquan.sl.playalarm";
    public static final String EXTRA_CRIME_ID = "com.liwenquan.sleep.clock_id";
    private ListView mListView;
    ClockAdapter adapter;
    static boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_list);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        firstTime = prefs.getBoolean("first_time", true);
        if (firstTime) {
            Toast.makeText(getApplicationContext(), "HEllo", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ClockListActivity.this, HelloActivity.class));
            SharedPreferences.Editor pEdit = prefs.edit();
            pEdit.putBoolean("first_time", false);
            pEdit.commit();
        }
        mClocks = ClockLab.get(this).getClocks();
        adapter = new ClockAdapter(mClocks);
        mListView = (ListView) findViewById(R.id.list_view_main);
        if (adapter != null)
            mListView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.action_bug) {
                    Toast.makeText(ClockListActivity.this, "请点击关于⊙０⊙", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(ClockListActivity.this, PlayAlarmActivity.class));
                    return true;
                } else if (id == R.id.action_about) {
                    startActivity(new Intent(ClockListActivity.this, AboutUsActivity.class));
                    return true;
                }
                return true;
            }
        });
        toolbar.setTitle(R.string.home_page);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        findViewById(R.id.action_setting).setOnClickListener(this);

        findViewById(R.id.clock_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), AddAlarmActivity.class);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //刷新列表信息；
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_setting:
                startActivity(new Intent(ClockListActivity.this, SettingActivity.class));
                break;
        }
    }

    //定制列表项，创建ArrayAdapter作为CrimeListFragment的内部类
    private class ClockAdapter extends ArrayAdapter<Clock> {


        SimpleDateFormat dateFormater;

        public ClockAdapter(ArrayList<Clock> crimes) {
            super(ClockListActivity.this, 0, crimes);
        }

        //覆盖getView方法
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_cell, null);
                holder = new ViewHolder();
                holder.mswitchOn = (Switch) convertView.findViewById(R.id.switchOn);
                holder.mtvClockClock = (TextView) convertView.findViewById(R.id.tvTime);
                holder.titleTextView = (TextView) convertView.findViewById(R.id.tvlable);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            //已解决
            final Clock c = getItem(position);
            holder.titleTextView.setText(c.getLable());


            //mswitchOn = (Switch) convertView.findViewById(R.id.switchOn);
            holder.mswitchOn.setChecked(c.isOn());
            final ViewHolder finalHolder = holder;
            holder.mswitchOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    finalHolder.mswitchOn.setChecked(isChecked);
                    //Toast.makeText(getContext(),"点击的是第"+position+"个闹钟",Toast.LENGTH_SHORT).show();
                    c.setOn(isChecked);
                    ClockLab.get(getApplicationContext()).saveClocks();
//                    if (isChecked == true) {
//                        Intent i = new Intent(ClockListActivity.this, PlayAlarmActivity.class);
//                        Calendar calendar = Calendar.getInstance();
//                        String s = getIntent().getStringExtra("闹钟时间");
//                        String timelist[] = s.split(":");
//                        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timelist[0]));
//                        calendar.set(Calendar.MINUTE, Integer.valueOf(timelist[1]));
//                        calendar.set(Calendar.SECOND, 0);
//                        calendar.set(Calendar.MILLISECOND, 0);
//                        Toast.makeText(getContext(),"闹钟ID"+c.getId(),Toast.LENGTH_SHORT).show();
//                        PendingIntent pi = PendingIntent.getActivity(ClockListActivity.this, c.getId().hashCode(), i, 0);
//                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                                AddAlarmActivity.INTERVAL, pi);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
//                                calendar.getTimeInMillis(), pi);
//                    }
                    if (isChecked == false) {
                        Intent i = new Intent(PLAY_ALARM);
                        Toast.makeText(getContext(), "闹钟ID" + c.getId(), Toast.LENGTH_SHORT).show();
                        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                        //此处有问题，待解决
                        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), Integer.valueOf(c.getId().hashCode()), i, 0);
                        am.cancel(pi);
                    }
                }
            });

            //格式化时间
            dateFormater = new SimpleDateFormat("HH:mm");
            TextView dateTextView = (TextView) convertView.findViewById(R.id.tvTime);
            dateTextView.setText(dateFormater.format(c.getDate()));

            holder.mtvClockClock = (TextView) convertView.findViewById(R.id.tvTime);
            holder.mtvClockClock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(ClockListActivity.this, SetAlarmActivity.class);
                    i.putExtra(EXTRA_CRIME_ID, c.getId());
                    i.putExtra("闹钟时间", dateFormater.format(c.getDate()));
                    startActivity(i);
                }
            });
            return convertView;
        }

        public final class ViewHolder {
            public Switch mswitchOn;
            public TextView mtvClockClock;
            public TextView titleTextView;
        }
    }

}
