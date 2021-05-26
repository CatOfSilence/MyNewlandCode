package com.newland.smartpark.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.newland.smartpark.R;
import com.newland.smartpark.adapter.MonitorAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {
    //图片路径的集合
    public static List<String> imageList;
    static{
        //从SD卡中取出监控截图
        imageList= getAllPics();

    }
    //获得图片路径的集合
    public static List<String> getAllPics()
    {
        List<String> list=new ArrayList();
        File f = new File(Environment.getExternalStorageDirectory().getPath(),"pic");
        String[] fileNames= f.list();

        for(String s:fileNames)
        {
            list.add(s);

        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        MonitorAdapter adapter = new MonitorAdapter(imageList);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
    }
}
