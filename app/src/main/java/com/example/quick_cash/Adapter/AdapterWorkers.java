package com.example.quick_cash.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quick_cash.Model.ModelWorker;
import com.example.quick_cash.R;

import java.util.List;


public class AdapterWorkers extends BaseAdapter {

    private final Context mContext;


    // The  Model List (workerInfo)
    private final List<ModelWorker> list;

    public AdapterWorkers(Context context , List<ModelWorker> list){
        this.list = list;
        this.mContext = context;
    }

    /**
     *
     * @return
     * get the list size
     */
    @Override
    public int getCount(){
        return list.size();
    }


    /**
     *
     * @param position -> the item we are checking
     * @return
     * get this position item
     */
    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    /**
     *
     * @param position -> item we are checking
     * @return itemId
     */
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterWorkers.ViewHolder holder;

        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.adapter_workersinfo,null);
            holder = new AdapterWorkers.ViewHolder();
            holder.workerPic = convertView.findViewById(R.id.wokers_head_pic);
            holder.workerDes = convertView.findViewById(R.id.workers_des);
            convertView.setTag(holder);
        }else {
            holder = (AdapterWorkers.ViewHolder)convertView.getTag();
        }

        ModelWorker worker = list.get(position);
        holder.workerDes.setText(worker.getDes());
        holder.workerPic.setImageResource(R.drawable.headpic);
        return convertView;
    }

    /**
     * A separate class for View Holder
     */
    static class ViewHolder {
        ImageView workerPic;
        TextView workerDes;
    }


}
