package com.example.newland.testdthj0331;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    Thread thread;
    Handler handler;
    TextView tv_wd, tv_sd;
    Button bt_open,bt_close;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);

        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.31.16",2002),null);



        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    zigBee.ctrlDoubleRelay(0001,1,true,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    zigBee.ctrlDoubleRelay(0001,1,false,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                    try {
                        tv_wd.setText(""+df.format(zigBee.getTmpHum()[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        tv_sd.setText(""+df.format(zigBee.getTmpHum()[1]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
