package com.example.newland.testhjjc0405;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd,tv_sd,tv_co2,tv_light;
    DecimalFormat df = new DecimalFormat("0");
    ZigBee zigBee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_co2 = findViewById(R.id.tv_co2);
        tv_light = findViewById(R.id.tv_light);

        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.5.16",2002),null);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigBee.getFourEnter();
                    tv_co2.setText("空气质量："+df.format(FourChannelValConvert.getTemperature(vals[2])));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_sd.setText("湿度："+df.format(zigBee.getTmpHum()[1])+"Rh");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_wd.setText("温度："+df.format(zigBee.getTmpHum()[0])+"℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_light.setText("光照数据："+df.format(zigBee.getFire())+"Lx");
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
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
