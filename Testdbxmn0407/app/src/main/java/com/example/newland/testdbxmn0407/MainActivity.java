package com.example.newland.testdbxmn0407;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    TextView tv_wd;
    Handler handler;
    Thread thread;
    ImageView iv_lamp, iv_bx;
    ZigBee zigBee;
    Modbus4150 modbus4150;
    int xc = 2, jj = 2;
    int x = 0;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        iv_bx = findViewById(R.id.iv_bx);
        iv_lamp = findViewById(R.id.iv_lamp);
        findViewById(R.id.bt_open).setOnTouchListener(this);
        findViewById(R.id.bt_close).setOnTouchListener(this);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.7.16", 2002), null);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.7.16", 2001), null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText("" + df.format(zigBee.getTmpHum()[0]) + "℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(2, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            xc = i;
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(1, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            jj = i;
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
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
        Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (xc == 1) {
                    try {
                        modbus4150.ctrlRelay(1, false, null);
                        iv_lamp.setImageResource(R.drawable.lamp_off);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (xc == 0) {
                    try {
                        modbus4150.ctrlRelay(1, true, null);
                        iv_lamp.setImageResource(R.drawable.lamp_on);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (xc == 1 && jj == 1) {
                    iv_bx.setImageResource(R.drawable.pic_fridge_closed);
                } else if (xc == 0 && jj == 1) {
                    iv_bx.setImageResource(R.drawable.pic_fridge_open_2);
                } else if (xc == 0 && jj == 0) {
                    iv_bx.setImageResource(R.drawable.pic_fridge_open_3);
                }

            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler1.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            try {
                modbus4150.ctrlRelay(2, false, null);
                modbus4150.ctrlRelay(3, false, null);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (action == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.bt_open:
                    try {
                        modbus4150.ctrlRelay(2, false, null);
                        for (int i = 0; i < 1000; i++) ;
                        modbus4150.ctrlRelay(3, true, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.bt_close:
                    try {
                        modbus4150.ctrlRelay(3, false, null);
                        for (int i = 0; i < 1000; i++) ;
                        modbus4150.ctrlRelay(2, true, null);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
        return false;
    }

    public void bxtp() {
        Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                iv_bx.setImageResource(R.drawable.pic_fridge_closed);
            }
        };
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    handler1.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (modbus4150 != null)
            modbus4150.stopConnect();
        if (zigBee != null)
            zigBee.stopConnect();
    }
}
