package com.example.newland.testzndyy0404;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class IndexActivity extends AppCompatActivity {
    TextView tv_co2;
    ZigBee zigBee;
    Handler handler;
    Thread thread;
    boolean fan,lamp;
    ImageView iv_lamp,iv_fan;
    Modbus4150 modbus4150;
    AnimationDrawable animationDrawable;
    double x = 0;
    DecimalFormat df = new DecimalFormat("0.00");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        tv_co2 = findViewById(R.id.tv_co2);
        iv_fan = findViewById(R.id.iv_fan);
        iv_lamp = findViewById(R.id.iv_lamp);

        iv_fan.setBackgroundResource(R.drawable.fan);
        animationDrawable = (AnimationDrawable) iv_fan.getBackground();

        iv_lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lamp){
                    lamp = false;
                    iv_lamp.setImageResource(R.drawable.lamp_off);
                    try {
                        zigBee.ctrlDoubleRelay(0001,2,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        modbus4150.ctrlRelay(1,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    lamp = true;
                    iv_lamp.setImageResource(R.drawable.lamp_on);

                    try {
                        zigBee.ctrlDoubleRelay(0001,2,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        modbus4150.ctrlRelay(1,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        iv_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fan){
                    fan = false;
                    animationDrawable.stop();
                    try {
                        zigBee.ctrlDoubleRelay(0001,1,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        modbus4150.ctrlRelay(0,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    fan = true;
                    animationDrawable.start();

                    try {
                        zigBee.ctrlDoubleRelay(0001,1,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        modbus4150.ctrlRelay(0,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.4.16", 2005), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.19.4.16", 2006), null);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigBee.getFourEnter();
                    x = FourChannelValConvert.getTemperature(vals[2]);
                    tv_co2.setText(""+df.format(x));
                    if(x > 10){
                        try {
                            zigBee.ctrlDoubleRelay(0001,1,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++);
                        try {
                            modbus4150.ctrlRelay(0,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < 1000; i++);
                        try {
                            zigBee.ctrlDoubleRelay(0001,2,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++);
                        try {
                            modbus4150.ctrlRelay(1,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread = new Thread(new Runnable() {
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

    @Override
    protected void onStop() {
        super.onStop();
        System.out.println("Index关闭");
        if(zigBee!=null)
            zigBee.stopConnect();
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }
}
