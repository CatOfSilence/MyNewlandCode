package com.example.testled0915;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity{
    EditText ed;
    LedScreen ledScreen;
    Button bt_open,bt_close,bt_send;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = findViewById(R.id.ed);
        bt_send = findViewById(R.id.bt_send);
        bt_close = findViewById(R.id.bt_close);
        bt_open = findViewById(R.id.bt_open);

        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.19.15.16",953));
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ledScreen.switchLed(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ledScreen.switchLed(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ledScreen.sendTxt(ed.getText().toString(),PlayType.LEFT,ShowSpeed.SPEED1,0,0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ledScreen!=null){
            ledScreen.stopConnect();
        }
    }
}