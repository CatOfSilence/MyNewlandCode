package com.example.newland.testdthj0319;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Zigbee zigbee;
    Modbus4150 modbus4150;
    TextView tv_wd,tv_sd,tv_rt,tv_yw;
    Button bt1,bt2;
    boolean flag;
    int x = 2;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.19.16",951));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.19.16",950));

        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_rt = findViewById(R.id.tv_rt);
        tv_yw = findViewById(R.id.tv_yw);
        bt1 = findViewById(R.id.button);
        bt2 = findViewById(R.id.button2);



        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(1,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.closeRelay(1,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText("温度："+df.format(zigbee.getTmpHum()[0])+" ℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_sd.setText("湿度："+df.format(zigbee.getTmpHum()[1])+" Rh");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modbus4150.getVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if (val == 0){
                                tv_rt.setText("人体：有人");
                            }else{
                                tv_rt.setText("人体：无人");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {

                    modbus4150.getVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if (val == 0){
                                tv_yw.setText("烟雾：无烟");
                                if(!flag&&(x == 1||x == 2)){
                                    flag = true;
                                    x = 0;
                                }
                            }else{
                                tv_yw.setText("烟雾：有烟");
                                if(!flag&&(x == 0||x == 2)){
                                    flag = true;
                                    x = 1;
                                }
                            }
                        }
                    });
                    if(flag&&x == 1){
                        modbus4150.openRelay(7,null);
                        flag = false;
                    }else if(flag&&x == 0){
                        modbus4150.closeRelay(7,null);
                        flag = false;
                    }
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
        if(zigbee!=null){
            zigbee.stopConnect();
        }
        if(modbus4150!=null){
            modbus4150.stopConnect();
        }
    }
}
