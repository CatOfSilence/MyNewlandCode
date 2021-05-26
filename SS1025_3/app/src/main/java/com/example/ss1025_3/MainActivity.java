package com.example.ss1025_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd,tv_sd,tv_rt,tv_yw;
    Button bt_open,bt_close;
    Zigbee zigbee;
    Modbus4150 modbus4150;
    int yw = 0;
    DecimalFormat df = new DecimalFormat("0.0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_rt = findViewById(R.id.tv_rt);
        tv_yw = findViewById(R.id.tv_yw);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);

        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.25.16",951));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.25.16",950));
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText(""+ df.format(zigbee.getTmpHum()[0]));
                    tv_sd.setText(""+ df.format(zigbee.getTmpHum()[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {

                    modbus4150.getVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            tv_rt.setText(""+val);
                        }
                    });

                    modbus4150.getVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            tv_yw.setText(""+val);
                            yw = val;
                        }
                    });

                    if(yw == 1){
                        modbus4150.openRelay(7,null);
                    }else if(yw == 0){
                        modbus4150.closeRelay(7,null);
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
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(1,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    modbus4150.closeRelay(1,null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(modbus4150!=null){
            modbus4150.stopConnect();
        }
        if(zigbee!=null){
            zigbee.stopConnect();
        }
    }
}