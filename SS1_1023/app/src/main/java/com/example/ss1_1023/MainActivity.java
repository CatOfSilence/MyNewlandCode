package com.example.ss1_1023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd,tv_sd;
    Button bt;
    boolean flag;
    Zigbee zigbee;
    double[] zhi;
    DecimalFormat df = new DecimalFormat("0");
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
                if(bt.getText().toString().equals("开始读取")){
                    bt.setText("停止读取");
                    flag = true;
                    zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.23.16",951));
                }else{
                    bt.setText("开始读取");
                    flag = false;
                    zigbee.stopConnect();
                }
            }
        });
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(flag==true){
                    try {
                        zhi = zigbee.getFourEnter();
                        tv_wd.setText(""+df.format(FourChannelValConvert.getTemperature(zhi[0])));
                        tv_sd.setText(""+df.format(FourChannelValConvert.getHumidity(zhi[1])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
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
        {
            zigbee.stopConnect();
        }
    }
}