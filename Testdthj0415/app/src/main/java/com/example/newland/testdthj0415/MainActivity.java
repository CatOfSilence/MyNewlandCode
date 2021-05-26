package com.example.newland.testdthj0415;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ZigBee zigBee;
    Modbus4150 modbus4150;
    TextView tv_wd,tv_sd,tv_rt;
    ImageView iv_lamp,iv_bjd,iv_sw;
    Handler handler;
    Thread thread;
    boolean lamp_sw;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.15.16",2001),null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.15.16",2002),null);
        tv_wd = findViewById(R.id.tv_wd);
        tv_rt = findViewById(R.id.tv_rt);
        tv_sd = findViewById(R.id.tv_sd);
        iv_lamp = findViewById(R.id.iv_lamp);
        iv_bjd = findViewById(R.id.iv_bjd);
        iv_sw = findViewById(R.id.iv_sw);

        iv_sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lamp_sw = !lamp_sw;
                if(!lamp_sw){
                    iv_sw.setImageResource(R.drawable.btn_switch_on);
                    iv_lamp.setImageResource(R.drawable.lamp_on);
                    try {
                        modbus4150.ctrlRelay(1,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        zigBee.ctrlDoubleRelay(0001,2,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    iv_sw.setImageResource(R.drawable.btn_switch_off);
                    iv_lamp.setImageResource(R.drawable.lamp_off);
                    try {
                        modbus4150.ctrlRelay(1,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        zigBee.ctrlDoubleRelay(0001,2,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
                    tv_wd.setText(""+df.format(zigBee.getTmpHum()[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_sd.setText(""+df.format(zigBee.getTmpHum()[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i==0){
                                tv_rt.setText("有人");
                            }else{
                                tv_rt.setText("无人");
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
                    modbus4150.getDIVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i==0){
                                try {
                                    modbus4150.ctrlRelay(7,false,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                iv_bjd.setImageResource(R.drawable.pic_alarm_off);
                            }else{
                                try {
                                    modbus4150.ctrlRelay(7,true,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                iv_bjd.setImageResource(R.drawable.pic_alarm_on);
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
                while (true){
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
        if(modbus4150!=null)
            modbus4150.stopConnect();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
