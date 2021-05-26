package com.example.newland;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    ImageView iv_yan,iv_men,iv_fire;
    Animation animation;
    Modbus4150 modbus4150;
    Handler handler;
    Thread thread;
    Zigbee zigbee;
    TextView tv_wd;
    boolean fire;
    int x = 0;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_yan = findViewById(R.id.iv_yan);
        tv_wd = findViewById(R.id.tv);
        iv_fire = findViewById(R.id.iv_fire);
        iv_men = findViewById(R.id.iv_men);

        zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.1.16",951));
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.1.16", 950));

        animation = new RotateAnimation(45, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(0);
        animation.setRepeatCount(1);
        animation.setFillAfter(true);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if(val==1){
                                iv_yan.setVisibility(View.VISIBLE);

                                iv_yan.startAnimation(animation);
                            }else{
                                iv_yan.setVisibility(View.INVISIBLE);
                                iv_yan.setAnimation(null);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if(zigbee.getFire()==1){
                        iv_fire.setVisibility(View.VISIBLE);
                    }else{
                        iv_fire.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int val) {
                            if(val==0){
                                iv_men.setImageResource(R.drawable.open);
                            }else{
                                iv_men.setImageResource(R.drawable.close);
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
                while (true){
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
        Handler handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigbee.getFourEnter();
                    tv_wd.setText("当前室温：" + df.format(FourChannelValConvert.getTemperature(vals[0])) + "度");
                    x = 0;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler1.sendEmptyMessage(0);
                }
            }
        });
        thread1.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigbee!=null)
            zigbee.stopConnect();
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }
}
