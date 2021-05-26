package com.example.ss1026_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    RFID rfid;
    Modbus4150 modbus4150;
    TextView tv_card;
    int i = 0,type = 3;
    String card = "无卡";
    boolean flag = false;
    Animation animation;
    Button vvv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);
        vvv = findViewById(R.id.vvv);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.19.16",952));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.19.16",950));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if(type==3){
                    tv_card.setText(card);
                }
                if(type == 0) {
                    animation = new ScaleAnimation(1,0.5f,1,1);
                    animation.setDuration(3000);
                    animation.setFillAfter(true);
                    vvv.startAnimation(animation);

                }
                if(type == 1){
                    animation = new ScaleAnimation(0.5f,1,1,1);
                    animation.setDuration(3000);
                    animation.setFillAfter(true);
                    vvv.startAnimation(animation);
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        if(flag==false) {
                            rfid.readSingleEpc(new SingleEpcListener() {
                                @Override
                                public void onVal(String val) {
                                    i = 0;
                                    type = 3;
                                    card = "有卡";
                                    flag = true;
                                    handler.sendEmptyMessage(0);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag){
                        try {
                            type = 0;
                            handler.sendEmptyMessage(0);
                            Thread.sleep(100);
                            modbus4150.closeRelay(2,null);
                            Thread.sleep(100);
                            modbus4150.openRelay(3,null);
                            Thread.sleep(3000);
                            type = 1;
                            handler.sendEmptyMessage(0);
                            Thread.sleep(100);
                            modbus4150.closeRelay(3,null);
                            Thread.sleep(100);
                            modbus4150.openRelay(2,null);
                            Thread.sleep(100);
                            modbus4150.openRelay(7,null);
                            Thread.sleep(3000);
                            modbus4150.closeRelay(7,null);
                            Thread.sleep(100);
                            flag = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(i==3){
                        card = "无卡";
                        handler.sendEmptyMessage(0);
                    }
                    i++;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}