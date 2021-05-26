package com.example.newland.testznjj0404;

import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_wd, tv_sd, tv_light, tv_rt;
    ZigBee zigBee;
    Modbus4150 modbus4150;
    Handler handler;
    Thread thread;
    ImageView iv;
    EditText ed;
    Button bt;
    int dh = 0;
    double light = 0, light_yz = 0;
    boolean light_fg = true, rt_fg, rt, light_fgbt;
    AnimationDrawable animationDrawable;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = findViewById(R.id.iv);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_light = findViewById(R.id.tv_light);
        tv_rt = findViewById(R.id.tv_rt);
        ed = findViewById(R.id.ed);
        bt = findViewById(R.id.bt);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.16.6.15", 2001), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.16.6.15", 2002), null);

        iv.setBackgroundResource(R.drawable.fan);
        animationDrawable = (AnimationDrawable) iv.getBackground();


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed.getText().toString().isEmpty()) {
                    light_yz = Double.parseDouble(ed.getText().toString());
                    light_fgbt = true;
                }else{
                    Toast.makeText(MainActivity.this,"阈值不能为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText("温度：" + df.format(zigBee.getTmpHum()[0]) + "℃");
                    tv_sd.setText("湿度：" + df.format(zigBee.getTmpHum()[1]) + "Rh");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    light = zigBee.getLight();
                    tv_light.setText("光照：" + df.format(light) + "Lx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if (i == 0) {
                                tv_rt.setText("人体：有人");
                                if (!rt && !rt_fg) {
                                    rt_fg = true;
                                }
                                light_fg = false;
                                rt = true;
                            } else {
                                tv_rt.setText("人体：无人");
                                if (rt && !rt_fg) {
                                    rt_fg = true;
                                }
                                rt = false;
                                light_fg = true;
                            }
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (light_fgbt) {
                    if (light_fg) {
                        if (light < light_yz) {
                            try {
                                zigBee.ctrlDoubleRelay(0001, 2, true, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (light > light_yz) {
                            try {
                                zigBee.ctrlDoubleRelay(0001, 2, false, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
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
                    handler.sendEmptyMessage(0);

                }
            }
        });
        thread.start();
        Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (dh == 1) {
                    animationDrawable.start();
                    try {
                        modbus4150.ctrlRelay(2, false, null);
                        for (int i = 0; i < 1000; i++);
                        modbus4150.ctrlRelay(3, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (dh == 2) {
                    animationDrawable.stop();
                    try {
                        modbus4150.ctrlRelay(3, false, null);
                        for (int i = 0; i < 1000; i++);
                        modbus4150.ctrlRelay(2, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
                    if (rt_fg) {
                        rt_fg = false;
                        light_fg = false;
                        if (rt) {
                            try {
                                dh = 1;
                                handler1.sendEmptyMessage(0);
                                zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                Thread.sleep(1000);
                                zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                Thread.sleep(1000);
                                zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                Thread.sleep(1000);
                                zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                Thread.sleep(1000);
                                zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                Thread.sleep(1000);
                                zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                dh = 2;
                                handler1.sendEmptyMessage(0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
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
