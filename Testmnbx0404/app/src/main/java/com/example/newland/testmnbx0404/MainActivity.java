package com.example.newland.testmnbx0404;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    Modbus4150 modbus4150;
    ZigBee zigBee;
    TextView tv_wd;
    Handler handler;
    Thread thread;
    ImageView iv_light,iv_bx;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        iv_light = findViewById(R.id.iv_light);
        iv_bx = findViewById(R.id.iv_bx);
        findViewById(R.id.bt_open).setOnTouchListener(this);
        findViewById(R.id.bt_close).setOnTouchListener(this);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.4.16", 2005), null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.19.4.16", 2006), null);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigBee.getFourEnter();
                    tv_wd.setText(""+df.format(FourChannelValConvert.getTemperature(vals[0]))+"℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(2, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i==1){
                                try {
                                    modbus4150.ctrlRelay(1,false,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                iv_light.setImageResource(R.drawable.lamp_off);
                                iv_bx.setImageResource(R.drawable.pic_cartoon_gate_1);
                            }else{
                                try {
                                    modbus4150.ctrlRelay(1,true,null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                iv_light.setImageResource(R.drawable.lamp_on);
                                iv_bx.setImageResource(R.drawable.pic_cartoon_gate_2);
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

    @Override
    protected void onStop() {
        super.onStop();
        if(zigBee!=null)
            zigBee.stopConnect();
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }
}
