package com.example.newland.testldzm0404;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Modbus4150 modbus4150;
    TextView tv_light, tv_zy, tv_rt, tv_wd2;
    ZigBee zigBee;
    Handler handler;
    Thread thread;
    Button bt_rt, bt_zy, bt_light;
    EditText ed_rt, ed_zy, ed_light;
    boolean light_fgbt, zy_fgbt, rt_fgbt;
    boolean light_fg = true, zy_fg = true, rt_fg = true;
    double zy;
    double light, light_yz, zy_yz, rt;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_light = findViewById(R.id.tv_light);
        tv_zy = findViewById(R.id.tv_zy);
        tv_rt = findViewById(R.id.tv_rt);
        tv_wd2 = findViewById(R.id.tv_wd2);
        bt_rt = findViewById(R.id.bt_rt);
        bt_zy = findViewById(R.id.bt_zy);
        bt_light = findViewById(R.id.bt_light);
        ed_rt = findViewById(R.id.ed_rt);
        ed_zy = findViewById(R.id.ed_zy);
        ed_light = findViewById(R.id.ed_light);


        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.4.16", 2001), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.4.16", 2002), null);


        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_light.getText().toString().isEmpty()) {
                    light_yz = Double.parseDouble(ed_light.getText().toString());
                    light_fgbt = true;
                } else {
                    Toast.makeText(MainActivity.this, "阈值不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_rt.getText().toString().isEmpty()) {
                    rt = Double.parseDouble(ed_light.getText().toString());
                    rt_fgbt = true;
                } else {
                    Toast.makeText(MainActivity.this, "阈值不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_zy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_zy.getText().toString().isEmpty()) {
                    zy_yz = Double.parseDouble(ed_light.getText().toString());
                    zy_fgbt = true;
                } else {
                    Toast.makeText(MainActivity.this, "阈值不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
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
                    if (zy_fgbt) {
                        if (zy_fg) {
                            if (zy > zy_yz) {
                                light_fg = false;
                                rt_fg = false;
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (zy < zy_yz) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                light_fg = true;
                                rt_fg = true;
                                zy_fg = true;
                            }
                        }
                    }
                }
            }
        }.start();
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
                    if (rt_fgbt) {
                        if (rt_fg) {
                            if (rt == 0) {
                                light_fg = false;
                                zy_fg = false;
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (rt == 1) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                light_fg = true;
                                rt_fg = true;
                                zy_fg = true;
                            }
                        }
                    }
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    if (light_fgbt) {
                        if (light_fg) {
                            if (light_yz > light) {
                                rt_fg = false;
                                zy_fg = false;
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else if (light_yz < light) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    zigBee.ctrlDoubleRelay(0001, 2, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                light_fg = true;
                                rt_fg = true;
                                zy_fg = true;
                            }
                        }
                    }
                }
            }
        }.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    light = zigBee.getLight();
                    tv_light.setText("光照：" + df.format(light) + "Lx");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    double vals[] = zigBee.getFourEnter();
                    zy = FourChannelValConvert.getTemperature(vals[3]);
                    tv_zy.setText("噪音：" + df.format(FourChannelValConvert.getTemperature(vals[3])));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            rt = i;
                            if (i == 0) {
                                tv_rt.setText("人体：有人");
                            } else {
                                tv_rt.setText("人体：无人");
                            }
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(4, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if (i == 0) {
                                tv_wd2.setText("微动2：关");
                            } else {
                                tv_wd2.setText("微动2：开");
                            }
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
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
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
        if (zigBee != null)
            zigBee.stopConnect();
    }
}
