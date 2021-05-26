package com.example.ss2_1023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.RFIDReadListener;
import com.nle.mylibrary.forUse.rfid.RFIDWriteListener;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_card;
    Modbus4150 modbus4150;
    RFID rfid;
    boolean flag = false;
    String card = "";
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_read);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.23.16", 950));
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.23.16", 952));
        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                tv_card.setText(card);
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!flag) {
                            rfid.readSingleEpc(new SingleEpcListener() {
                                @Override
                                public void onVal(String val) {
                                    i = 0;
                                    card = val;
                                    handler.sendEmptyMessage(0);
                                    flag = true;
                                }
                            });
                        }
                        if (flag) {
                            modbus4150.closeRelay(2, null);
                            Thread.sleep(100);
                            modbus4150.openRelay(3, null);
                            Thread.sleep(3000);
                            modbus4150.closeRelay(3, null);
                            Thread.sleep(100);
                            modbus4150.openRelay(2, null);
                            Thread.sleep(100);
                            modbus4150.openRelay(7, null);
                            Thread.sleep(3000);
                            modbus4150.closeRelay(7, null);
                            Thread.sleep(100);
                            flag = false;
                        }
                        if(i==3){
                            card = "";
                            handler.sendEmptyMessage(0);
                        }
                        i++;
                        Thread.sleep(2000);
                    } catch (Exception e) {
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
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
        if (rfid != null) {
            rfid.stopConnect();
        }
    }
}