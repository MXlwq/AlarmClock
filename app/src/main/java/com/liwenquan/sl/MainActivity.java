package com.liwenquan.sl;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences.Editor editor;
    StringBuffer sb;
    private AlarmManager alarmManager;
    private TextView tvTime;
    private RecyclerView mRecyclerView;
    int count;

    List<String> list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(new MyAdapter(list));
        //saveAlarmList();
        tvTime = (TextView) findViewById(R.id.tvTime);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_bug) {
                    startActivity(new Intent(MainActivity.this, PlayAlarmActivity.class));
                    return true;
                } else if (id == R.id.action_about) {
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                    return true;
                }
                return true;
            }
        });
        toolbar.setTitle(R.string.home_page);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        findViewById(R.id.imgAdd).setOnClickListener(this);
        findViewById(R.id.action_setting).setOnClickListener(this);
        tvTime.setText("当前的时间" + getIntent().getStringExtra("时间"));


    }

    private static final String KEY = "alarmList";

    public void saveAlarmList() {
        editor = getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE).edit();
        sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            sb.append(AddAlarmActivity.list.get(i)).append(",");
        }
//        String content = sb.toString().substring(0, sb.length() - 1);
//        editor.putString(KEY, content);
//        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgAdd:
                startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
                //finish();
                break;
            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
    }

    private class MyAdapter extends RecyclerView.Adapter {
        private List<String> list;

        public MyAdapter(List<String> list) {
            this.list = list;
        }
        @Override
        public int getItemCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.list_cell, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder vh=(ViewHolder) holder;
            vh.getTvClock().setText(list.get(position));
        }


        class ViewHolder extends RecyclerView.ViewHolder {

            public Button tvClock;

            public Button getTvClock() {
                return tvClock;
            }

            public ViewHolder(View view) {
                super(view);
                // TODO Auto-generated constructor stub
                tvClock = (Button) view.findViewById(R.id.tvClock);

                tvClock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
//                        Snackbar.make(v, "当前点击的位置：" + getAdapterPosition(), Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                    }
                });
                tvTime.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //remove(getAdapterPosition());

                        return true;

                    }
                });
            }

        }
    }

//    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
//        private List<String> list;
//
//        public MyAdapter(List<String> list) {
//            this.list = list;
//        }
//
//
//        @Override
//        public int getItemCount() {
//            // TODO Auto-generated method stub
//            return list.size();
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return super.getItemViewType(position);
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder viewHolder, int position) {
//            // TODO Auto-generated method stub
//            viewHolder.textView.setText(list.get(position));
//        }
//
//        @Override
//        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
//            // TODO Auto-generated method stub
//            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
//                    R.layout.list_cell, viewGroup, false);
//            ViewHolder holder = new ViewHolder(view);
//            return holder;
//        }
//
//
//        public class ViewHolder extends RecyclerView.ViewHolder {
//
//            public Button textView;
//
//            public ViewHolder(View view) {
//                super(view);
//                // TODO Auto-generated constructor stub
//                textView = (Button) view.findViewById(R.id.tvTitle);
//                textView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(MainActivity.this, AddAlarmActivity.class));
////                        Snackbar.make(v, "当前点击的位置：" + getAdapterPosition(), Snackbar.LENGTH_LONG)
////                                .setAction("Action", null).show();
//                    }
//                });
//                textView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
////                        remove(getAdapterPosition());
//
//                        return true;
//
//                    }
//                });
//            }
//
//        }
//
////        public void remove(int position) {
////            list.remove(position);
////            notifyItemRemoved(position);
////            sb = new StringBuffer();
////            for (int i = 0; i < getItemCount(); i++) {
////                sb.append(list.get(i)).append(",");
////            }
////            String content;
////            if (getItemCount() == 0)
////                content = null;
////            else
////                content = sb.toString().substring(0, sb.length() - 1);
////            editor = getContext().getSharedPreferences(ClockFragment.class.getName(), Context.MODE_PRIVATE).edit();
////            editor.putString(KEY, content);
////            editor.commit();
////        }
//
//
//    }

}
