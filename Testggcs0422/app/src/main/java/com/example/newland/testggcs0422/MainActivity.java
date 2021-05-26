package com.example.newland.testggcs0422;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_rt, tv_yw;
    Handler handler;
    Thread thread;
    Modbus4150 modbus4150;
    LedScreen ledScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_rt = findViewById(R.id.tv_rt);
        tv_yw = findViewById(R.id.tv_yw);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.set);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.18.22.16", 2001), null);
        ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.18.22.16", 2004), null);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getDIVal(5, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if (i == 0) {
                                tv_yw.setText("无烟雾");
                                try {
                                    modbus4150.ctrlRelay(7, false, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                tv_yw.setText("有烟雾");
                                try {
                                    modbus4150.ctrlRelay(7, true, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFail(Exception e) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getDIVal(6, new MdBus4150SensorListener() {
                        @Override
                        public void onVal(int i) {
                            if (i == 0) {
                                tv_rt.setText("有人");
                                try {
                                    ledScreen.sendTxt("有人", PlayType.NOW, ShowSpeed.SPEED1, 1, 100, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                tv_rt.setText("无人");
                                try {
                                    ledScreen.sendTxt("无人", PlayType.NOW, ShowSpeed.SPEED1, 1, 100, null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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

        try {
            ledScreen.switchLed(true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (modbus4150 != null)
            modbus4150.stopConnect();
        if (ledScreen != null)
            ledScreen.stopConnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.aa:
                startActivity(new Intent(this,SetActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
