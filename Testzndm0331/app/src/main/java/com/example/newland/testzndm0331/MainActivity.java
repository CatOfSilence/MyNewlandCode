package com.example.newland.testzndm0331;

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
    RFID rfid;
    Handler handler;
    Thread thread;
    String str;
    TextView tv_card;
    boolean flag;
    Modbus4150 modbus4150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);

        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.31.16", 2003), null);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.31.16", 2001), null);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //设置文本内容
                tv_card.setText(str);
                System.out.println(str);
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!flag) {
                        try {
                            rfid.readSingleEpc(new SingleEpcListener() {
                                @Override
                                public void onVal(String s) {
                                    str = s;
                                    handler.sendEmptyMessage(0);
                                    flag = true;
                                }

                                @Override
                                public void onFail(Exception e) {
                                    //这里应该是十秒左右没读到卡就运行这个函数，可以在这里把str 文本清空
                                    //如果我没记错的话，如果不行可以用我这种方法清空文本
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            modbus4150.ctrlRelay(2, false, null);
                            Thread.sleep(10);
                            modbus4150.ctrlRelay(3, true, null);
                            Thread.sleep(10);
                            modbus4150.ctrlRelay(7, true, null);
                            Thread.sleep(3000);
                            modbus4150.ctrlRelay(3, false, null);
                            Thread.sleep(10);
                            modbus4150.ctrlRelay(2, true, null);
                            Thread.sleep(10);
                            modbus4150.ctrlRelay(7, false, null);
                            flag = false;
                            //清空文本
                            str = "";
                            handler.sendEmptyMessage(0);
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
        if (modbus4150 != null)
            modbus4150.stopConnect();
        if (rfid != null)
            rfid.stopConnect();
    }
}
