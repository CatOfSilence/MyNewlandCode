package com.example.newland.testyybb0418;

import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class MainActivity extends AppCompatActivity {

    TextToSpeech toSpeech;
    Button bt;
    double light = 0, zy = 0;
    NetWorkBusiness netWorkBusiness;
    Handler handler;
    Thread thread;
    String Token = "";
    TextView tv_light, tv_zy;
    DecimalFormat df = new DecimalFormat("0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        tv_light = findViewById(R.id.tv_light);
        tv_zy = findViewById(R.id.tv_zy);
        toSpeech = new TextToSpeech(MainActivity.this, null);
        init();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSpeech.speak("当前光照值" + (int) light + "流明，当前噪音值" + (int) zy + "分贝。", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                netWorkBusiness = new NetWorkBusiness(Token, "http://api.nlecloud.com");
                netWorkBusiness.getSensor("236179", "z_light", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                        if(response.getStatus()==0) {
                            light = Double.parseDouble(response.getResultObj().getValue());
                            System.out.println(response.getResultObj().getValue());
                        }
                    }
                });
                netWorkBusiness.getSensor("236179", "z_noise", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                        if(response.getStatus()==0) {
                            zy = Double.parseDouble(response.getResultObj().getValue());
                            System.out.println(response.getResultObj().getValue());
                        }
                    }
                });
                tv_light.setText("" + df.format(light));
                tv_zy.setText("" + df.format(zy));

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

    public void init() {
        netWorkBusiness = new NetWorkBusiness("", "http://api.nlecloud.com");
        netWorkBusiness.signIn(new SignIn("18316461896", "asdffdsa"), new NCallBack<BaseResponseEntity<User>>(MainActivity.this) {
            @Override
            protected void onResponse(BaseResponseEntity<User> response) {
                if (response.getStatus() == 0) {
                    Token = response.getResultObj().getAccessToken();
                    System.out.println(Token);
                }
            }
        });
        netWorkBusiness = new NetWorkBusiness(Token, "http://api.nlecloud.com");
    }
}
