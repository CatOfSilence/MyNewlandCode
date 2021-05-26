package com.example.testzigbee0911;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    DecimalFormat df = new DecimalFormat("0.0");
    TextView tv_4wd, tv_4sd, tv_4co2, tv_4zy;
    TextView tv_wd, tv_sd, tv_gz, tv_hy;
    Button bt_light, bt_fan;
    Zigbee zigbee;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getVals();
        control();
    }

    public void init() {
        tv_4wd = findViewById(R.id.tv_4getwendu);
        tv_4sd = findViewById(R.id.tv_4getshidu);
        tv_4co2 = findViewById(R.id.tv_4getco2);
        tv_4zy = findViewById(R.id.tv_4getzaoyin);
        tv_wd = findViewById(R.id.tv_getwendu);
        tv_sd = findViewById(R.id.tv_getshidu);
        tv_gz = findViewById(R.id.tv_getguangzhao);
        tv_hy = findViewById(R.id.tv_gethuoyan);
        bt_fan = findViewById(R.id.bt_fan);
        bt_light = findViewById(R.id.bt_light);
        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.15.16",952));
    }
    public void getVals() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    double[] vals = zigbee.getFourEnter();
                    tv_4wd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[0])));
                    tv_4sd.setText(""+df.format(FourChannelValConvert.getHumidity(vals[1])));
                    tv_4co2.setText(""+df.format(FourChannelValConvert.getCO2(vals[2])));
                    tv_4zy.setText(""+df.format(FourChannelValConvert.getNoice(vals[3])));
                    tv_wd.setText(""+df.format(zigbee.getTmpHum()[0]));
                    tv_sd.setText(""+df.format(zigbee.getTmpHum()[1]));
                    tv_gz.setText(""+zigbee.getLight());
                    tv_hy.setText(""+df.format(zigbee.getFire()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
    }
    public void control(){
        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt_light.getText().toString().equals("灯泡：开")){
                    bt_light.setText("灯泡：关");
                    try {
                        zigbee.ctrlDoubleRelay(0001,1,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    bt_light.setText("灯泡：开");
                    try {
                        zigbee.ctrlDoubleRelay(0001,1,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bt_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bt_fan.getText().toString().equals("风扇：开")){
                    bt_fan.setText("风扇：关");
                    try {
                        zigbee.ctrlDoubleRelay(0001,0,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    bt_fan.setText("风扇：开");
                    try {
                        zigbee.ctrlDoubleRelay(0001,0,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
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