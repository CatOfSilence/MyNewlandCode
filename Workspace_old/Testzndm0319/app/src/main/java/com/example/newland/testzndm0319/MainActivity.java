package com.example.newland.testzndm0319;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    RFID rfid;
    TextView tv_card;
    Handler handler, handler1;
    Thread thread;
    boolean flag;
    int x = 0;
    String card;
    Modbus4150 modbus4150;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);
        tv = findViewById(R.id.view1);

        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.19.16", 952));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.19.16", 950));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_card.setText(card);
                if (x == 1) {
                    Animation animation = new ScaleAnimation(1f, 0f, 1f, 1f);
                    animation.setDuration(3000);
                    animation.setFillAfter(true);
                    tv.startAnimation(animation);
                } else if (x == 2) {
                    Animation animation = new ScaleAnimation(0f, 1f, 1f, 1f);
                    animation.setDuration(3000);
                    animation.setFillAfter(true);
                    tv.startAnimation(animation);
                }

            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!flag) {
                        try {
                            rfid.readSingleEpc(new SingleEpcListener() {
                                @Override
                                public void onVal(String val) {
                                    card = val;
                                    handler.sendEmptyMessage(0);
                                    flag = true;
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (flag) {
                        try {
                            handler.sendEmptyMessage(0);
                            try {
                                modbus4150.closeRelay(2, null);
                                Thread.sleep(100);
                                modbus4150.openRelay(3, null);

                                x = 1;
                                handler.sendEmptyMessage(0);

                                Thread.sleep(3000);
                                modbus4150.closeRelay(3, null);
                                Thread.sleep(100);
                                modbus4150.openRelay(2, null);

                                x = 2;
                                handler.sendEmptyMessage(0);

                                Thread.sleep(100);
                                modbus4150.openRelay(7, null);
                                Thread.sleep(3000);
                                modbus4150.closeRelay(7, null);

                                x = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            card = "";
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
        if (rfid != null) {
            rfid.stopConnect();
        }
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
    }
}
