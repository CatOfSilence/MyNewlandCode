package com.example.newland.testzigbee0331;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.exeception.SocketException;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd, tv_sd;
    Button bt;
    Handler handler;
    Thread thread;
    boolean flag;
    ZigBee zigBee;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt = findViewById(R.id.bt);

        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.31.16",2002),null);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(flag){
                    try {
                        double vals[] = zigBee.getFourEnter();
                        tv_wd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[0])));
                        tv_sd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[1])));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt.getText().toString().equals("开始读取")) {
                    bt.setText("停止读取");
                    flag = true;
                } else {
                    bt.setText("开始读取");
                    flag = false;
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
