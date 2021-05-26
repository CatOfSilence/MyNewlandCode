package com.example.testzigbeecontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
TextView tv_wd,tv_gz,tv_info;
Zigbee zigbee;
Button bt;
double gz,wd;
boolean flag,light_on,light_off,fan_on,fan_off;
EditText ed_gz1,ed_gz2,ed_wd1,ed_wd2;
DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_gz = findViewById(R.id.tv_gz);
        tv_info = findViewById(R.id.tv_info);
        ed_gz1 = findViewById(R.id.ed_gz1);
        ed_gz2 = findViewById(R.id.ed_gz2);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        bt = findViewById(R.id.bt);

        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2,38400));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(flag){
                    try {
                        gz = zigbee.getLight();
                        wd = zigbee.getTmpHum()[0];
                        tv_wd.setText(""+df.format(wd));
                        tv_gz.setText(""+df.format(gz));
                        if (gz < Double.parseDouble(ed_gz1.getText().toString())){
                            if(light_on){
                                light_on = false;
                                light_off = true;
                                zigbee.ctrlDoubleRelay(0001,2,true,null);
                            }
                        }else if(gz > Double.parseDouble(ed_gz2.getText().toString())){
                            if(light_off){
                                light_on = true;
                                light_off = false;
                                zigbee.ctrlDoubleRelay(0001,2,false,null);
                            }
                        }
                        if (wd > Double.parseDouble(ed_wd1.getText().toString())){
                            if(fan_on){
                                fan_on = false;
                                fan_off = true;
                                zigbee.ctrlDoubleRelay(0001,1,true,null);
                            }
                        }else if(wd < Double.parseDouble(ed_wd2.getText().toString())){
                            if(fan_off){
                                fan_on = true;
                                fan_off = false;
                                zigbee.ctrlDoubleRelay(0001,1,false,null);
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
                if(bt.getText().toString().equals("启动监测")){
                    if(ed_gz1.getText().toString().isEmpty()||ed_gz2.getText().toString().isEmpty()||ed_wd1.getText().toString().isEmpty()||ed_wd2.getText().toString().isEmpty()){
                        tv_info.setVisibility(View.VISIBLE);
                    }else{
                        try {
                            zigbee.ctrlDoubleRelay(0001,1,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tv_info.setVisibility(View.INVISIBLE);
                        bt.setText("停止监测");
                        flag = true;
                        light_on = true;
                        light_off = true;
                        fan_on = true;
                        fan_off = true;
                        tv_wd.setText("");
                        tv_gz.setText("");
                        try {
                            zigbee.ctrlDoubleRelay(0001,2,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    bt.setText("启动监测");
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