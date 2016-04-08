package com.liwenquan.sl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by LWQ on 2016/3/30.
 */
class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String KEY = "alarmList";
    private static ArrayList<String> list;
    StringBuffer sb;
    SharedPreferences.Editor editor;
    //数据集
    private Context mContext;


    public MyAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        this.list = list;
    }

    //adapter需要实现以下三个方法

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    //获取数据集大小
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    //绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // TODO Auto-generated method stub
        viewHolder.getTvTime().setText(list.get(position));
    }

    //这个方法主要生成为每个Item inflater出一个View，该方法返回的是一个ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_cell, viewGroup, false);


        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mtvTime;

        public ViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            mtvTime = (TextView) view.findViewById(R.id.tvTime);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, SetAlarmActivity.class);
                    i.putExtra(SetAlarmActivity.EXTAR_TIME, list.get(getPosition()));
                    i.putExtra(SetAlarmActivity.EXTAR_POSITON, getPosition());
                    mContext.startActivity(i);
                }
            });
        }

        public TextView getTvTime() {
            return mtvTime;
        }

    }


}

