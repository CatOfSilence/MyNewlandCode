package com.example.hj;

import java.util.ArrayList;

import org.json.JSONException;

import lib.Json_data;
import lib.SocketThread;
import lib.json_dispose;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Vmain extends Activity {
    ViewPager viewPager;
    ArrayList<View> views;
    PagerAdapter pagerAdapter;
    RadioButton rb1, rb2, rb3;
    json_dispose js = new json_dispose();
    int count = 1;


    //V1
    TextView tv_wendu, tv_shidu, tv_ranqi, tv_yanwu, tv_guangzhao, tv_pm25, tv_qiya, tv_co2, tv_rthw;
    Handler hd1;

    //V2
    ImageView iv_lamp, iv_fan, iv_bjd, iv_mj, iv_td1, iv_td2, iv_td3, iv_cl1, iv_cl2, iv_cl3;
    boolean lamp, fan, bjd, mj, td1, td2, td3, cl1, cl2, cl3;

    //V3
    CheckBox ch1, ch2, ch3, ch_lamp, ch_fan, ch_bjd, ch_mj, ch_td1, ch_td2, ch_td3, ch_cl;
    Spinner sp1, sp2;
    EditText ed_v3;
    Handler hd2;
    float yz, rthw, ranqi, wendu;
    boolean tj = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vmain);
        viewPager = (ViewPager) findViewById(R.id.ViewPager);
        rb1 = (RadioButton) findViewById(R.id.radioButton1);
        rb2 = (RadioButton) findViewById(R.id.radioButton2);
        rb3 = (RadioButton) findViewById(R.id.radioButton3);

        View v1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.v1, null);
        View v2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.v2, null);
        View v3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.v3, null);

        //V1
        tv_wendu = (TextView) v1.findViewById(R.id.TextView_v1_wendu);
        tv_shidu = (TextView) v1.findViewById(R.id.TextView_v1_shidu);
        tv_yanwu = (TextView) v1.findViewById(R.id.TextView_v1_yanwu);
        tv_ranqi = (TextView) v1.findViewById(R.id.TextView_v1_ranqi);
        tv_guangzhao = (TextView) v1.findViewById(R.id.TextView_v1_guangzhao);
        tv_qiya = (TextView) v1.findViewById(R.id.TextView_v1_qiya);
        tv_co2 = (TextView) v1.findViewById(R.id.TextView_v1_co2);
        tv_pm25 = (TextView) v1.findViewById(R.id.TextView_v1_PM25);
        tv_rthw = (TextView) v1.findViewById(R.id.TextView_v1_rthw);

        //V2
        iv_lamp = (ImageView) v2.findViewById(R.id.ImageView_lamp);
        iv_fan = (ImageView) v2.findViewById(R.id.ImageView_fan);
        iv_bjd = (ImageView) v2.findViewById(R.id.ImageView_bjd);
        iv_mj = (ImageView) v2.findViewById(R.id.ImageView_mj);
        iv_td1 = (ImageView) v2.findViewById(R.id.ImageView_td1);
        iv_td2 = (ImageView) v2.findViewById(R.id.ImageView_td2);
        iv_td3 = (ImageView) v2.findViewById(R.id.ImageView_td3);
        iv_cl1 = (ImageView) v2.findViewById(R.id.ImageView_cl1);
        iv_cl2 = (ImageView) v2.findViewById(R.id.ImageView_cl2);
        iv_cl3 = (ImageView) v2.findViewById(R.id.ImageView_cl3);

        //V3
        ch1 = (CheckBox) v3.findViewById(R.id.CheckBox_v3_1);
        ch2 = (CheckBox) v3.findViewById(R.id.CheckBox_v3_2);
        ch3 = (CheckBox) v3.findViewById(R.id.CheckBox_v3_3);
        ch_lamp = (CheckBox) v3.findViewById(R.id.CheckBox_lamp);
        ch_fan = (CheckBox) v3.findViewById(R.id.CheckBox_fan);
        ch_bjd = (CheckBox) v3.findViewById(R.id.CheckBox_bjd);
        ch_mj = (CheckBox) v3.findViewById(R.id.CheckBox_mj);
        ch_td1 = (CheckBox) v3.findViewById(R.id.CheckBox_td1);
        ch_td2 = (CheckBox) v3.findViewById(R.id.CheckBox_td2);
        ch_td3 = (CheckBox) v3.findViewById(R.id.CheckBox_td3);
        ch_cl = (CheckBox) v3.findViewById(R.id.CheckBox_cl);
        sp1 = (Spinner) v3.findViewById(R.id.Spinner_1);
        sp2 = (Spinner) v3.findViewById(R.id.Spinner_2);
        ed_v3 = (EditText) v3.findViewById(R.id.EditText_v3);


        views = new ArrayList<View>();
        views.add(v1);
        views.add(v2);
        views.add(v3);


        pagerAdapter = new PagerAdapter() {

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));
                return views.get(position);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);

        SocketThread.mHandlerSocketState = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                if (count == 1) {
                    if (bundle.getString("SocketThread_State") == "error") {
                        Toast.makeText(getApplicationContext(), "网络连接失败", 100).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "网络连接成功", 1000).show();
                        count = 0;
                    }
                }
            }
        };
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        rb1.setChecked(true);
                        rb2.setChecked(false);
                        rb3.setChecked(false);
                        break;
                    case 1:
                        rb1.setChecked(false);
                        rb2.setChecked(true);
                        rb3.setChecked(false);
                        break;
                    case 2:
                        rb1.setChecked(false);
                        rb2.setChecked(false);
                        rb3.setChecked(true);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        rb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    viewPager.setCurrentItem(0);
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });

        rb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    viewPager.setCurrentItem(1);
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });

        rb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    viewPager.setCurrentItem(2);
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                }
            }
        });

        //V1
        hd1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                js.receive();
                try {
                    rthw = Float.valueOf(js.receive_data.get(Json_data.StateHumanInfrared).toString());
                    ranqi = Float.valueOf(js.receive_data.get(Json_data.Gas).toString());
                    wendu = Float.valueOf(js.receive_data.get(Json_data.Temp).toString());
                    tv_wendu.setText(js.receive_data.get(Json_data.Temp).toString());
                    tv_shidu.setText(js.receive_data.get(Json_data.Humidity).toString());
                    tv_yanwu.setText(js.receive_data.get(Json_data.Smoke).toString());
                    tv_ranqi.setText(js.receive_data.get(Json_data.Gas).toString());
                    tv_guangzhao.setText(js.receive_data.get(Json_data.Illumination).toString());
                    tv_qiya.setText(js.receive_data.get(Json_data.AirPressure).toString());
                    tv_pm25.setText(js.receive_data.get(Json_data.PM25).toString());
                    tv_co2.setText(js.receive_data.get(Json_data.Co2).toString());
                    if (Float.valueOf(js.receive_data.get(Json_data.StateHumanInfrared).toString()) > 0) {
                        tv_rthw.setText("有人");
                    } else {
                        tv_rthw.setText("无人");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    hd1.sendEmptyMessage(0);
                }
            }
        }).start();

        //V2
        iv_lamp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!lamp) {
                    lamp = true;
                    iv_lamp.setImageResource(R.drawable.lamp_pressed);
                    js.control(Json_data.Lamp, 0, 1);
                } else {
                    lamp = false;
                    iv_lamp.setImageResource(R.drawable.lamp_unpressed);
                    js.control(Json_data.Lamp, 0, 0);
                }
            }
        });
        iv_fan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!fan) {
                    fan = true;
                    iv_fan.setImageResource(R.drawable.wind_speed_pressed);
                    js.control(Json_data.Fan, 0, 1);
                } else {
                    fan = false;
                    iv_fan.setImageResource(R.drawable.wind_speed_unpressed);
                    js.control(Json_data.Fan, 0, 0);
                }
            }
        });
        iv_bjd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!bjd) {
                    bjd = true;
                    iv_bjd.setImageResource(R.drawable.alarm_pressed);
                    js.control(Json_data.WarningLight, 0, 1);
                } else {
                    bjd = false;
                    iv_bjd.setImageResource(R.drawable.alarm_unpressed);
                    js.control(Json_data.WarningLight, 0, 0);
                }
            }
        });
        iv_mj.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!mj) {
                    mj = true;
                    iv_mj.setImageResource(R.drawable.door_control_pressed);
                    js.control(Json_data.RFID_Open_Door, 0, 1);
                } else {
                    mj = false;
                    iv_mj.setImageResource(R.drawable.door_control_unpressed);
                    js.control(Json_data.RFID_Open_Door, 0, 0);
                }
            }
        });
        iv_td1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!td1) {
                    td1 = true;
                    iv_td1.setImageResource(R.drawable.tongdao1_pressed);
                    js.control(Json_data.InfraredLaunch, 0, 5);
                } else {
                    td1 = false;
                    iv_td1.setImageResource(R.drawable.tongdao1_unpressed);
                    js.control(Json_data.InfraredLaunch, 0, 5);
                }
            }
        });
        iv_td2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!td2) {
                    td2 = true;
                    iv_td2.setImageResource(R.drawable.tongdao2_pressed);
                    js.control(Json_data.InfraredLaunch, 0, 1);
                } else {
                    td2 = false;
                    iv_td2.setImageResource(R.drawable.tongdao2_unpressed);
                    js.control(Json_data.InfraredLaunch, 0, 1);
                }
            }
        });
        iv_td3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!td3) {
                    td3 = true;
                    iv_td3.setImageResource(R.drawable.tongdao3_pressed);
                    js.control(Json_data.InfraredLaunch, 0, 10);
                } else {
                    td3 = false;
                    iv_td3.setImageResource(R.drawable.tongdao3_unpressed);
                    js.control(Json_data.InfraredLaunch, 0, 10);
                }
            }
        });
        iv_cl1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!cl1) {
                    cl1 = true;
                    iv_cl1.setImageResource(R.drawable.curtain_open_pressed);
                    js.control(Json_data.Curtain, 0, 1);
                } else {
                    cl1 = false;
                    iv_cl1.setImageResource(R.drawable.curtain_open_unpressed);
                    js.control(Json_data.Curtain, 0, 1);
                }
            }
        });
        iv_cl2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!cl2) {
                    cl2 = true;
                    iv_cl2.setImageResource(R.drawable.curtain_stop_pressed);
                    js.control(Json_data.Curtain, 0, 2);
                } else {
                    cl2 = false;
                    iv_cl2.setImageResource(R.drawable.curtain_stop_unpressed);
                    js.control(Json_data.Curtain, 0, 2);
                }
            }
        });
        iv_cl3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!cl3) {
                    cl3 = true;
                    iv_cl3.setImageResource(R.drawable.curtain_close_pressed);
                    js.control(Json_data.Curtain, 0, 0);
                } else {
                    cl3 = false;
                    iv_cl3.setImageResource(R.drawable.curtain_close_unpressed);
                    js.control(Json_data.Curtain, 0, 0);
                }
            }

        });

        ch3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    try {
                        yz = Float.valueOf(ed_v3.getText().toString().trim());
                    } catch (Exception e) {
                        yz = 0;
                    }
                }
            }
        });

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        if (ch1.isChecked()) {
                            if (rthw > 0) {
                                js.control(Json_data.WarningLight, 0, 1);
                            }
                        }
                        if (ch2.isChecked()) {
                            if (rthw > 0 || ranqi >= 800) {
                                js.control(Json_data.WarningLight, 0, 1);
                            }
                        }
                        if (ch3.isChecked()) {
                            switch (sp2.getSelectedItemPosition()) {
                                case 0:
                                    tj = wendu > yz;
                                    break;
                                case 1:
                                    tj = wendu <= yz;
                                    break;

                                default:
                                    break;
                            }
                            if (tj) {
                                if (ch_lamp.isChecked()) {
                                    js.control(Json_data.Lamp, 0, 1);
                                }
                                if (ch_fan.isChecked()) {
                                    js.control(Json_data.Fan, 0, 1);
                                }
                                if (ch_bjd.isChecked()) {
                                    js.control(Json_data.WarningLight, 0, 1);
                                }
                                if (ch_mj.isChecked()) {
                                    js.control(Json_data.RFID_Open_Door, 0, 1);
                                }
                                if (ch_td1.isChecked()) {
                                    js.control(Json_data.InfraredLaunch, 0, 5);
                                }
                                if (ch_td2.isChecked()) {
                                    js.control(Json_data.InfraredLaunch, 0, 1);
                                }
                                if (ch_td3.isChecked()) {
                                    js.control(Json_data.InfraredLaunch, 0, 10);
                                }
                                if (ch_cl.isChecked()) {
                                    js.control(Json_data.Curtain, 0, 1);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vmain, menu);
        return true;
    }

}
