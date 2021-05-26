package com.example.newland.testyybb0425;

import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_light,tv_zy,tv_info;
    Button bt;
    int light = 0,zy = 0;
    TextToSpeech tts;
    Handler handler;
    Thread thread;
    ZigBee zigBee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_light = findViewById(R.id.tv_light);
        tv_zy = findViewById(R.id.tv_zy);
        tv_info = findViewById(R.id.tvinfo);
        bt = findViewById(R.id.button);
        zigBee = new ZigBee(DataBusFactory.newSerialDataBus(1,38400),null);
        tts = new TextToSpeech(MainActivity.this,null);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("当前光照值"+light+"流明，当前噪音值"+zy+"分贝。", TextToSpeech.QUEUE_FLUSH,null);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    light = (int) zigBee.getLight();
                    tv_light.setText(""+light);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    double vals[] = zigBee.getFourEnter();
                    zy = (int) FourChannelValConvert.getNoice(vals[3]);
                    tv_zy.setText(""+zy);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(zy!=0){
                    tv_info.setVisibility(View.INVISIBLE);
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
}
