package com.example.newland.testckgl0422;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    int count = 0;
    int max = 10;
    ImageView iv1,iv2,iv3,iv4,iv5,iv6,iv7,iv8,iv9,iv10;
    Handler handler;
    Thread thread;
    TextView tv_count;
    Modbus4150 modbus4150;
    LedScreen ledScreen;
    boolean hw;
    Animation animation1,animation2,animation3,animation4,animation5,animation6,animation7,animation8,animation9,animation10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv1 = findViewById(R.id.iv_1);
        iv2 = findViewById(R.id.iv_2);
        iv3 = findViewById(R.id.iv_3);
        iv4 = findViewById(R.id.iv_4);
        iv5 = findViewById(R.id.iv_5);
        iv6 = findViewById(R.id.iv_6);
        iv7 = findViewById(R.id.iv_7);
        iv8 = findViewById(R.id.iv_8);
        iv9 = findViewById(R.id.iv_9);
        iv10 = findViewById(R.id.iv_10);
        tv_count = findViewById(R.id.tv_count);
        animation1 = new TranslateAnimation(0,-100,0,0);
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        animation2 = new TranslateAnimation(0,-100,0,0);
        animation2.setDuration(500);
        animation2.setFillAfter(true);
        animation3 = new TranslateAnimation(0,-100,0,0);
        animation3.setDuration(500);
        animation3.setFillAfter(true);
        animation4 = new TranslateAnimation(0,-100,0,0);
        animation4.setDuration(500);
        animation4.setFillAfter(true);
        animation5 = new TranslateAnimation(0,-100,0,0);
        animation5.setDuration(500);
        animation5.setFillAfter(true);
        animation6 = new TranslateAnimation(0,100,0,0);
        animation6.setDuration(500);
        animation6.setFillAfter(true);
        animation7 = new TranslateAnimation(0,100,0,0);
        animation7.setDuration(500);
        animation7.setFillAfter(true);
        animation8 = new TranslateAnimation(0,100,0,0);
        animation8.setDuration(500);
        animation8.setFillAfter(true);
        animation9 = new TranslateAnimation(0,100,0,0);
        animation9.setDuration(500);
        animation9.setFillAfter(true);
        animation10 = new TranslateAnimation(0,100,0,0);
        animation10.setDuration(500);
        animation10.setFillAfter(true);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv1.startAnimation(animation1);
            }
        });

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.22.16",2001),null);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.18.22.16",2004),null);
        try {
            ledScreen.switchLed(true,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_count.setText("空位："+max+"个");
                try {
                    modbus4150.getDIVal(0, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if(i == 1) {
                                if (!hw) {
                                    if(count<10){
                                        count += 1;
                                        max -= 1;
                                        try {
                                            ledScreen.sendTxt("已用:"+count+"，剩余数量："+max,PlayType.LEFT,ShowSpeed.SPEED1,1,90,null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        switch (count){
                                            case 1:
                                                iv1.startAnimation(animation1);
                                                break;
                                            case 2:
                                                iv2.startAnimation(animation2);
                                                break;
                                            case 3:
                                                iv3.startAnimation(animation3);
                                                break;
                                            case 4:
                                                iv4.startAnimation(animation4);
                                                break;
                                            case 5:
                                                iv5.startAnimation(animation5);
                                                break;
                                            case 6:
                                                iv6.startAnimation(animation6);
                                                break;
                                            case 7:
                                                iv7.startAnimation(animation7);
                                                break;
                                            case 8:
                                                iv8.startAnimation(animation8);
                                                break;
                                            case 9:
                                                iv9.startAnimation(animation9);
                                                break;
                                            case 10:
                                                iv10.startAnimation(animation10);
                                                break;
                                        }
                                    }else{
                                        try {
                                            ledScreen.sendTxt("车位已满",PlayType.NOW,ShowSpeed.SPEED1,1,90,null);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                hw = true;
                            }else{
                                hw = false;
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
                        Thread.sleep(300);
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
        if(ledScreen!=null)
            ledScreen.stopConnect();
    }

}
