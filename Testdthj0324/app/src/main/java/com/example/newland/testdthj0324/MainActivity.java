package com.example.newland.testdthj0324;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Zigbee zigbee;
    ImageView iv;
    Handler handler;
    TextView tv_wd, tv_sd, tv_yw, tv_rt;
    Button bt_type;
    Thread thread;
    Modbus4150 modbus4150;
    int type = 0, start = 0;
    boolean yw, yw_open = true;
    boolean rt, rt_open = false;
    Button bt_set;
    EditText ed_set;
    int temp = 0, temp_set = 0;
    boolean ani_fan, ani_fan_flag;
    boolean wd_set = true;
    Button bt_fanOpen, bt_fanClose, bt_lightOpen, bt_lightClose;
    DecimalFormat df = new DecimalFormat("0");
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_yw = findViewById(R.id.tv_yw);
        tv_rt = findViewById(R.id.tv_rt);
        iv = findViewById(R.id.imageView);
        bt_set = findViewById(R.id.bt_set);
        bt_type = findViewById(R.id.button);
        ed_set = findViewById(R.id.editText);
        bt_fanOpen = findViewById(R.id.bt_fan_open);
        bt_fanClose = findViewById(R.id.bt_fan_close);
        bt_lightOpen = findViewById(R.id.bt_light_open);
        bt_lightClose = findViewById(R.id.bt_light_close);

        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.24.16", 2002));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.24.16", 2001));

        iv.setBackgroundResource(R.drawable.my_fan_animation);
        animationDrawable = (AnimationDrawable) iv.getBackground();
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ed_set.getText().toString().isEmpty()) {
                    temp_set = Integer.parseInt(ed_set.getText().toString());
                    wd_set = true;
                }
            }
        });
        Handler ani_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (ani_fan_flag) {
                    if (ani_fan) {
                        animationDrawable.start();
                        ani_fan_flag = false;
                    } else {
                        animationDrawable.stop();
                        ani_fan_flag = false;
                    }
                }
            }
        };
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (start == 1) {
                    start = 2;
                    try {
                        modbus4150.closeRelay(3, null);
                        for (int i = 0; i < 1000; i++) ;
                        modbus4150.openRelay(2, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (type == 1) {
                    if (wd_set) {
                        if (temp > temp_set) {
                            try {
                                modbus4150.openRelay(0, new MdBus4150RelayListener() {
                                    @Override
                                    public void onCtrl(boolean isSuccess) {
                                        if(ani_fan == false&&ani_fan_flag==false){
                                            ani_fan_flag = true;
                                        }
                                        ani_fan = true;
                                        ani_handler.sendEmptyMessage(0);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                modbus4150.closeRelay(0, new MdBus4150RelayListener() {
                                    @Override
                                    public void onCtrl(boolean isSuccess) {
                                        if(ani_fan == true&&ani_fan_flag==false){
                                            ani_fan_flag = true;
                                        }
                                        ani_fan = false;
                                        ani_handler.sendEmptyMessage(0);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (yw) {
                        if (yw_open) {
                            yw_open = false;
                            try {
                                modbus4150.openRelay(7, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (!yw) {
                        if (yw_open) {
                            yw_open = false;
                            try {
                                modbus4150.closeRelay(7, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (rt) {
                        if (rt_open) {
                            rt_open = false;
                            try {
                                modbus4150.closeRelay(2, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < 1000; i++) ;
                            try {
                                modbus4150.openRelay(3, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            for (int i = 0; i < 1000; i++) ;
                            try {
                                modbus4150.openRelay(1, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (rt_open) {
                            rt_open = false;
                            try {
                                modbus4150.closeRelay(3, null);
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
                                modbus4150.closeRelay(1, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                try {

                    if(temp== (int) zigbee.getTmpHum()[0]){
                        wd_set = false;
                    }else{
                        wd_set = true;
                    }
                    temp = (int) zigbee.getTmpHum()[0];
                    tv_wd.setText("温度：" + df.format(temp) + "℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    tv_sd.setText("湿度：" + df.format(zigbee.getTmpHum()[1]) + "Rh");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modbus4150.getVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if (val == 0) {
                                tv_yw.setText("烟雾：无烟");
                                if (yw == true && yw_open == false) {
                                    yw_open = true;
                                }
                                yw = false;
                            } else {
                                tv_yw.setText("烟雾：有烟");
                                if (yw == false && yw_open == false) {
                                    yw_open = true;
                                }
                                yw = true;
                            }
                            if (start == 0) {
                                start = 1;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    modbus4150.getVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if (val == 0) {
                                tv_rt.setText("人体：有人");
                                if (!rt && !rt_open) {
                                    rt_open = true;
                                }
                                rt = true;
                            } else {
                                tv_rt.setText("人体：无人");
                                if (rt && !rt_open) {
                                    rt_open = true;

                                }
                                rt = false;
                            }
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
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_type.getText().toString().equals("手动模式")) {
                    bt_type.setText("自动模式");
                    type = 1;
                    bt_fanOpen.setEnabled(false);
                    bt_fanClose.setEnabled(false);
                    bt_lightOpen.setEnabled(false);
                    bt_lightClose.setEnabled(false);
                } else {
                    bt_type.setText("手动模式");
                    type = 0;
                    bt_fanOpen.setEnabled(true);
                    bt_fanClose.setEnabled(true);
                    bt_lightOpen.setEnabled(true);
                    bt_lightClose.setEnabled(true);
                }
            }
        });

        bt_fanOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(0, null);

                    animationDrawable.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_fanClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.closeRelay(0, null);
                    animationDrawable.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_lightOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_lightClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.closeRelay(1, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
        if (modbus4150 != null)
            modbus4150.stopConnect();
    }
}
class MyToast{

    private static Toast toast = null;

    public static void msg(Context context,String str){
        if(toast!=null){
            toast.setText(str);
        }else{
            toast = Toast.makeText(context,str,Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}