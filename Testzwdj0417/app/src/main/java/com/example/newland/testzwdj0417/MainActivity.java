package com.example.newland.testzwdj0417;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    RFID rfid;
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    static String str1 = "", str2 = "", str3 = "", str4 = "", str5 = "", str6 = "";
    Handler handler;
    int x = 0;
    boolean flag;
    Modbus4150 modbus4150;
    LedScreen ledScreen;
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

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.21.17.16",2001),null);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.21.17.16", 2003), null);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.21.17.16", 2004), null);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (x) {
                    case 1:
                        tv1.setText(str1);
                        tv1.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        tv2.setText(str2);
                        tv2.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        tv3.setText(str3);
                        tv3.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        tv4.setText(str4);
                        tv4.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        tv5.setText(str5);
                        tv5.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 6:
                        tv6.setText(str6);
                        tv6.setBackgroundResource(R.drawable.kuang1);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 7:
                        try {
                            modbus4150.ctrlRelay(7,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++);
                        try {
                            ledScreen.switchLed(true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++);
                        try {
                            ledScreen.sendTxt("此票据已登记",PlayType.LEFT,ShowSpeed.SPEED1,1,20,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 8:
                        try {
                            ledScreen.sendTxt("位置已满",PlayType.LEFT,ShowSpeed.SPEED1,1,100,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        for (int i = 0; i < 1000; i++);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                flag = false;
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                if (!flag) {
                                    card_pd(s);
                                    flag = true;
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                try {
                                    modbus4150.ctrlRelay(7,false,null);
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
        }.start();
    }

    public void card_pd(String s) {
        if (str1.length() == 0) {
            str1 = s;
            x = 1;
            handler.sendEmptyMessage(0);
        } else if (str2.length() == 0) {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                str2 = s;
                x = 2;
                handler.sendEmptyMessage(0);
            }
        } else if (str3.length() == 0) {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str2.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                str3 = s;
                x = 3;
                handler.sendEmptyMessage(0);
            }
        } else if (str4.length() == 0) {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str2.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str3.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                str4 = s;
                x = 4;
                handler.sendEmptyMessage(0);
            }
        } else if (str5.length() == 0) {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str2.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str3.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str4.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                str5 = s;
                x = 5;
                handler.sendEmptyMessage(0);
            }
        } else if (str6.length() == 0) {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str2.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str3.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str4.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str5.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                str6 = s;
                x = 6;
                handler.sendEmptyMessage(0);
            }
        } else {
            if (str1.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str2.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str3.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str4.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str5.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else if (str6.equals(s)) {
                x = 7;
                handler.sendEmptyMessage(0);
            } else {
                x = 8;
                handler.sendEmptyMessage(0);
            }
        }
    }
}
