package com.example.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weather.R;

import java.util.ArrayList;

public class PlaceAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private  int resourceId;
    private ArrayList<String> datalist;
    private Context context;
    public PlaceAdapter(Context context, int resourceId,ArrayList<String> datalist )
    {
        this.datalist=datalist;
        this.resourceId=resourceId;
        this.context=context;
    }

    @Override
    public int getCount() {
        return datalist==null ? 0 : datalist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null)
        {
            viewHolder=new ViewHolder();
           convertView=mInflater.inflate(android.R.layout.activity_list_item,parent,false);
           viewHolder.tv=convertView.findViewById(R.id.tv);
           viewHolder.tv.setText(datalist.get(position));
           convertView.setTag(viewHolder);

        }else
        {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        return  convertView;
    }
    private  final class  ViewHolder
    {
       TextView tv;
    }

}
