package com.example.newland.fourin;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;


public class MainActivity extends AppCompatActivity {

    TextView tv_temp,tv_hum;
    Button bt_start;
    Zigbee zigbee;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_temp = findViewById(R.id.tv_temp);
        tv_hum = findViewById(R.id.tv_hum);
        bt_start = findViewById(R.id.bt_start);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if(flag) {
                        double vals[] = zigbee.getFourEnter();
                        tv_temp.setText("" + FourChannelValConvert.getTemperature(vals[0]));
                        tv_hum.setText("" + FourChannelValConvert.getHumidity(vals[1]));
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
                    try {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_start.getText().toString().equals("开始读取")){
                    flag = true;
                    bt_start.setText("停止读取");
                    zigbee = new Zigbee(DataBusFactory.newSocketDataBus("192.168.1.111",953));
                }else{
                    flag = false;
                    bt_start.setText("开始读取");
                    zigbee.stopConnect();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigbee != null){
            zigbee.stopConnect();
        }
    }
}
