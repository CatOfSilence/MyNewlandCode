package com.example.newland.testzigbee0308;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd,tv_sd;
    Button bt;
    Zigbee zigbee;
    boolean flag;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        bt = findViewById(R.id.bt);
        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("192.168.1.1",950));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt.getText().toString().equals("开始读取")){
                    zigbee = new Zigbee(DataBusFactory.newSocketDataBus("192.168.1.1",950));
                    bt.setText("停止读取");
                    flag = true;
                }else{
                    bt.setText("开始读取");
                    flag = false;
                    if(zigbee!=null){
                        zigbee.stopConnect();
                    }
                }
            }
        });

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(flag){
                    try {
                        double[] vals = zigbee.getFourEnter();
                        tv_wd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[0])));
                        tv_sd.setText(""+df.format(FourChannelValConvert.getHumidity(vals[1])));
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigbee!=null){
            zigbee.stopConnect();
        }
    }
}
