package com.example.newland.testzdkz0407;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GridView gridView;
    SimpleAdapter simpleAdapter;
    List<HashMap<String, String>> list;
    Myhttp conn = new Myhttp();
    double wd;
    TextView tv_wd;
    ImageView iv_fan,iv_set;
    Modbus4150 modbus4150;
    ZigBee zigBee;
    boolean fs, fs_sw;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridview);

        list = new ArrayList();
        iv_set = findViewById(R.id.iv_set);
        iv_fan = findViewById(R.id.iv_fan);
        tv_wd = findViewById(R.id.tv_wd);


        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.21.7.16", 2001), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.21.7.16", 2002), null);

        iv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SetActivity.class);
                startActivity(intent);
            }
        });
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(MyData.dqpl);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String json = "{'Account':'"+MyData.username+"','Password':'"+MyData.password+"','IsRememberMe':'true'}";
                try {

                    JSONObject js = new JSONObject(conn.login(json));
                    JSONObject ResultObj = js.getJSONObject("ResultObj");
                    Myhttp.Token += ResultObj.getString("AccessToken");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_wd.setText("" + wd);
                if (wd > 29) {
                    iv_fan.setImageResource(R.drawable.co_fan_open);
                    try {
                        modbus4150.ctrlRelay(MyData.fsio, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!fs && !fs_sw) {
                        fs_sw = true;
                    }
                    fs = true;
                } else if (wd < 30) {
                    iv_fan.setImageResource(R.drawable.co_fan_close);
                    try {
                        modbus4150.ctrlRelay(MyData.fsio, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (fs && !fs_sw) {
                        fs_sw = true;
                    }
                    fs = false;
                }
                if (fs_sw) {
                    fs_sw = false;
                    if (fs) {
                        String time = sdf.format(new Date());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("a", time);
                        list.add(map);
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("a", "开启");
                        list.add(map1);
                        simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.item, new String[]{"a"}, new int[]{R.id.tv_time});
                        gridView.setAdapter(simpleAdapter);
                        gridView.setSelection(list.size() - 1);

                    } else {
                        String time = sdf.format(new Date());
                        HashMap<String, String> map = new HashMap<>();
                        map.put("a", time);
                        list.add(map);
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("a", "关闭");
                        list.add(map1);
                        simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.item, new String[]{"a"}, new int[]{R.id.tv_time});
                        gridView.setAdapter(simpleAdapter);
                        gridView.setSelection(list.size() - 1);
                    }
                }
            }
        };
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(1000);
                        try {
                            JSONObject js = null;
                            String result = conn.getData();
                            if (result.length() > 0) {
                                js = new JSONObject(result);
                                JSONObject ResultObj = js.getJSONObject("ResultObj");
                                wd = ResultObj.getDouble("Value");
                                System.out.println(wd);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (modbus4150 != null)
            modbus4150.stopConnect();
        if (zigBee != null)
            zigBee.stopConnect();
    }
}
