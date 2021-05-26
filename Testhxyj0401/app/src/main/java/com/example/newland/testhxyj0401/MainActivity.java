package com.example.newland.testhxyj0401;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    Zigbee zigbee;
    Handler handler;
    Thread thread;
    ImageView iv_open,iv_close,iv_bjd;
    TextView tv_fire;
    boolean flag;
    Modbus4150 modbus4150;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_open = findViewById(R.id.iv_open);
        iv_close = findViewById(R.id.iv_close);
        iv_bjd = findViewById(R.id.iv_bjd);
        tv_fire = findViewById(R.id.tv_fire);


        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.1.16",951));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.1.16", 950));
        iv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                tv_fire.setVisibility(View.VISIBLE);
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                tv_fire.setVisibility(View.INVISIBLE);
                iv_bjd.setImageResource(R.drawable.deng_off);
                try {
                    modbus4150.closeRelay(7,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(flag){
                    try {
                        if(zigbee.getFire()==1){
                            tv_fire.setText("有火焰");
                            iv_bjd.setImageResource(R.drawable.deng_on);
                            modbus4150.openRelay(7,null);
                        }else{
                            tv_fire.setText("无火焰");
                            iv_bjd.setImageResource(R.drawable.deng_off);
                            modbus4150.closeRelay(7,null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        if(zigbee!=null)
            zigbee.stopConnect();
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }
}
