package com.example.newland.testwsdsj0425;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    GridView gv;
    Spinner sp;
    TextView tv_wd, tv_sd;
    Button bt_time1, bt_time2;
    EditText ed_value1, ed_value2;

    ZigBee zigBee;
    Handler handler;
    Thread thread;


    int wd = 0;
    int sd = 0;
    int count = 0;
    boolean time_flag = false;


    Mysql mysql;
    SQLiteDatabase db;
    List<Map<String, String>> list;
    List<Map<String, String>> list2;
    SimpleAdapter simpleAdapter;
    ArrayAdapter<String> arrayAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gv = findViewById(R.id.gv);
        sp = findViewById(R.id.sp);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt_time1 = findViewById(R.id.bt_time1);
        bt_time2 = findViewById(R.id.bt_time2);
        ed_value1 = findViewById(R.id.ed_value1);
        ed_value2 = findViewById(R.id.ed_value2);


        mysql = new Mysql(MainActivity.this, "data", null, 1);
        db = mysql.getWritableDatabase();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.gv_item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("192.168.25.16", 2002), null);
        initSql();

        arrayAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, R.id.tv_sp, getResources().getStringArray(R.array.abc));
        sp.setAdapter(arrayAdapter);

        bt_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View vv = LayoutInflater.from(MainActivity.this).inflate(R.layout.time_item, null);
                TimePicker timePicker = vv.findViewById(R.id.tp_time);
                timePicker.setIs24HourView(true);
                Button bt_time = vv.findViewById(R.id.bt_time);
                builder.setCancelable(false);
                builder.setView(vv);
                AlertDialog dialog = builder.create();
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                        bt_time1.setText("" + i + ":" + i1 + ":00");
                        time_flag = true;
                    }
                });
                bt_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!time_flag) {
                            bt_time1.setText(sdf2.format(new Date()));
                        }
                        time_flag = false;
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        bt_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View vv = LayoutInflater.from(MainActivity.this).inflate(R.layout.time_item, null);
                TimePicker timePicker = vv.findViewById(R.id.tp_time);
                timePicker.setIs24HourView(true);
                Button bt_time = vv.findViewById(R.id.bt_time);
                builder.setCancelable(false);
                builder.setView(vv);
                AlertDialog dialog = builder.create();
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                        bt_time2.setText("" + i + ":" + i1 + ":00");
                        time_flag = true;
                    }
                });
                bt_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!time_flag) {
                            bt_time2.setText(sdf2.format(new Date()));
                        }
                        time_flag = false;
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
                    double vals[] = zigBee.getFourEnter();
                    wd = (int) FourChannelValConvert.getTemperature(vals[0]);
                    sd = (int) FourChannelValConvert.getHumidity(vals[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_wd.setText("" + wd);
                tv_sd.setText("" + sd);

                count++;
                if (count == 5) {
                    count = 0;
                    String time = sdf.format(new Date());

                    ContentValues values = new ContentValues();
                    values.put("date", time);
                    values.put("type", "湿度");
                    values.put("value", String.valueOf(sd) + "Rh");
                    db.insert("data", null, values);
                    values.clear();
                    values.put("date", time);
                    values.put("type", "温度");
                    values.put("value", String.valueOf(wd) + "℃");
                    db.insert("data", null, values);


                    Collections.reverse(list);
                    Map<String, String> map = new HashMap<>();
                    map.put("date", time);
                    map.put("type", "湿度");
                    map.put("value", String.valueOf(sd) + "Rh");
                    list.add(map);
                    map = new HashMap<>();
                    map.put("date", time);
                    map.put("type", "温度");
                    map.put("value", String.valueOf(wd) + "℃");
                    list.add(map);

                    Collections.reverse(list);
                    gv.setAdapter(simpleAdapter);
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void initSql() {
        Cursor cursor = db.query("data", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put("date", cursor.getString(cursor.getColumnIndex("date")));
            map.put("type", cursor.getString(cursor.getColumnIndex("type")));
            map.put("value", cursor.getString(cursor.getColumnIndex("value")));
            list.add(map);
        }
        Collections.reverse(list);
        gv.setAdapter(simpleAdapter);
    }

    public void query(View view) {
        list2.clear();
        if (!bt_time1.getText().toString().isEmpty() && !bt_time2.getText().toString().isEmpty() && !ed_value1.getText().toString().isEmpty() && !ed_value2.getText().toString().isEmpty()) {
            try {
                if (sp.getSelectedItemPosition() == 0) {
                    String t1 = sdf1.format(new Date()) + " " + bt_time1.getText().toString();
                    String t2 = sdf1.format(new Date()) + " " + bt_time2.getText().toString();
                    Date d1 = sdf.parse(t1);
                    Date d2 = sdf.parse(t2);
                    int i1 = Integer.parseInt(ed_value1.getText().toString());
                    int i2 = Integer.parseInt(ed_value2.getText().toString());
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).get("type").equals("温度")){
                            if(sdf.parse(list.get(i).get("date")).getTime() >= d1.getTime()&&sdf.parse(list.get(i).get("date")).getTime() <= d2.getTime()){
                                int s = Integer.parseInt(list.get(i).get("value").substring(0,list.get(i).get("value").indexOf("℃")));
                                if(s >= i1&&s <= i2){
                                    list2.add(list.get(i));
                                }
                            }
                        }
                    }
                } else if (sp.getSelectedItemPosition() == 1) {
                    String t1 = sdf1.format(new Date()) + " " + bt_time1.getText().toString();
                    String t2 = sdf1.format(new Date()) + " " + bt_time2.getText().toString();
                    Date d1 = sdf.parse(t1);
                    Date d2 = sdf.parse(t2);
                    int i1 = Integer.parseInt(ed_value1.getText().toString());
                    int i2 = Integer.parseInt(ed_value2.getText().toString());
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).get("type").equals("湿度")){
                            if(sdf.parse(list.get(i).get("date")).getTime() >= d1.getTime()&&sdf.parse(list.get(i).get("date")).getTime() <= d2.getTime()){
                                int s = Integer.parseInt(list.get(i).get("value").substring(0,list.get(i).get("value").indexOf("Rh")));
                                if(s >= i1&&s <= i2){
                                    list2.add(list.get(i));
                                }
                            }
                        }
                    }
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            simpleAdapter = new SimpleAdapter(MainActivity.this, list2, R.layout.gv_item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
            gv.setAdapter(simpleAdapter);
        }else{
            simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.gv_item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
            gv.setAdapter(simpleAdapter);
        }
    }
}
