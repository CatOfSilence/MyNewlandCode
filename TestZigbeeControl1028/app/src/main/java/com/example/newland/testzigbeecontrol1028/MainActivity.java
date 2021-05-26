package com.example.newland.testzigbeecontrol1028;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd,tv_gz,tv_info;
    EditText ed_gz1,ed_gz2,ed_wd1,ed_wd2;
    Button bt;
    boolean flag,light_on,light_off,fan_on,fan_off;
    Zigbee zigbee;
    double wd,gz;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_gz = findViewById(R.id.tv_gz);
        tv_info = findViewById(R.id.tv_info);
        ed_gz1 = findViewById(R.id.ed_gz1);
        ed_gz2 = findViewById(R.id.ed_gz2);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        bt = findViewById(R.id.bt);
        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2,38400));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(flag) {
                    try {
                        wd = zigbee.getTmpHum()[0];
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        gz = zigbee.getLight();
                        if (gz < Double.parseDouble(ed_gz1.getText().toString())) {
                            if (light_on) {
                                light_on = false;
                                light_off = true;
                                try {
                                    zigbee.ctrlDoubleRelay(1, 2, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (gz > Double.parseDouble(ed_gz2.getText().toString())) {
                            if (light_off) {
                                light_on = true;
                                light_off = false;
                                try {
                                    zigbee.ctrlDoubleRelay(1, 2, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (wd > Double.parseDouble(ed_wd1.getText().toString())) {
                            if (fan_on) {
                                fan_on = false;
                                fan_off = true;
                                try {
                                    zigbee.ctrlDoubleRelay(1, 1, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (wd < Double.parseDouble(ed_wd2.getText().toString())) {
                            if (fan_off) {
                                fan_on = true;
                                fan_off = false;
                                try {
                                    zigbee.ctrlDoubleRelay(1, 1, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tv_gz.setText("" + df.format(gz));
                    tv_wd.setText("" + df.format(wd));
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.getText().toString().equals("启动监测")){
                    if(ed_gz1.getText().toString().isEmpty()||ed_gz2.getText().toString().isEmpty()||ed_wd1.getText().toString().isEmpty()||ed_wd2.getText().toString().isEmpty()){
                        tv_info.setVisibility(View.VISIBLE);
                    }else{
                        tv_info.setVisibility(View.INVISIBLE);
                        flag = true;
                        light_on = true;
                        light_off = true;
                        fan_off = true;
                        fan_on = true;
                        bt.setText("停止监测");
                    }
                }else{
                    try {
                        zigbee.ctrlDoubleRelay(1,1,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    flag = false;
                    bt.setText("启动监测");
                    tv_gz.setText("");
                    tv_wd.setText("");
                    try {
                        zigbee.ctrlDoubleRelay(1,2,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigbee!=null){
            zigbee.stopConnect();
        }
    }
}
