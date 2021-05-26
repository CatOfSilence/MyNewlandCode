package com.example.testzigbeecontrol1015;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd, tv_gz, tv_info;
    EditText ed_gz1, ed_gz2, ed_wd1, ed_wd2;
    double gz, wd;
    boolean flag, light_on, light_off, fan_on, fan_off;
    Button bt;
    Zigbee zigbee;
    DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2, 38400));
        tv_wd = findViewById(R.id.tv_wd);
        tv_gz = findViewById(R.id.tv_gz);
        tv_info = findViewById(R.id.tv_info);
        ed_gz1 = findViewById(R.id.ed_gz1);
        ed_gz2 = findViewById(R.id.ed_gz2);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("启动监测")) {
                    if (ed_gz1.getText().toString().isEmpty() || ed_gz2.getText().toString().isEmpty() || ed_wd1.getText().toString().isEmpty() || ed_wd2.getText().toString().isEmpty()) {
                        tv_info.setVisibility(View.VISIBLE);
                    } else {
                        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2, 38400));
                        tv_info.setVisibility(View.INVISIBLE);
                        bt.setText("停止监测");
                        flag = true;
                        light_on = true;
                        light_off = true;
                        fan_on = true;
                        fan_off = true;
                    }
                } else {
                    try {
                        zigbee.ctrlDoubleRelay(0001, 1, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    bt.setText("启动监测");
                    flag = false;
                    tv_wd.setText("");
                    tv_gz.setText("");
                    try {
                        zigbee.ctrlDoubleRelay(0001, 2, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    if (true) {
//                        gz = zigbee.getLight();
//                        wd = zigbee.getTmpHum()[0];
                        tv_wd.setText("" + df.format(wd));
                        tv_gz.setText("" + df.format(gz));
                        if (gz < Double.parseDouble(ed_gz1.getText().toString())) {
                            if (light_on) {
                                zigbee.ctrlDoubleRelay(0001, 2, true, null);
                                light_on = false;
                                light_off = true;
                            }
                        } else if (gz > Double.parseDouble(ed_gz2.getText().toString())) {
                            if (light_off) {
                                zigbee.ctrlDoubleRelay(0001, 2, false, null);
                                light_on = true;
                                light_off = false;
                            }
                        }
                        if (wd > Double.parseDouble(ed_wd1.getText().toString())) {
                            if (fan_on) {
                                zigbee.ctrlDoubleRelay(0001, 1, true, null);
                                fan_on = false;
                                fan_off = true;
                            }
                        } else if (wd < Double.parseDouble(ed_wd2.getText().toString())) {
                            if (fan_off) {
                                zigbee.ctrlDoubleRelay(0001, 1, false, null);
                                fan_on = true;
                                fan_off = false;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if(flag) {
                        try {
                            Thread.sleep(500);
                            handler.sendEmptyMessage(0);
                            wd = zigbee.getTmpHum()[0];
                            gz = zigbee.getLight();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
    }
}