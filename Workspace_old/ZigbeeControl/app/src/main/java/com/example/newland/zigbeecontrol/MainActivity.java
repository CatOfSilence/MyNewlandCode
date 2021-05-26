package com.example.newland.zigbeecontrol;

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

public class MainActivity extends AppCompatActivity {

    TextView tv_light, tv_temp, tv_info;
    EditText ed_light1, ed_light2;
    EditText ed_temp1, ed_temp2;
    Button bt_start;
    boolean flag, flag_light_on, flag_light_off, flag_temp_on, flag_temp_off;
    Zigbee zigbee;
    double light, temp, yu_light1, yu_temp1, yu_light2, yu_temp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_light = findViewById(R.id.tv_light);
        tv_temp = findViewById(R.id.tv_temp);
        ed_light1 = findViewById(R.id.ed_light1);
        ed_light2 = findViewById(R.id.ed_light2);
        ed_temp1 = findViewById(R.id.ed_temp1);
        ed_temp2 = findViewById(R.id.ed_temp2);
        tv_info = findViewById(R.id.info);
        bt_start = findViewById(R.id.bt_start);

        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("192.168.1.111", 953));
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {
                    try {
                        light = zigbee.getLight();
                        temp = zigbee.getTmpHum()[0];
                        tv_light.setText("" + light);
                        tv_temp.setText("" + temp);
                        if (yu_light1 > light) {
                            if (flag_light_on) {
                                zigbee.ctrlDoubleRelay(0001, 2, true, null);
                                flag_light_on = false;
                                flag_light_off = true;
                            }
                        } else if (yu_light2 < light) {
                            if (flag_light_off) {
                                zigbee.ctrlDoubleRelay(0001, 2, false, null);
                                flag_light_on = true;
                                flag_light_off = false;
                            }
                        }
                        if (yu_temp1 < temp) {
                            if (flag_temp_on) {
                                zigbee.ctrlDoubleRelay(0001, 1, true, null);
                                flag_temp_on = false;
                                flag_light_off = true;
                            }
                        } else if (yu_temp2 > temp) {
                            if (flag_temp_off) {
                                zigbee.ctrlDoubleRelay(0001, 1, false, null);
                                flag_temp_on = true;
                                flag_light_off = false;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_start.getText().toString().equals("启动监测")) {
                    if (ed_light1.getText().toString().isEmpty() || ed_light2.getText().toString().isEmpty() ||
                            ed_temp1.getText().toString().isEmpty() || ed_temp2.getText().toString().isEmpty()) {
                        tv_info.setVisibility(View.VISIBLE);
                    } else {
                        yu_light1 = Double.parseDouble(ed_light1.getText().toString());
                        yu_light2 = Double.parseDouble(ed_light2.getText().toString());
                        yu_temp1 = Double.parseDouble(ed_temp1.getText().toString());
                        yu_temp2 = Double.parseDouble(ed_temp2.getText().toString());
                        tv_info.setVisibility(View.GONE);
                        bt_start.setText("停止监测");
                        flag = true;
                        flag_light_on = true;
                        flag_light_off = true;
                        flag_temp_on = true;
                        flag_temp_off = true;
                    }
                } else {
                    bt_start.setText("启动监测");
                    tv_light.setText("");
                    tv_temp.setText("");
                    flag = false;
                    flag_light_off = false;
                    flag_light_on = false;
                    flag_temp_on = false;
                    flag_temp_off = false;
                    try {
                        zigbee.ctrlDoubleRelay(0001, 1, false, null);
                        zigbee.ctrlDoubleRelay(0001, 2, false, null);
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
        if (zigbee != null) {
            zigbee.stopConnect();
        }
    }
}
