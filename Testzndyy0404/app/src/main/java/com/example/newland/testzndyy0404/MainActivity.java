package com.example.newland.testzndyy0404;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    Button bt_sm;
    TextView tv_card;
    RFID rfid;
    Handler handler;
    Thread thread;
    String card;
    Modbus4150 modbus4150;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView iv;
    int x = 0;
    boolean flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_sm = findViewById(R.id.bt_sm);
        tv_card = findViewById(R.id.card);
        iv = findViewById(R.id.iv_men);
        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.19.4.16", 2005), null);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.19.4.16", 2007), null);
        bt_sm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SetActivity.class);
                startActivity(intent);
                System.exit(0);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_card.setText(card);
                switch (x){
                    case 0:
                        break;
                    case 1:
                        iv.setImageResource(R.drawable.pic_cartoon_gate_2);
                        Toast.makeText(MainActivity.this,"刷票成功，即将进入主页面",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        iv.setImageResource(R.drawable.pic_cartoon_gate_1);
                        for (int i = 0; i < 10000; i++);
                        Intent intent = new Intent(MainActivity.this,IndexActivity.class);
                        startActivity(intent);
                        break;
                }
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
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                card = s;
                                handler.sendEmptyMessage(0);
                                if(card.equals(sp.getString("card", ""))){
                                    flag = true;
                                }else{
                                    flag = false;
                                }
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag){
                        try {
                            modbus4150.ctrlRelay(2, false, null);
                            for (int i = 0; i < 1000; i++) ;
                            modbus4150.ctrlRelay(3, true, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        x = 1;
                        handler.sendEmptyMessage(0);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.ctrlRelay(3, false, null);
                            for (int i = 0; i < 1000; i++) ;
                            modbus4150.ctrlRelay(2, true, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        x = 2;
                        card = "";
                        handler.sendEmptyMessage(0);
                        flag = false;
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rfid != null)
            rfid.stopConnect();
    }
}
