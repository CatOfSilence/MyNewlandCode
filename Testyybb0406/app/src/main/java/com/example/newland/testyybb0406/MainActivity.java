package com.example.newland.testyybb0406;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView tv_light,tv_zy;
    Button bt;
    Handler handler;
    Thread thread;
    TextToSpeech tts;
    int light = 0,zy = 0;
    DecimalFormat df = new DecimalFormat("0");
    ZigBee zigBee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_light = findViewById(R.id.tv_light);
        tv_zy = findViewById(R.id.tv_zy);
        bt = findViewById(R.id.bt);

        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("192.168.6.16",2002),null);
        tts = new TextToSpeech(MainActivity.this,null);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tts.speak("当前光照值"+light+"流明，当前噪音值"+zy+"分贝",TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    light = (int)zigBee.getLight();
                    tv_light.setText(""+df.format(light));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    double vals[] = zigBee.getFourEnter();
                    zy = (int)FourChannelValConvert.getNoice(vals[3]);
                    tv_zy.setText(""+df.format(zy));
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
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(zigBee!=null)
            zigBee.stopConnect();
    }
}
