package com.liwenquan.sl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LWQ on 2016/3/30.
 */
class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    //数据集
    public static List<String> list;
    public MyAdapter(List<String> list) {
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
    }

    //这个方法主要生成为每个Item inflater出一个View，该方法返回的是一个ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_cell, viewGroup, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTime;
        private MyItemClickListener mListener;
        //private MyItemLongClickListener mLongClickListener;
        public TextView getTextView() {
            return tvTime;
        }

        public ViewHolder(View view) {
            super(view);
            // TODO Auto-generated constructor stub
            tvTime = (TextView) view.findViewById(R.id.tvTime);

        }

        @Override
        public void onClick(View v) {

        }
    }


}

