package com.liwenquan.sl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    private static final String TAG="CrimeListFragment";
    private ArrayList<Clock> mClocks;
    public static final String EXTRA_CRIME_ID="com.liwenquan.sleep.clock_id";
    private ListView mListView;
    ClockAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_list);
        mClocks=ClockLab.get(this).getClocks();
        adapter=new ClockAdapter(mClocks);
        ListView mListView= (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_bug) {
                    Toast.makeText(ClockListActivity.this,"点击关于⊙０⊙",Toast.LENGTH_SHORT).show();
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

                Intent i=new Intent(getApplicationContext(),AddAlarmActivity.class);
                startActivityForResult(i,0);
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

        Switch mswitchOn;
        TextView mtvClockClock;
        SimpleDateFormat dateFormater;
        public ClockAdapter(ArrayList<Clock> crimes) {
            super(ClockListActivity.this,0,crimes);
        }

        //覆盖getView方法
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(convertView==null){
                convertView=getLayoutInflater().inflate(R.layout.list_cell,null);
            }
            //问题在这里
            final Clock c=getItem(position);
            TextView titleTextView=(TextView)convertView.findViewById(R.id.tvlable);
            titleTextView.setText(c.getLable());

            mswitchOn= (Switch) convertView.findViewById(R.id.switchOn);
            mswitchOn.setChecked(c.isOn());
            mswitchOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mswitchOn.setChecked(isChecked);
                    c.setOn(isChecked);
                    ClockLab.get(getApplicationContext()).saveClocks();
                }
            });

            //格式化时间
            dateFormater = new SimpleDateFormat("HH:mm");
            TextView dateTextView =(TextView)convertView.findViewById(R.id.tvTime);
            dateTextView.setText(dateFormater.format(c.getDate()));

            mtvClockClock= (TextView) convertView.findViewById(R.id.tvTime);
            mtvClockClock.setOnClickListener(new View.OnClickListener() {
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
    }

}
