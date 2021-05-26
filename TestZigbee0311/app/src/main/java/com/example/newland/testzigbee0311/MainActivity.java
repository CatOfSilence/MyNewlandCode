package com.example.newland.testzigbee0311;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd, tv_sd;
    DecimalFormat df = new DecimalFormat("0");
    Handler handler;
    Thread thread;
    boolean flag;
    Button bt;
    Zigbee zigbee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt = findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("开始读取")) {
                    zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.10.16", 951));
                    flag = true;
                    bt.setText("停止读取");
                } else {
                    zigbee.stopConnect();
                    flag = false;
                    bt.setText("开始读取");
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {

                    try {
                        double[] vals = zigbee.getFourEnter();
                        tv_wd.setText("" + df.format(FourChannelValConvert.getTemperature(vals[0])));
                        tv_sd.setText("" + df.format(FourChannelValConvert.getTemperature(vals[1])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread = new Thread(new Runnable() {
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
}
