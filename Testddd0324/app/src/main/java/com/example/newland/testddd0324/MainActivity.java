package com.example.newland.testddd0324;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText ed;
    ListView listView;
    Button bt_send,bt_close;
    List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = findViewById(R.id.ed1);
        listView = findViewById(R.id.listview);
        bt_send = findViewById(R.id.bt1);
        bt_close = findViewById(R.id.bt2);


        list = new ArrayList();
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.add("云飞扬："+ed.getText().toString());
                Adapter adapter = new MyAdpater(MainActivity.this,list);
                listView.setAdapter((BaseAdapter) adapter);
                listView.setSelection(listView.getBottom());
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });
    }
}
class MyAdpater extends BaseAdapter{

    LayoutInflater inflater;
    List list;
    public MyAdpater(Context context, List list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
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
        TextView tv = view.findViewById(R.id.tv_item);
        tv.setText(list.get(i).toString());

        return view;
    }
}
