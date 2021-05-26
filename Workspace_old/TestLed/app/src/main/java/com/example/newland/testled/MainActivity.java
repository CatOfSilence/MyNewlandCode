package com.example.newland.testled;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.transfer.DataBus;
import com.nle.mylibrary.transfer.DataBusFactory;


public class MainActivity extends AppCompatActivity {

    public Button.OnClickListener onClickListener;
    public LedScreen ledScreen;
    String MODE = "socket";
    RadioGroup rg;
    EditText ed,ed_led;
    TextView tv_weizhi, tv_speed, tv_stoptime, tv_time;
    Button bt_open, bt_close, bt_weizhi, bt_speed, bt_stoptime, bt_time, bt_send;
    String txt = "没有内容没有内容没内容没内容内容内容";
    PlayType playType = PlayType.UP;
    ShowSpeed showSpeed = ShowSpeed.SPEED1;
    int stoptime = 1, time = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onClick();
        setClick();
        newLed();

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                tv_stoptime.setText(String.valueOf(stoptime));
                tv_time.setText(String.valueOf(time));
                switch (playType) {
                    case LEFT:
                        tv_weizhi.setText("左移");
                        break;
                    case UP:
                        tv_weizhi.setText("上移");
                        break;
                    case DOWN:
                        tv_weizhi.setText("下移");
                        break;
                    case DOWN_OVER:
                        tv_weizhi.setText("下覆盖");
                        break;
                    case UP_OVER:
                        tv_weizhi.setText("上覆盖");
                        break;
                    case WHITE:
                        tv_weizhi.setText("翻白覆盖");
                        break;
                    case SPANGLE:
                        tv_weizhi.setText("闪烁显示");
                        break;
                    case NOW:
                        tv_weizhi.setText("立即打出");
                        break;
                }
                switch (showSpeed) {
                    case SPEED1:
                        tv_speed.setText("一级");
                        break;
                    case SPEED2:
                        tv_speed.setText("二级");
                        break;
                    case SPEED3:
                        tv_speed.setText("三级");
                        break;
                    case SPEED4:
                        tv_speed.setText("四级");
                        break;
                    case SPEED5:
                        tv_speed.setText("五级");
                        break;
                    case SPEED6:
                        tv_speed.setText("六级");
                        break;
                    case SPEED7:
                        tv_speed.setText("七级");
                        break;
                    case SPEED8:
                        tv_speed.setText("八级");
                        break;
                }
            }
        };

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();
    }

    public void newLed() {
        if (MODE.equals("socket")) {
            ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.19.2.16", 953));
        } else {
            ledScreen = new LedScreen(DataBusFactory.newSerialDataBus(2, 9600));
        }

        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.rb_socket:
                    MODE = "socket";
                    ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("172.19.2.16", 953));
                    Toast.makeText(getApplicationContext(), "前端能源启动", Toast.LENGTH_LONG).show();
                    break;
                case R.id.rb_serial:
                    MODE = "serial";
                    ledScreen = new LedScreen(DataBusFactory.newSerialDataBus(2, 9600));
                    Toast.makeText(getApplicationContext(), "后备隐藏能源启动", Toast.LENGTH_LONG).show();
                    break;
            }
        });


    }

    public void setClick() {
        rg = findViewById(R.id.rg_led);
        ed = findViewById(R.id.ed_);
        ed_led = findViewById(R.id.ed_led);
        tv_weizhi = findViewById(R.id.tv_weizhi);
        tv_speed = findViewById(R.id.tv_speed);
        tv_stoptime = findViewById(R.id.tv_stoptime);
        tv_time = findViewById(R.id.tv_time);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        bt_weizhi = findViewById(R.id.bt_weizhi);
        bt_speed = findViewById(R.id.bt_speed);
        bt_stoptime = findViewById(R.id.bt_stoptime);
        bt_time = findViewById(R.id.bt_time);
        bt_send = findViewById(R.id.bt_send);

        bt_open.setOnClickListener(onClickListener);
        bt_close.setOnClickListener(onClickListener);
        bt_weizhi.setOnClickListener(onClickListener);
        bt_speed.setOnClickListener(onClickListener);
        bt_stoptime.setOnClickListener(onClickListener);
        bt_time.setOnClickListener(onClickListener);
        bt_send.setOnClickListener(onClickListener);
    }

    public void onClick() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_alertdialog, null);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity.this);
                EditText ed = v.findViewById(R.id.ed_);
                Button bt = v.findViewById(R.id.bt_set);
                builder3.setView(v);
                switch (view.getId()) {
                    case R.id.bt_open:
                        try {
                            ledScreen.switchLed(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.bt_close:
                        try {
                            ledScreen.switchLed(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.bt_weizhi:
                        String[] weizhi = {"左移", "上移", "下移", "下覆盖", "上覆盖", "翻白显示", "闪烁显示", "立即打出"};
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("选择移动方式：");
                        builder1.setSingleChoiceItems(weizhi, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        playType = PlayType.LEFT;
                                        dialogInterface.dismiss();
                                        break;
                                    case 1:
                                        playType = PlayType.UP;
                                        dialogInterface.dismiss();
                                        break;
                                    case 2:
                                        playType = PlayType.DOWN;
                                        dialogInterface.dismiss();
                                        break;
                                    case 3:
                                        playType = PlayType.DOWN_OVER;
                                        dialogInterface.dismiss();
                                        break;
                                    case 4:
                                        playType = PlayType.UP_OVER;
                                        dialogInterface.dismiss();
                                        break;
                                    case 5:
                                        playType = PlayType.WHITE;
                                        dialogInterface.dismiss();
                                        break;
                                    case 6:
                                        playType = PlayType.SPANGLE;
                                        dialogInterface.dismiss();
                                        break;
                                    case 7:
                                        playType = PlayType.NOW;
                                        dialogInterface.dismiss();
                                        break;
                                }
                            }
                        });
                        builder1.show();
                        break;
                    case R.id.bt_speed:
                        String[] speed = {"一级", "二级", "三级", "四级", "五级", "六级", "七级", "八级"};
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                        builder2.setTitle("选择移动方式：");
                        builder2.setSingleChoiceItems(speed, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        showSpeed = ShowSpeed.SPEED1;
                                        dialogInterface.dismiss();
                                        break;
                                    case 1:
                                        showSpeed = ShowSpeed.SPEED2;
                                        dialogInterface.dismiss();
                                        break;
                                    case 2:
                                        showSpeed = ShowSpeed.SPEED3;
                                        dialogInterface.dismiss();
                                        break;
                                    case 3:
                                        showSpeed = ShowSpeed.SPEED4;
                                        dialogInterface.dismiss();
                                        break;
                                    case 4:
                                        showSpeed = ShowSpeed.SPEED5;
                                        dialogInterface.dismiss();
                                        break;
                                    case 5:
                                        showSpeed = ShowSpeed.SPEED6;
                                        dialogInterface.dismiss();
                                        break;
                                    case 6:
                                        showSpeed = ShowSpeed.SPEED7;
                                        dialogInterface.dismiss();
                                        break;
                                    case 7:
                                        showSpeed = ShowSpeed.SPEED8;
                                        dialogInterface.dismiss();
                                        break;

                                }
                            }
                        });
                        builder2.show();
                        break;
                    case R.id.bt_stoptime:
                        builder3.setTitle("请设置停止时间");
                        builder3.show();
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ed.getText().toString().equals("")) {
                                    ed.setText("请输入有效数值");
                                } else {
                                    stoptime = Integer.parseInt(ed.getText().toString());
                                }

                            }
                        });
                        break;
                    case R.id.bt_time:
                        builder3.setTitle("请设置持续时间");
                        builder3.show();
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ed.getText().toString().equals("")) {
                                    ed.setText("请输入有效数值");
                                } else {
                                    time = Integer.parseInt(ed.getText().toString());
                                }
                            }
                        });
                        break;
                    case R.id.bt_send:
                        try {
                            txt = ed_led.getText().toString();
                            ledScreen.sendTxt(txt, playType, showSpeed, stoptime, time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(ledScreen!=null){
            ledScreen.stopConnect();
        }
    }
}
