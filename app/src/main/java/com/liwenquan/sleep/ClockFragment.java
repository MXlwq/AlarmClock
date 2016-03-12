package com.liwenquan.sleep;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ClockFragment extends Fragment {

    private RecyclerView mRecyclerView;
    SharedPreferences.Editor editor;
    StringBuffer sb;
    private AlarmManager alarmManager;
    final List<String> list = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        readSavedAlarmList();
    }

    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }


    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_clock, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        root.findViewById(R.id.btnChooseDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        Calendar currentTime = Calendar.getInstance();
                        if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                            calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                        }
                        AlarmDate ad = new AlarmDate(calendar.getTimeInMillis());
                        list.add(ad.getTimelable());
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                ad.getTime(),
                                5 * 60 * 1000,
                                PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmRecevier.class), 0));
                        MyAdapter adapter = new MyAdapter(list);
                        mRecyclerView.setAdapter(adapter);
                        saveAlarmList(adapter);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });
        root.findViewById(R.id.btnEnterClock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),PlayAlarmAty.class);
                startActivity(i);
            }
        });

        MyAdapter adapter = new MyAdapter(list);
        mRecyclerView.setAdapter(adapter);
        return root;
    }

    private static final String KEY = "alarmList";

    public void saveAlarmList(MyAdapter adapter) {
        editor = getContext().getSharedPreferences(ClockFragment.class.getName(), Context.MODE_PRIVATE).edit();
        sb = new StringBuffer();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            sb.append(list.get(i)).append(",");
        }
        String content = sb.toString().substring(0, sb.length() - 1);
        editor.putString(KEY, content);
        editor.commit();
    }

    private void readSavedAlarmList() {
        SharedPreferences sp = getContext().getSharedPreferences(ClockFragment.class.getName(), Context.MODE_PRIVATE);
        String content = sp.getString(KEY, null);
        if (content != null) {
            String[] timeStrings = content.split(",");
            for (String string : timeStrings) {
                list.add(string);
            }
        }
    }

    class AlarmDate {
        private long time = 0l;
        private Calendar date;
        private String timelable = "";

        public AlarmDate(long time) {
            this.time = time;
            date = Calendar.getInstance();
            date.setTimeInMillis(time);
            timelable = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));

        }

        public String getTimelable() {
            return timelable;
        }

        public long getTime() {
            return time;
        }

        public int getId() {
            return (int) getTime() / 1000 / 60;
        }

        @Override
        public String toString() {
            return getTimelable();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
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
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            // TODO Auto-generated method stub
            viewHolder.textView.setText(list.get(position));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            // TODO Auto-generated method stub
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.list_cell, viewGroup, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            public Button textView;

            public ViewHolder(View view) {
                super(view);
                // TODO Auto-generated constructor stub
                textView = (Button) view.findViewById(R.id.tvTitle);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), ClockSetAty.class));
//                        Snackbar.make(v, "当前点击的位置：" + getAdapterPosition(), Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                    }
                });
                textView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        remove(getAdapterPosition());

                        return true;

                    }
                });
            }

        }

        public void remove(int position) {
            list.remove(position);
            notifyItemRemoved(position);
            sb = new StringBuffer();
            for (int i = 0; i < getItemCount(); i++) {
                sb.append(list.get(i)).append(",");
            }
            String content;
            if (getItemCount() == 0)
                content = null;
            else
                content = sb.toString().substring(0, sb.length() - 1);
            editor = getContext().getSharedPreferences(ClockFragment.class.getName(), Context.MODE_PRIVATE).edit();
            editor.putString(KEY, content);
            editor.commit();
        }


    }

}


