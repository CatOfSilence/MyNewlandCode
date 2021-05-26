package com.example.newland.testzndm0329;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_card;
    RFID rfid;
    Modbus4150 modbus4150;
    Handler handler;
    Thread thread;
    boolean flag;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);

        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.29.16",2003),null);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.29.16",2001),null);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_card.setText(str);
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!flag){
                        try {
                            rfid.readSingleEpc(new SingleEpcListener() {
                                @Override
                                public void onVal(String s) {
                                    str = s;
                                    handler.sendEmptyMessage(
                                            0);
                                    flag = true;
                                }

                                @Override
                                public void onFail(Exception e) {

                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else{
                        try {
                            modbus4150.ctrlRelay(2,false,null);
                            for (int i = 0; i < 1000; i++);
                            modbus4150.ctrlRelay(3,true,null);
                            for (int i = 0; i < 1000; i++);
                            modbus4150.ctrlRelay(7,true,null);
                            Thread.sleep(3000);
                            modbus4150.ctrlRelay(3,false,null);
                            for (int i = 0; i < 1000; i++);
                            modbus4150.ctrlRelay(2,true,null);
                            for (int i = 0; i < 1000; i++);
                            modbus4150.ctrlRelay(7,false,null);
                            str = "";
                            handler.sendEmptyMessage(0);
                            flag = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
        if(rfid!=null)
            rfid.stopConnect();
    }
}
