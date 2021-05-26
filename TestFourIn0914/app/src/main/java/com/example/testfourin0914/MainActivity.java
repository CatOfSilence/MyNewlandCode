package com.example.testfourin0914;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity{
    TextView tv_wd,tv_sd;
    Button bt;
    Zigbee zigbee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_wd = findViewById(R.id.tv_wendu);
        tv_sd = findViewById(R.id.tv_shidu);
        bt = findViewById(R.id.bt);

        DecimalFormat df = new DecimalFormat("0.0");
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    double[] vals = zigbee.getFourEnter();
                    tv_wd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[0])));
                    tv_sd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[1])));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.15.16",952));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.getText().toString().equals("开始读取")){
                    thread.start();
                    bt.setText("停止读取");
                }else{
                    thread.interrupt();
                    bt.setText("开始读取");
                }
            }
        });
    }
}