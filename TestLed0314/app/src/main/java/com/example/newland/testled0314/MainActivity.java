package com.example.newland.testled0314;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    LedScreen ledScreen;
    Button bt;
    int i = 0;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.button);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.20.12.16", 953));
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    ledScreen.switchLed(true);
                    ledScreen.sendTxt("" + sdf.format(new Date()) + "            ", PlayType.LEFT, ShowSpeed.SPEED3, 0, 100);
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
                        Thread.sleep(7000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i >= 5) {
                    bt.setText("疯了是吧？");
                    if(i >= 7){
                        i = 0;
                        bt.setText("发送个吊啊");
                    }
                }else{
                    bt.setText("发送个吊啊");
                }

                i++;
            }
        });

    }
}
