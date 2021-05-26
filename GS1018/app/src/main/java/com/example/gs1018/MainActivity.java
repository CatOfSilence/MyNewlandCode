package com.example.gs1018;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Zigbee zigbee;
    Modbus4150 modbus4150;
    ImageView img;
    TextView tv_wd, tv_sd, tv_yw, tv_rt;
    Button bt_light, bt_fan, bt_bjd, bt_tg, bt_set, bt_type;
    double wd, set_wd = 25;
    int yw, rt,x = 0;
    boolean tg, flag, rt_on, rt_off, wd_on, wd_off;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        tv_yw = findViewById(R.id.tv_yw);
        tv_rt = findViewById(R.id.tv_rt);
        bt_light = findViewById(R.id.bt_light);
        bt_fan = findViewById(R.id.bt_fan);
        bt_bjd = findViewById(R.id.bt_bjd);
        bt_tg = findViewById(R.id.bt_tg);
        bt_set = findViewById(R.id.bt_set);
        bt_type = findViewById(R.id.bt_type);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.25.16", 950));
        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.18.25.16", 951));

        Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    wd = zigbee.getTmpHum()[0];
                    tv_wd.setText("" + df.format(wd));
                    tv_sd.setText("" + df.format(zigbee.getTmpHum()[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            tv_yw.setText("" + val);
                            yw = val;
                        }
                    });
                    modbus4150.getVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            tv_rt.setText("" + val);
                            rt = val;
                        }
                    });
                    if (flag) {
                        if (yw > 0) {
                            modbus4150.openRelay(7, null);
                        }
                        if (wd > set_wd) {
                            if (wd_on) {
                                wd_on = false;
                                wd_off = true;
                                modbus4150.openRelay(0, null);
                            }
                        } else {
                            if (wd_off) {
                                wd_on = true;
                                wd_off = false;
                                modbus4150.closeRelay(0, null);
                            }
                        }
                        if (rt == 0) {
                            if (rt_on) {
                                rt_on = false;
                                rt_off = true;
                                modbus4150.closeRelay(2, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.openRelay(3, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.openRelay(1, null);
                            }
                        } else {
                            if (rt_off) {
                                rt_on = true;
                                rt_off = false;
                                modbus4150.closeRelay(3, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.openRelay(2, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.closeRelay(1, null);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!tg) {
                        try {
                            modbus4150.closeRelay(3, null);
                            for (int i = 0; i < 1000; i++) ;
                            modbus4150.openRelay(2, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tg = true;
                    }
                }
            }
        });
        thread.start();
        try {
            modbus4150.closeRelay(3, null);
            for (int i = 0; i < 1000; i++) ;
            modbus4150.openRelay(2, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.bt_light:
                        if (bt_light.getText().toString().equals("电灯开启")) {
                            bt_light.setText("电灯关闭");
                            try {
                                modbus4150.openRelay(1, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            bt_light.setText("电灯开启");
                            try {
                                modbus4150.closeRelay(1, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_fan:
                        if (bt_fan.getText().toString().equals("风扇开启")) {
                            bt_fan.setText("风扇关闭");
                            try {
                                modbus4150.openRelay(0, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            bt_fan.setText("风扇开启");
                            try {
                                modbus4150.closeRelay(0, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_tg:
                        if (bt_tg.getText().toString().equals("推杆前进")) {
                            bt_tg.setText("推杆后退");
                            try {
                                modbus4150.closeRelay(3, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.openRelay(2, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            bt_tg.setText("推杆前进");
                            try {
                                modbus4150.closeRelay(2, null);
                                for (int i = 0; i < 1000; i++) ;
                                modbus4150.openRelay(3, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_bjd:
                        if (bt_bjd.getText().toString().equals("警灯开启")) {
                            bt_bjd.setText("警灯关闭");
                            try {
                                modbus4150.openRelay(7, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            bt_bjd.setText("警灯开启");
                            try {
                                modbus4150.closeRelay(7, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_set:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View v = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert,null);
                        EditText ed_wd = v.findViewById(R.id.ed_wd);
                        Button bt_wd = v.findViewById(R.id.bt_wd);
                        builder.setView(v);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                        lp.width = 800;
                        lp.height = 600;
                        alertDialog.getWindow().setAttributes(lp);
                        bt_wd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ed_wd.getText().toString().isEmpty()) {
                                }else {
                                    set_wd = Double.parseDouble(ed_wd.getText().toString());
                                    alertDialog.dismiss();
                                }
                            }
                        });
                        break;
                    case R.id.bt_type:
                        if (bt_type.getText().toString().equals("手动模式")) {
                            bt_type.setText("自动模式");
                            flag = true;
                            wd_on = true;
                            wd_off = true;
                            rt_on = true;
                            rt_off = true;
                            bt_light.setEnabled(false);
                            bt_fan.setEnabled(false);
                            bt_bjd.setEnabled(false);
                            bt_tg.setEnabled(false);
                        } else {
                            bt_type.setText("手动模式");
                            flag = false;
                            bt_light.setEnabled(true);
                            bt_fan.setEnabled(true);
                            bt_bjd.setEnabled(true);
                            bt_tg.setEnabled(true);
                        }
                        break;
                }
            }
        };
        bt_type.setOnClickListener(click);
        bt_bjd.setOnClickListener(click);
        bt_light.setOnClickListener(click);
        bt_fan.setOnClickListener(click);
        bt_set.setOnClickListener(click);
        bt_tg.setOnClickListener(click);
    }
}