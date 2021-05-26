package com.example.newland.testznxq0402;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    Button bt_res,bt_set;
    Handler handler;
    Thread thread;
    RFID rfid;
    Modbus4150 modbus4150;
    boolean flag;
    boolean nono;
    String str;
    TextView tv_card;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_res = findViewById(R.id.bt_res);
        bt_set = findViewById(R.id.bt_set);
        tv_card = findViewById(R.id.tv_Card);

        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.2.16",2003),null);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.2.16", 2001), null);

        MySqlite mySqlite = new MySqlite(this,"data",null,1);
        db = mySqlite.getWritableDatabase();

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SetActivity.class);
                startActivity(intent);
            }
        });
        bt_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ResActivity.class);
                startActivity(intent);
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                tv_card.setText(str);
            }
        };

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {

                                str = s;
                                if(new Tools(db).query(str)){
                                    flag = true;
                                }else{
                                    nono = true;
                                }

                                handler.sendEmptyMessage(0);
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if(nono){
                        try {
                            modbus4150.ctrlRelay(7,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        nono = false;
                        str = "";
                        handler.sendEmptyMessage(0);
                    }
                    if(flag){
                        try {
                            modbus4150.ctrlRelay(2,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.ctrlRelay(3,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.ctrlRelay(3,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            modbus4150.ctrlRelay(2,true,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        flag = false;
                        str = "";
                        handler.sendEmptyMessage(0);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(rfid!=null)
            rfid.stopConnect();
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }
}
