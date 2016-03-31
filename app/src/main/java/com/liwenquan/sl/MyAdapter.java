package com.liwenquan.sl;

import android.content.Context;
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
    //数据集
    private Context mContext;
    private static ArrayList<String> list;
    StringBuffer sb;
    SharedPreferences.Editor editor;

    public MyAdapter(Context context, ArrayList<String> list) {
        mContext = context;
        this.list = list;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    //adapter需要实现以下三个方法

    //获取数据集大小
    @Override
    public int getItemCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public void addData(int position) {
        list.add(position, "Insert One");
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    //绑定数据到ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // TODO Auto-generated method stub
        viewHolder.getTextView().setText(list.get(position));
        //viewHolder.getTvLable().setText(list.get(position));
    }

    //这个方法主要生成为每个Item inflater出一个View，该方法返回的是一个ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int position) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_cell, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) mContext).startAlarmDetailsActivity(list.get(position));
                //System.err.print("position" + position);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                ((MainActivity) mContext).deleteAlarm(position);
                return true;
            }
        });

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private static final String KEY = "alarmList";

//    public void remove(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//        sb = new StringBuffer();
//        for (int i = 0; i < getItemCount(); i++) {
//            sb.append(list.get(i)).append(",");
//        }
//        String content;
//        if (getItemCount() == 0)
//            content = null;
//        else
//            content = sb.toString().substring(0, sb.length() - 1);
//        editor = this.getSharedPreferences(AddAlarmActivity.class.getName(), Context.MODE_PRIVATE).edit();
//        editor.putString(KEY, content);
//        editor.commit();
//    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mtvTime;

        public TextView getTvLable() {
            return mtvLable;
        }

        private TextView mtvLable;

        public TextView getTvTime() {
            return mtvTime;
        }

        private MyItemClickListener mListener;

        //private MyItemLongClickListener mLongClickListener;
        public TextView getTextView() {
            return mtvTime;
        }

        public ViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            mtvTime = (TextView) view.findViewById(R.id.tvTime);

        }

    }


}

