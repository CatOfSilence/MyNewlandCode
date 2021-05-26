package com.example.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Adapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public Adapter(Context context) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public ImageView image;
        public TextView Tv_Title, Tv_Content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.image = convertView.findViewById(R.id.image1);
            holder.Tv_Title = convertView.findViewById(R.id.Text1);
            holder.Tv_Content = convertView.findViewById(R.id.Text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.Tv_Title.setText("Cao");
        holder.Tv_Content.setText("Shit");
        return convertView;
    }
}
