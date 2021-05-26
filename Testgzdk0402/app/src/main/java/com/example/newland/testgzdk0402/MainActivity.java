package com.example.newland.testgzdk0402;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    Modbus4150 modbus4150;
    Handler handler;
    Thread thread;
    TextView tv_light;
    ImageView iv;
    double light;
    boolean flag = true;
    DecimalFormat df = new DecimalFormat("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_light = findViewById(R.id.tv_light);
        iv = findViewById(R.id.iv1);

        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.2.16", 2002), null);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.2.16", 2001), null);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    flag = true;
                    iv.setImageResource(R.drawable.btn_switch_auto);
                } else {
                    flag = false;
                    iv.setImageResource(R.drawable.btn_switch_mt);
                    try {
                        zigBee.ctrlDoubleRelay(0001, 2, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++) ;
                    try {
                        modbus4150.ctrlRelay(1, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try {
                            light = zigBee.getLight();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tv_light.setText("" + df.format(light));
                        if (flag) {
                            if (light < 60) {
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < 1000; i++) ;
                                try {
                                    modbus4150.ctrlRelay(1, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (light > 65) {
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                for (int i = 0; i < 1000; i++) ;
                                try {
                                    modbus4150.ctrlRelay(1, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                iv.setImageResource(R.drawable.btn_switch_mt);
                                flag = false;
                            }
                        }
                    }
                }

        ;
        thread = new

                Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
