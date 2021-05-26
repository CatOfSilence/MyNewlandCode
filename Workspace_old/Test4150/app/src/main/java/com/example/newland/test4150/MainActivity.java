package com.example.newland.test4150;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150RelayListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    Modbus4150 modbus4150;
    RadioGroup rg_4150;
    Button bt, bt_set, bt_cannel;
    EditText ed_hw, ed_jj, ed_xc, ed_wd1, ed_wd2, ed_smog, ed_renti, ed_light, ed_fan, ed_qianjin, ed_houtui, ed_red, ed_green, ed_yellow, ed_alert;
    Switch sw_light, sw_fan, sw_qianjin, sw_houtui, sw_red, sw_green, sw_yellow, sw_alert;
    TextView tv_hw, tv_jj, tv_xc, tv_wd1, tv_wd2, tv_smog, tv_renti;
    int hw = 0, jj = 1, xc = 2, wd1 = 3, wd2 = 4, smog = 5, renti = 6, light = 1, fan = 0, qianjin = 2, houtui = 3, red = 4, green = 5, yellow = 6, alert = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.Bt_4150);
        SwitchOnOff();
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.3.16", 950));

        rg_4150.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_socket:
                        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.3.16", 950));
                        break;
                    case R.id.rb_serial:
                        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(1, 9600));
                        break;
                }
            }
        });
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getVal(hw,val -> tv_hw.setText("红外对射："+ val));
                    modbus4150.getVal(jj,val -> tv_jj.setText("接近开关："+ val));
                    modbus4150.getVal(xc,val -> tv_xc.setText("行程开关："+ val));
                    modbus4150.getVal(wd1,val -> tv_wd1.setText("微动开关1："+ val));
                    modbus4150.getVal(wd2,val -> tv_wd2.setText("微动开关2："+ val));
                    modbus4150.getVal(smog,val -> tv_smog.setText("烟雾数据："+ val));
                    modbus4150.getVal(renti,val -> tv_renti.setText("人体数据："+ val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_item, null);

                ed_hw = v.findViewById(R.id.ed_infrared);
                ed_jj = v.findViewById(R.id.ed_jiejin);
                ed_xc = v.findViewById(R.id.ed_xc);
                ed_wd1 = v.findViewById(R.id.ed_weidong1);
                ed_wd2 = v.findViewById(R.id.ed_weidong2);
                ed_smog = v.findViewById(R.id.ed_smog);
                ed_renti = v.findViewById(R.id.ed_renti);
                ed_light = v.findViewById(R.id.ed_light);
                ed_fan = v.findViewById(R.id.ed_fan);
                ed_qianjin = v.findViewById(R.id.ed_qianjin);
                ed_houtui = v.findViewById(R.id.ed_houtui);
                ed_red = v.findViewById(R.id.ed_red);
                ed_green = v.findViewById(R.id.ed_green);
                ed_yellow = v.findViewById(R.id.ed_yellow);
                ed_alert = v.findViewById(R.id.ed_alert);
                bt_set = v.findViewById(R.id.Bt_set);
                bt_cannel = v.findViewById(R.id.Bt_cannel);

                builder.setTitle("请配置接口");
                builder.setView(v);
                AlertDialog dialog = builder.show();
                ed_hw.setText(String.valueOf(hw));
                ed_jj.setText(String.valueOf(jj));
                ed_xc.setText(String.valueOf(xc));
                ed_wd1.setText(String.valueOf(wd1));
                ed_wd2.setText(String.valueOf(wd2));
                ed_smog.setText(String.valueOf(smog));
                ed_renti.setText(String.valueOf(renti));
                ed_light.setText(String.valueOf(light));
                ed_fan.setText(String.valueOf(fan));
                ed_qianjin.setText(String.valueOf(qianjin));
                ed_houtui.setText(String.valueOf(houtui));
                ed_red.setText(String.valueOf(red));
                ed_green.setText(String.valueOf(green));
                ed_yellow.setText(String.valueOf(yellow));
                ed_alert.setText(String.valueOf(alert));
                bt_set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hw = Integer.parseInt(ed_hw.getText().toString());
                        jj = Integer.parseInt(ed_jj.getText().toString());
                        xc = Integer.parseInt(ed_xc.getText().toString());
                        wd1 = Integer.parseInt(ed_wd1.getText().toString());
                        wd2 = Integer.parseInt(ed_wd2.getText().toString());
                        smog = Integer.parseInt(ed_smog.getText().toString());
                        renti = Integer.parseInt(ed_renti.getText().toString());
                        light = Integer.parseInt(ed_light.getText().toString());
                        fan = Integer.parseInt(ed_fan.getText().toString());
                        qianjin = Integer.parseInt(ed_qianjin.getText().toString());
                        houtui = Integer.parseInt(ed_houtui.getText().toString());
                        red = Integer.parseInt(ed_red.getText().toString());
                        green = Integer.parseInt(ed_green.getText().toString());
                        yellow = Integer.parseInt(ed_yellow.getText().toString());
                        alert = Integer.parseInt(ed_alert.getText().toString());
                        dialog.dismiss();
                    }
                });
                bt_cannel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public void SwitchOnOff() {
        rg_4150 = findViewById(R.id.rg_4150);
        tv_hw = findViewById(R.id.infrared);
        tv_jj = findViewById(R.id.jiejin);
        tv_xc = findViewById(R.id.xingcheng);
        tv_wd1 = findViewById(R.id.weidong1);
        tv_wd2 = findViewById(R.id.weidong2);
        tv_smog = findViewById(R.id.smoke);
        tv_renti = findViewById(R.id.renti);

        sw_light = findViewById(R.id.light);
        sw_fan = findViewById(R.id.fan);
        sw_qianjin = findViewById(R.id.qianjin);
        sw_houtui = findViewById(R.id.houtui);
        sw_red = findViewById(R.id.red);
        sw_green = findViewById(R.id.green);
        sw_yellow = findViewById(R.id.yellow);
        sw_alert = findViewById(R.id.alert);

        sw_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(light, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(light, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(fan, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(fan, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_qianjin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(qianjin, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(qianjin, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_houtui.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(houtui, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(houtui, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_red.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(red, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(red, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_green.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(green, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(green, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_yellow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(yellow, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(yellow, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        sw_alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        modbus4150.openRelay(alert, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "开启成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "开启失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        modbus4150.closeRelay(alert, new MdBus4150RelayListener() {
                            @Override
                            public void onCtrl(boolean isSuccess) {
                                if (isSuccess) {
                                    MyToast.go(getApplicationContext(), "关闭成功");
                                } else {
                                    MyToast.go(getApplicationContext(), "关闭失败");
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setId() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
    }
}

class MyToast {
    public static Toast toast;

    public static void go(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
        } else {
            toast.setText(s);
        }
        toast.show();
    }
}
