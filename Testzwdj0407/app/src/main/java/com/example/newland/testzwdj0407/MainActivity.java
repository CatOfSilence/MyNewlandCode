package com.example.newland.testzwdj0407;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd, tv_light, tv_info;
    DecimalFormat df = new DecimalFormat("0");
    ZigBee zigBee;
    double wd, light;
    Button bt;
    boolean flag;
    EditText ed_light1, ed_light2, ed_wd1, ed_wd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_light = findViewById(R.id.tv_light);
        bt = findViewById(R.id.bt_open);
        tv_info = findViewById(R.id.tv_info);
        ed_light1 = findViewById(R.id.ed_light1);
        ed_light2 = findViewById(R.id.ed_light2);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("启动监测")) {
                    if (!ed_light1.getText().toString().isEmpty() || !ed_light2.getText().toString().isEmpty() || !ed_wd1.getText().toString().isEmpty() || !ed_wd2.getText().toString().isEmpty()) {
                        tv_info.setVisibility(View.INVISIBLE);
                        flag = true;
                        bt.setText("停止监测");
                    } else {
                        tv_info.setVisibility(View.VISIBLE);
                    }
                } else {
                    bt.setText("启动监测");
                    flag = false;
                    tv_light.setText("");
                    tv_wd.setText("");
                    try {
                        zigBee.ctrlDoubleRelay(0001, 1, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++) ;
                    try {
                        zigBee.ctrlDoubleRelay(0001, 2, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.21.7.16", 2002), null);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {
                    try {
                        wd = zigBee.getTmpHum()[0];
                        tv_wd.setText("" + df.format(wd));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        light = zigBee.getLight();
                        tv_light.setText("" + df.format(light));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (light < Double.parseDouble(ed_light1.getText().toString())) {
                        try {
                            zigBee.ctrlDoubleRelay(0001, 2, true, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (light > Double.parseDouble(ed_light2.getText().toString())) {
                        try {
                            zigBee.ctrlDoubleRelay(0001, 2, false, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (wd > Double.parseDouble(ed_wd1.getText().toString())) {
                        try {
                            zigBee.ctrlDoubleRelay(0001, 1, true, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (wd < Double.parseDouble(ed_wd2.getText().toString())) {
                        try {
                            zigBee.ctrlDoubleRelay(0001, 1, false, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

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
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
