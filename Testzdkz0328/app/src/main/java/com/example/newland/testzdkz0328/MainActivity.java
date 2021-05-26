package com.example.newland.testzdkz0328;

import android.annotation.SuppressLint;
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
    Handler handler;
    Zigbee zigbee;
    Thread thread;
    TextView tv_light, tv_wd, tv_error;
    boolean flag;
    double wd,light;
    Button bt;
    EditText ed_light1, ed_light2, ed_wd1, ed_wd2;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_light = findViewById(R.id.tv_light);
        tv_wd = findViewById(R.id.tv_wd);
        ed_light1 = findViewById(R.id.ed_light1);
        ed_light2 = findViewById(R.id.ed_light2);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        bt = findViewById(R.id.button);
        tv_error = findViewById(R.id.tv_error);


        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2, 38400));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("启动监测")) {
                    if (ed_light1.getText().toString().isEmpty() || ed_light2.getText().toString().isEmpty() || ed_wd1.getText().toString().isEmpty() || ed_wd2.getText().toString().isEmpty()) {
                        tv_error.setVisibility(View.VISIBLE);
                    } else {
                        bt.setText("停止监测");
                        flag = true;
                        tv_error.setVisibility(View.INVISIBLE);
                    }
                } else {
                    bt.setText("启动监测");
                    flag = false;
                    tv_wd.setText("");
                    tv_light.setText("");
                    try {
                        zigbee.ctrlDoubleRelay(0001,1,false,null);
                        for (int i = 0; i < 5000; i++);
                        zigbee.ctrlDoubleRelay(0001,2,false,null);
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
                if (flag) {
                    try {
                        light = zigbee.getLight();
                        tv_light.setText(""+decimalFormat.format(light));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        wd = zigbee.getTmpHum()[0];
                        tv_wd.setText(""+decimalFormat.format(wd));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(light < Double.parseDouble(ed_light1.getText().toString())){
                            try {
                                zigbee.ctrlDoubleRelay(0001,2,true,null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(light > Double.parseDouble(ed_light2.getText().toString())){
                            try {
                                zigbee.ctrlDoubleRelay(0001,2,false,null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if(wd>Double.parseDouble(ed_wd1.getText().toString())){
                            try {
                                zigbee.ctrlDoubleRelay(0001,1,true,null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(wd<Double.parseDouble(ed_wd2.getText().toString())){
                            try {
                                zigbee.ctrlDoubleRelay(0001,1,false,null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
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
        if(zigbee!=null)
            zigbee.stopConnect();
    }
}
