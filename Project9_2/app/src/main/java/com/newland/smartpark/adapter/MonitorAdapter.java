package com.newland.smartpark.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.smartpark.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class MonitorAdapter extends RecyclerView.Adapter<MonitorAdapter.ViewHolder> {
    private List<String> monitorImageList;

    public MonitorAdapter(List<String> monitorImageList)
    {
        this.monitorImageList=monitorImageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imagepath=monitorImageList.get(position);
        //取出拍照时间
        String time= imagepath.substring(0,imagepath.indexOf(".png"));
        InputStream input=null;
        Bitmap bitmap=null;
        try {
            input= new FileInputStream("/mnt/sdcard/pic/"+imagepath);
          bitmap=  BitmapFactory.decodeStream(input);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        holder.image.setImageBitmap(bitmap);
        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        return monitorImageList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView image;//图片
        private TextView time;//时间
        public ViewHolder(View v)
        {
            super(v);
            image = v.findViewById(R.id.image);
            time = v.findViewById(R.id.time);
        }
    }
}
