package com.example.newland.testzwdj0425;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    RFID rfid;
    Modbus4150 modbus4150;
    LedScreen ledScreen;
    Handler handler;
    Thread thread;
    String str1 = "", str2 = "", str3 = "", str4 = "", str5 = "", str6 = "";
    int type = 0;
    boolean fff = false, flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("192.168.25.16", 2001), null);
        rfid = new RFID(DataBusFactory.newSocketDataBus("192.168.25.16", 2003), null);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("192.168.25.16", 2004), null);

        try {
            ledScreen.switchLed(true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                if (str1.isEmpty()) {
                                    str1 = s;
                                    asd(1);
                                } else if (str2.isEmpty()) {
                                    if (str1.equals(s)) {
                                        asd(7);
                                    } else {
                                        str2 = s;
                                        asd(2);
                                    }
                                } else if (str3.isEmpty()) {
                                    if (str1.equals(s) || str2.equals(s)) {
                                        asd(7);
                                    } else {
                                        str3 = s;
                                        asd(3);
                                    }
                                } else if (str4.isEmpty()) {
                                    if (str1.equals(s) || str2.equals(s) || str3.equals(s)) {
                                        asd(7);
                                    } else {
                                        str4 = s;
                                        asd(4);
                                    }
                                } else if (str5.isEmpty()) {
                                    if (str1.equals(s) || str2.equals(s) || str3.equals(s) || str4.equals(s)) {
                                        asd(7);
                                    } else {
                                        str5 = s;
                                        asd(5);
                                    }
                                } else if (str6.isEmpty()) {
                                    if (str1.equals(s) || str2.equals(s) || str3.equals(s) || str4.equals(s) || str5.equals(s)) {
                                        asd(7);
                                    } else {
                                        str6 = s;
                                        asd(6);
                                    }
                                } else {
                                    if (str1.equals(s) || str2.equals(s) || str3.equals(s) || str4.equals(s) || str5.equals(s) || str6.equals(s)) {
                                        asd(7);
                                    } else {
                                        asd(8);
                                    }
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                try {
                                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                for (int i = 0; i < 1000; i++) ;
                                try {
                                    modbus4150.ctrlRelay(7, false, null);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(4000);
                        if(!flag) {
                            handler.sendEmptyMessage(0);
                            flag = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();


    }

    public void asd(int x) {
        if (!fff) {
            tv1.setText("");
            tv2.setText("");
            tv3.setText("");
            tv4.setText("");
            tv5.setText("");
            tv6.setText("");
            fff = true;
        }
        switch (x) {
            case 1:
                tv1.setText(str1);
                tv1.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                tv2.setText(str2);
                tv2.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                tv3.setText(str3);
                tv3.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                tv4.setText(str4);
                tv4.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                tv5.setText(str5);
                tv5.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                tv6.setText(str6);
                tv6.setBackgroundResource(R.drawable.kuang2);
                try {
                    ledScreen.sendTxt("", PlayType.LEFT, ShowSpeed.SPEED1, 1, 1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    ledScreen.sendTxt("此票据已登记", PlayType.LEFT, ShowSpeed.SPEED1, 1, 90, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, true, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    ledScreen.sendTxt("位置已满", PlayType.LEFT, ShowSpeed.SPEED1, 1, 90, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 1000; i++) ;
                try {
                    modbus4150.ctrlRelay(7, false, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        flag = false;
    }

}
