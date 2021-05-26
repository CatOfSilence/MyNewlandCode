package com.example.newland.testzndm0327;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    RFID rfid;
    Modbus4150 modbus4150;
    Handler handler;
    Thread thread;
    boolean flag;
    TextView tv,tv_donghua;
    int x = 0;
    String str = null;
    Animation animation1, animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        tv_donghua = findViewById(R.id.donghua);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.27.16", 952));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.27.16", 950));

        animation1 = new ScaleAnimation(1f, 0.1f, 1, 1);
        animation2 = new ScaleAnimation(0.1f, 1, 1, 1);
        animation1.setDuration(3000);
        animation2.setDuration(3000);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv.setText(str);
                if (x == 1){
                    tv_donghua.startAnimation(animation1);
                }else if(x==2){
                    tv_donghua.startAnimation(animation2);
                }
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
                                public void onVal(String val) {
                                    flag = true;
                                    handler.sendEmptyMessage(0);
                                    str = val;
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (flag) {
                        try {
                            modbus4150.closeRelay(2, new MdBus4150RelayListener() {
                                @Override
                                public void onCtrl(boolean isSuccess) {
                                    x = 1;
                                    handler.sendEmptyMessage(0);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++) ;
                        try {
                            modbus4150.openRelay(3, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.closeRelay(3, new MdBus4150RelayListener() {
                                @Override
                                public void onCtrl(boolean isSuccess) {
                                    x = 2;
                                    handler.sendEmptyMessage(0);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++) ;
                        try {
                            modbus4150.openRelay(2, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++) ;
                        try {
                            modbus4150.openRelay(7, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.closeRelay(7, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flag = false;
                        str = "";
                        x = 0;
                        handler.sendEmptyMessage(0);
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
