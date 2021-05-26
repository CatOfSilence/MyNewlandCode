package com.example.testadam0912;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity{
    EditText ed1,ed2;
    TextView tv_get;
    Button bt_open,bt_close;
    Modbus4150 modbus4150;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        tv_get = findViewById(R.id.tv_get);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.15.16",950));

        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getVal(Integer.parseInt(ed1.getText().toString()),val -> tv_get.setText(""+val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    handler.sendEmptyMessage(0);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.openRelay(Integer.parseInt(ed2.getText().toString()),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modbus4150.closeRelay(Integer.parseInt(ed2.getText().toString()),null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(modbus4150!=null){
            modbus4150.stopConnect();
        }
    }
}