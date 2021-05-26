package com.example.newland.testdbxmn0415;

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
    ZigBee zigBee;
    Modbus4150 modbus4150;
    TextView tv_wd;
    Button bt_open,bt_close;
    ImageView iv_lamp,iv_bx;
    Handler handler;
    Thread thread;
    boolean xc = true,jj = false;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.15.16",2001),null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("172.18.15.16",2002),null);
        tv_wd = findViewById(R.id.tv_wd);
        iv_bx = findViewById(R.id.iv_bx);
        iv_lamp = findViewById(R.id.iv_lamp);
        findViewById(R.id.bt_open).setOnTouchListener(this);
        findViewById(R.id.bt_close).setOnTouchListener(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText(""+df.format(zigBee.getTmpHum()[0])+"℃");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(2, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i==1){
                                xc = true;
                            }else{
                                xc = false;
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
                    modbus4150.getDIVal(1, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i==1){
                                jj = true;
                            }else{
                                jj = false;
                            }
                        }
                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(xc&&jj){
                    iv_bx.setImageResource(R.drawable.pic_fridge_closed);
                }else if(!xc&&jj){
                    iv_bx.setImageResource(R.drawable.pic_fridge_open_2);
                }else if(!xc&&!jj){
                    iv_bx.setImageResource(R.drawable.pic_fridge_open_3);
                }

                if(!xc){
                    try {
                        modbus4150.ctrlRelay(1,true,null);
                        iv_lamp.setImageResource(R.drawable.lamp_on);
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
                    try {
                        modbus4150.ctrlRelay(1,false,null);
                        iv_lamp.setImageResource(R.drawable.lamp_off);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < 1000; i++);
                    try {
                        zigBee.ctrlDoubleRelay(0001,2,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
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
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if(action == MotionEvent.ACTION_UP||action == MotionEvent.ACTION_CANCEL){
            try {
                modbus4150.ctrlRelay(2,false,null);
                for (int i = 0; i < 1000; i++);
                modbus4150.ctrlRelay(3,false,null);
                for (int i = 0; i < 1000; i++);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(action == MotionEvent.ACTION_DOWN){
            switch (view.getId()){
                case R.id.bt_open:
                    try {
                        modbus4150.ctrlRelay(2,false,null);
                        for (int i = 0; i < 1000; i++);
                        modbus4150.ctrlRelay(3,true,null);
                        for (int i = 0; i < 1000; i++);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.bt_close:
                    try {
                        modbus4150.ctrlRelay(3,false,null);
                        for (int i = 0; i < 1000; i++);
                        modbus4150.ctrlRelay(2,true,null);
                        for (int i = 0; i < 1000; i++);
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
        if(modbus4150!=null)
            modbus4150.stopConnect();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
