package com.example.newland.testzwdj0407_;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {

    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    Thread thread;
    Handler handler;
    SQLiteDatabase db;
    RFID rfid;
    boolean flag,sw;
    String str;
    Modbus4150 modbus4150;
    LedScreen ledScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.21.7.16",2001),null);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.21.7.16",2004),null);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.21.7.16", 2003), null);
        Mysql mysql = new Mysql(MainActivity.this, "data", null, 1);
        db = mysql.getWritableDatabase();

        Cursor cursor = db.query("data", null, null, null, null, null, null);
        String fuck = "";
        db.delete("data",null,null);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (flag) {

                    try {
                        modbus4150.ctrlRelay(7,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ledScreen.switchLed(true,null);
                        ledScreen.sendTxt("此票据已登记",PlayType.NOW,ShowSpeed.SPEED1,1,10,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ContentValues values = new ContentValues();
                    values.put("id", str);
                    if (tv1.getText().toString().isEmpty()) {
                        tv1.setText(str);
                        tv1.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (tv2.getText().toString().isEmpty()) {
                        tv2.setText(str);
                        tv2.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (tv3.getText().toString().isEmpty()) {
                        tv3.setText(str);
                        tv3.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (tv4.getText().toString().isEmpty()) {
                        tv4.setText(str);
                        tv4.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (tv5.getText().toString().isEmpty()) {
                        tv5.setText(str);
                        tv5.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (tv6.getText().toString().isEmpty()) {
                        tv6.setText(str);
                        tv6.setBackgroundResource(R.drawable.kuang_);
                        db.insert("data", null, values);
                        try {
                            modbus4150.ctrlRelay(7,false,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        try {
                            ledScreen.switchLed(true,null);
                            ledScreen.sendTxt("位置已满",PlayType.NOW,ShowSpeed.SPEED1,1,10,null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                Cursor cursor = db.query("data", null, null, null, null, null, null);

                                flag = false;
                                while (cursor.moveToNext()) {
                                    if (s.equals(cursor.getString(cursor.getColumnIndex("id")))) {
                                        flag = true;
                                        break;
                                    }
                                }
                                str = s;
                                sw = true;
                            }

                            @Override
                            public void onFail(Exception e) {
                                try {
                                    modbus4150.ctrlRelay(7,false,null);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(sw) {
                        handler.sendEmptyMessage(0);
                        sw = false;
                    }
                }
            }
        });
        thread.start();

    }
}

class Mysql extends SQLiteOpenHelper {
    public Mysql(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table data(id varchar(20) primary key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
