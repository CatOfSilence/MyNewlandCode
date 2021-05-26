package com.example.testzigbeecontrol0929;

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
    TextView tv_wd,tv_gz,tv_cw;
    EditText ed_wd1,ed_wd2,ed_gz1,ed_gz2;
    Button bt;
    Zigbee zigbee;
    boolean light_off,light_on,fan_on,fan_off,flag;
    double wd,gz,wd1,wd2,gz1,gz2;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_gz = findViewById(R.id.tv_gz);
        tv_cw = findViewById(R.id.tv_cw);
        bt = findViewById(R.id.bt);
        ed_wd1 = findViewById(R.id.ed_wd1);
        ed_wd2 = findViewById(R.id.ed_wd2);
        ed_gz1 = findViewById(R.id.ed_gz1);
        ed_gz2 = findViewById(R.id.ed_gz2);
        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2,38400));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    wd = zigbee.getTmpHum()[0];
                    gz = zigbee.getLight();
                    tv_gz.setText(""+gz);
                    tv_wd.setText(""+df.format(wd));

                    if(gz < gz1){
                        if(light_on){
                            light_off = true;
                            light_on = false;
                            zigbee.ctrlDoubleRelay(0001,1,true,null);
                        }
                    }else if(gz > gz2){
                       if(light_off){
                            light_off = false;
                            light_on = true;
                            zigbee.ctrlDoubleRelay(0001,1,false,null);
                        }
                    }
                    if(wd > wd1){
                        if(fan_on){
                            fan_off = true;
                            fan_on = false;
                            zigbee.ctrlDoubleRelay(0001,0,true,null);
                        }
                    }else if(wd < wd2){
                        if(light_off){
                            fan_off = false;
                            fan_on = true;
                            zigbee.ctrlDoubleRelay(0001,0,false,null);
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
                while (flag){
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(300);
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
                if(ed_gz1.getText().length()==0||ed_gz2.getText().length()==0||ed_wd1.getText().length()==0||ed_wd2.getText().length()==0){
                    tv_cw.setVisibility(View.VISIBLE);
                }else{
                    tv_cw.setVisibility(View.INVISIBLE);
                    if(bt.getText().toString().equals("启动监测")){
                        bt.setText("停止监测");
                        flag = true;
                        light_off = true;
                        light_on = true;
                        fan_off = true;
                        fan_on = true;
                        gz1 = Double.parseDouble(ed_gz1.getText().toString());
                        gz2 = Double.parseDouble(ed_gz2.getText().toString());
                        wd1 = Double.parseDouble(ed_wd1.getText().toString());
                        wd2 = Double.parseDouble(ed_wd2.getText().toString());
                    }else{
                        bt.setText("启动监测");
                        flag = false;
                        tv_gz.setText("");
                        tv_wd.setText("");
                        try {
                            zigbee.ctrlDoubleRelay(0001,1,false,null);
                            tv_gz.setText("");
                            tv_wd.setText("");
                            zigbee.ctrlDoubleRelay(0001,0,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
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