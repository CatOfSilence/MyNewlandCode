package com.example.newland.testhjjk0417;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    TextView tv_wd,tv_light;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.21.17.16", 2002), null);
        tv_light = findViewById(R.id.tv_light);
        tv_wd = findViewById(R.id.tv_wd);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText("温度值："+df.format(zigBee.getTmpHum()[0])+"℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_light.setText("光照值："+df.format(zigBee.getLight())+"Lx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
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
}
