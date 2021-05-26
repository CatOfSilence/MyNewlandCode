package com.example.ss1026_1;

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
    DecimalFormat df = new DecimalFormat("0");
    Zigbee zigbee;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt = findViewById(R.id.bt);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(flag){
                    try {
//                        double[] vals = zigbee.getFourEnter();
                        tv_wd.setText(""+df.format(zigbee.getTmpHum()[0]));
                        tv_sd.setText(""+df.format(zigbee.getTmpHum()[1]));
                        for (double d:zigbee.getTmpHum()) {
                            System.out.println(""+d);
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
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.getText().toString().equals("开始读取")){
                    zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.26.16",951));
                    bt.setText("停止读取");
                    flag = true;
                }else{
                    if(zigbee!=null){
                        zigbee.stopConnect();
                    }
                    bt.setText("开始读取");
                    flag = false;
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