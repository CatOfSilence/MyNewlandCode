package com.example.newland.testlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    ListView listView;
    EditText ed;
    Button bt;
    int i = 0;
    
    Map<String,Object> map,map1;
    List list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listview);
        ed = findViewById(R.id.ed);
        bt = findViewById(R.id.bt);

        list = new ArrayList();

        listView.setAdapter(new MyAdapter(list,LayoutInflater.from(getApplicationContext())));

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed.getText().toString().isEmpty()){
                    list.add("空的，跟你搞这个");
                }else{
                    list.add(ed.getText().toString());
                }
                listView.setAdapter(new MyAdapter(list,LayoutInflater.from(getApplicationContext())));
            }
        });
    }
}

class MyAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List list = new ArrayList();

    public MyAdapter(List list,LayoutInflater inflater){

        this.list = list;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.item,null);

        TextView tv = view.findViewById(R.id.tv1);

        tv.setText(list.get(i).toString());

        return view;
    }
}