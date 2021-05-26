package com.example.newland.testzyjk_0407;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    TextView tv_zy, tv_zt;
    Handler handler;
    Thread thread;
    double zy;
    Modbus4150 modbus4150;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_zt = findViewById(R.id.tv_zt);
        tv_zy = findViewById(R.id.tv_zy);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.7.16", 2001), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.7.16", 2002), null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigBee.getFourEnter();
                    zy = FourChannelValConvert.getNoice(vals[3]);
                    tv_zy.setText("" + df.format(zy) + " dB");

                    if (zy > 50) {
                        tv_zt.setText("喧闹");
                        modbus4150.ctrlRelay(7, true, null);

                    } else if (zy < 50) {
                        tv_zt.setText("安静");
                        modbus4150.ctrlRelay(7, false, null);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
        if(modbus4150!=null)
            modbus4150.stopConnect();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
