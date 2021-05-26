package com.example.newland.testwsdsj0418;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    TextView tv_wd, tv_sd;
    EditText ed_time1, ed_time2, ed_value1, ed_value2;
    double wd = 0, sd = 0;
    int count = 0;
    Spinner sp1;
    SQLiteDatabase db;
    MySql mySql;
    DecimalFormat df = new DecimalFormat("0");
    SimpleAdapter simpleAdapter;
    GridView gridView;
    List<Map<String, String>> list, list2;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigBee.getFourEnter();
                    wd = FourChannelValConvert.getTemperature(vals[0]);
                    sd = FourChannelValConvert.getHumidity(vals[1]);
                    tv_wd.setText("" + df.format(wd));
                    tv_sd.setText("" + df.format(sd));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (count == 5) {
                    count = 0;
                    Collections.reverse(list);
                    String time = sdf.format(new Date());

                    ContentValues values = new ContentValues();
                    values.put("date", time);
                    values.put("type", "湿度");
                    values.put("value", String.valueOf(df.format(sd)) + "Rh");
                    db.insert("data", null, values);
                    values.clear();
                    values.put("date", time);
                    values.put("type", "温度");
                    values.put("value", String.valueOf(df.format(wd)) + "℃");
                    db.insert("data", null, values);

                    HashMap<String,String> map = new HashMap<>();
                    map.put("date", time);
                    map.put("type", "湿度");
                    map.put("value", String.valueOf(df.format(sd)) + "Rh");
                    list.add(map);
                    map = new HashMap<>();
                    map.put("date", time);
                    map.put("type", "温度");
                    map.put("value", String.valueOf(df.format(wd)) + "℃");
                    list.add(map);
                    Collections.reverse(list);
                    gridView.setAdapter(simpleAdapter);
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count++;
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }

    public void init() {
        /*绑定控件*/
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        gridView = findViewById(R.id.gv);
        sp1 = findViewById(R.id.sp1);
        ed_time1 = findViewById(R.id.ed_time1);
        ed_time2 = findViewById(R.id.ed_time2);
        ed_value1 = findViewById(R.id.ed_value1);
        ed_value2 = findViewById(R.id.ed_value2);
        /*设置默认文本内容*/
        ed_time1.setText(sdf.format(new Date()));
        ed_time2.setText(sdf.format(new Date()));

        /*初始化*/
        String str[] = getResources().getStringArray(R.array.fuck);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sp_item, R.id.tv_sp, str);
        ((ArrayAdapter) adapter).setDropDownViewResource(R.layout.sp_item);
        sp1.setAdapter((SpinnerAdapter) adapter);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("192.168.18.16", 2002), null);
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        mySql = new MySql(MainActivity.this, "data", null, 1);
        db = mySql.getWritableDatabase();
        simpleAdapter = new SimpleAdapter(this, list, R.layout.item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
        initSql();
    }

    public void initSql() {
        Handler han = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                gridView.setAdapter(simpleAdapter);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.query("data", null, null, null, null, null, null, null);
                while (cursor.moveToNext()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("date", cursor.getString(cursor.getColumnIndex("date")));
                    map.put("type", cursor.getString(cursor.getColumnIndex("type")));
                    map.put("value", cursor.getString(cursor.getColumnIndex("value")));
                    list.add(map);
                }
                Collections.reverse(list);
                han.sendEmptyMessage(0);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (zigBee != null)
            zigBee.stopConnect();
    }

    public void query(View view) {
        if (!ed_time1.getText().toString().isEmpty() && !ed_time2.getText().toString().isEmpty() && !ed_value1.getText().toString().isEmpty() && !ed_value2.getText().toString().isEmpty()) {
            list2.clear();
            try {
                Date time1 = sdf.parse(ed_time1.getText().toString());
                Date time2 = sdf.parse(ed_time2.getText().toString());
                Double d1 = Double.valueOf(ed_value1.getText().toString());
                Double d2 = Double.valueOf(ed_value2.getText().toString());
                for (int i = 0; i < list.size(); i++) {
                    double wd = 0, sd = 0;
                    if (sp1.getSelectedItemPosition() == 0) {
                        if (list.get(i).get("type").equals("温度")) {
                            String value1 = list.get(i).get("value");
                            value1 = value1.substring(0, value1.indexOf("℃"));
                            wd = Double.parseDouble(value1);
                            if ((sdf.parse(list.get(i).get("date")).getTime() >= time1.getTime())
                                    && (sdf.parse(list.get(i).get("date")).getTime() <= time2.getTime()) && (wd >= d1 && wd <= d2)) {
                                list2.add(list.get(i));
                            }
                        }
                    } else if (sp1.getSelectedItemPosition() == 1) {
                        if (list.get(i).get("type").equals("湿度")) {
                            String value2 = list.get(i).get("value");
                            value2 = value2.substring(0, value2.indexOf("Rh"));
                            sd = Double.parseDouble(value2);
                            if ((sdf.parse(list.get(i).get("date")).getTime() >= time1.getTime())
                                    && (sdf.parse(list.get(i).get("date")).getTime() <= time2.getTime()) && (sd >= d1 && sd <= d2)) {
                                list2.add(list.get(i));
                            }
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            simpleAdapter = new SimpleAdapter(this, list2, R.layout.item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
            gridView.setAdapter(simpleAdapter);
        } else {
            simpleAdapter = new SimpleAdapter(this, list, R.layout.item, new String[]{"date", "type", "value"}, new int[]{R.id.tv_item1, R.id.tv_item2, R.id.tv_item3});
            gridView.setAdapter(simpleAdapter);
        }

    }
}
