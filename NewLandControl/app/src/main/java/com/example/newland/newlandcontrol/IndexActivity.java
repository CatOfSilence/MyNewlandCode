package com.example.newland.newlandcontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class IndexActivity extends Fragment {
    //Index主页面
    ViewPager viewPager;
    ArrayList<View> array, array_adam;
    TabLayout tabLayout;
    View zigbee_view, camera_view, adam_view, rfid_view, led_view, adam_info_view, adam_control_view;
    Thread thread;
    DecimalFormat df;
    Handler zigbee_handler, adam_handler;
    SharedPreferences camera_data, ess_data;
    SharedPreferences.Editor camera_data_editor;
    //Zigbee
    TextView tv_temp, tv_hum, tv_light, tv_fire, tv_renti;
    TextView tv_four_temp, tv_four_hum, tv_four_co2, tv_four_noise;
    ImageView im_z1, im_z2, im_z3, im_z4, im_z5, im_z6, im_z7, im_z8, im_z9;
    Button bt_light, bt_fan;
    boolean zigbee_light, zigbee_fan;
    Zigbee zigbee;

    //CAMERA
    EditText ed_c_ip, ed_c_channel, ed_c_username, ed_c_password;
    TextureView textureView;
    CameraManager cameraManager;
    View.OnTouchListener onTouchListener;

    //ADAM
    Modbus4150 modbus4150;
    RadioGroup rg_adam;
    RadioButton rb_adam1, rb_adam2;
    VerticalViewPager viewPager_adam;
    TextView tv_hwds, tv_jj, tv_xc, tv_wd1, tv_wd2, tv_smoke, tv_rentihw;
    ImageView im_a1, im_a2, im_a3, im_a4, im_a5, im_a6, im_a7;
    Button bt_adam_light, bt_adam_fan, bt_adam_qj, bt_adam_ht, bt_adam_yellow, bt_adam_green, bt_adam_red, bt_adam_bjd;
    boolean adam_light = true, adam_fan = true, adam_qj = true, adam_ht = true, adam_yellow = true, adam_green = true, adam_red = true, adam_bjd = true;
    View.OnClickListener adam_click;

    //RFID
    TextView tv_read1, tv_read2;
    Button btread, bt_write, bt_read;
    EditText ed_write;
    RFID rfid;

    //LED
    Button bt_led_open, bt_led_close, bt_weizhi, bt_speed, bt_stoptime, bt_time, bt_send;
    TextView tv_weizhi, tv_speed, tv_stoptime, tv_time;
    LedScreen ledScreen;
    EditText ed_text;
    View.OnClickListener led_button;
    AlertDialog alertDialog;
    PlayType playType = PlayType.LEFT;
    ShowSpeed showSpeed = ShowSpeed.SPEED1;
    int stoptime = 3;
    int time = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_index, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);

        zigbee_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.zigbee, null);
        camera_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.camera, null, false);
        adam_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.adam, null);
        rfid_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.rfid, null);
        led_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.led, null);
        adam_info_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.adam_info, null);
        adam_control_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.adam_control, null);

        array = new ArrayList<>();
        array.add(zigbee_view);
        array.add(camera_view);
        array.add(adam_view);
        array.add(rfid_view);
        array.add(led_view);

        array_adam = new ArrayList<>();
        array_adam.add(adam_info_view);
        array_adam.add(adam_control_view);

        df = new DecimalFormat("0.0");
        String[] content = {"ZigBee", "Camera", "Adam", "RFID", "LED"};
        MyAdapter myAdapter = new MyAdapter(array, content);
        viewPager.setAdapter(myAdapter);
        tabLayout.setupWithViewPager(viewPager);


        BaseData();
        Zigbee_Activity();
        Camera_Activity();
        Adam_Activity();
        Rfid_Activity();
        Led_Activity(view);


        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Information.control_thread) {
                    try {
                        Thread.sleep(200);
                        zigbee_handler.sendEmptyMessage(0);
                        Thread.sleep(500);
                        adam_handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void BaseData() {

        ess_data = getActivity().getSharedPreferences("dataStorage", Context.MODE_PRIVATE);
        Information.ip = ess_data.getString("ip", "192.168.1.16");
        Information.control_thread = ess_data.getBoolean("control_thread", true);
        Information.zigbee_port = ess_data.getInt("zigbee_port", 951);
        Information.zigbee_com = ess_data.getInt("zigbee_com", 2);
        Information.zigbee_type = ess_data.getBoolean("zigbee_type", true);
        Information.adam_port = ess_data.getInt("adam_port", 951);
        Information.adam_com = ess_data.getInt("adam_com", 2);
        Information.adam_type = ess_data.getBoolean("adam_type", true);
        Information.rfid_port = ess_data.getInt("rfid_port", 951);
        Information.rfid_com = ess_data.getInt("rfid_com", 2);
        Information.rfid_type = ess_data.getBoolean("rfid_type", true);
        Information.led_port = ess_data.getInt("led_port", 951);
        Information.led_com = ess_data.getInt("led_com", 2);
        Information.led_type = ess_data.getBoolean("led_type", true);

    }

    private void Zigbee_Activity() {
        tv_temp = zigbee_view.findViewById(R.id.tv_temp);
        tv_hum = zigbee_view.findViewById(R.id.tv_hum);
        tv_light = zigbee_view.findViewById(R.id.tv_light);
        tv_fire = zigbee_view.findViewById(R.id.tv_frie);
        tv_renti = zigbee_view.findViewById(R.id.tv_renti);
        tv_four_temp = zigbee_view.findViewById(R.id.tv_four_temp);
        tv_four_hum = zigbee_view.findViewById(R.id.tv_four_hum);
        tv_four_co2 = zigbee_view.findViewById(R.id.tv_four_co2);
        tv_four_noise = zigbee_view.findViewById(R.id.tv_four_noise);
        bt_light = zigbee_view.findViewById(R.id.bt_light);
        bt_fan = zigbee_view.findViewById(R.id.bt_fan);
        im_z1 = zigbee_view.findViewById(R.id.img1);
        im_z2 = zigbee_view.findViewById(R.id.img2);
        im_z3 = zigbee_view.findViewById(R.id.img3);
        im_z4 = zigbee_view.findViewById(R.id.img4);
        im_z5 = zigbee_view.findViewById(R.id.img5);
        im_z6 = zigbee_view.findViewById(R.id.img6);
        im_z7 = zigbee_view.findViewById(R.id.img7);
        im_z8 = zigbee_view.findViewById(R.id.img8);
        im_z9 = zigbee_view.findViewById(R.id.img9);

        im_z1.setImageResource(R.drawable.img_temp);
        im_z2.setImageResource(R.drawable.collection_dropsofwater);
        im_z3.setImageResource(R.drawable.collection_co2);
        im_z4.setImageResource(R.drawable.img_noise);
        im_z5.setImageResource(R.drawable.img_temp);
        im_z6.setImageResource(R.drawable.collection_dropsofwater);
        im_z7.setImageResource(R.drawable.collection_light);
        im_z8.setImageResource(R.drawable.collection_gas);
        im_z9.setImageResource(R.drawable.collection_infrared_detector);
        bt_light.setBackgroundResource(R.drawable.lamp_unpressed);
        bt_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);


        if (Information.zigbee_type) {
            zigbee = new Zigbee(DataBusFactory.newSocketDataBus(Information.ip, Information.zigbee_port));
        } else {
            zigbee = new Zigbee(DataBusFactory.newSerialDataBus(Information.zigbee_com, 38400));
        }
        zigbee_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                double vals[] = new double[0];
                try {
                    vals = zigbee.getFourEnter();
                    tv_four_temp.setText("" + df.format(FourChannelValConvert.getTemperature(vals[0])));
                    tv_four_hum.setText("" + df.format(FourChannelValConvert.getTemperature(vals[1])));
                    tv_four_co2.setText("" + df.format(FourChannelValConvert.getCO2(vals[2])));
                    tv_four_noise.setText("" + df.format(FourChannelValConvert.getNoice(vals[3])));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_temp.setText("" + df.format(zigbee.getTmpHum()[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_hum.setText("" + df.format(zigbee.getTmpHum()[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_light.setText("" + df.format(zigbee.getLight()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_fire.setText("" + df.format(zigbee.getFire()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    tv_renti.setText("" + df.format(zigbee.getPerson()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        bt_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!zigbee_light) {
                    zigbee_light = true;
                    bt_light.setBackgroundResource(R.drawable.lamp_pressed);
                    try {
                        zigbee.ctrlDoubleRelay(Information.zigbee_id, Information.zigbee_light, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    zigbee_light = false;
                    bt_light.setBackgroundResource(R.drawable.lamp_unpressed);
                    try {
                        zigbee.ctrlDoubleRelay(Information.zigbee_id, Information.zigbee_light, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bt_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!zigbee_fan) {
                    zigbee_fan = true;
                    bt_fan.setBackgroundResource(R.drawable.wind_speed_unpressed_pressed);
                    try {
                        zigbee.ctrlDoubleRelay(Information.zigbee_id, Information.zigbee_fan, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    zigbee_fan = false;
                    bt_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);
                    try {
                        zigbee.ctrlDoubleRelay(Information.zigbee_id, Information.zigbee_fan, false, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void Adam_Activity() {
        viewPager_adam = (VerticalViewPager) adam_view.findViewById(R.id.viewpager_adam);
        rg_adam = adam_view.findViewById(R.id.rg_adam);
        rb_adam1 = adam_view.findViewById(R.id.rb_adam1);
        rb_adam2 = adam_view.findViewById(R.id.rb_adam2);
        bt_adam_light = adam_control_view.findViewById(R.id.bt_adam_light);
        bt_adam_fan = adam_control_view.findViewById(R.id.bt_adam_fan);
        bt_adam_qj = adam_control_view.findViewById(R.id.bt_adam_qj);
        bt_adam_ht = adam_control_view.findViewById(R.id.bt_adam_ht);
        bt_adam_yellow = adam_control_view.findViewById(R.id.bt_adam_yellow);
        bt_adam_green = adam_control_view.findViewById(R.id.bt_adam_green);
        bt_adam_red = adam_control_view.findViewById(R.id.bt_adam_red);
        bt_adam_bjd = adam_control_view.findViewById(R.id.bt_adam_bjd);
        tv_hwds = adam_info_view.findViewById(R.id.tv_hwds);
        tv_jj = adam_info_view.findViewById(R.id.tv_jj_switch);
        tv_xc = adam_info_view.findViewById(R.id.tv_xc_switch);
        tv_wd1 = adam_info_view.findViewById(R.id.tv_wd1);
        tv_wd2 = adam_info_view.findViewById(R.id.tv_wd2);
        tv_smoke = adam_info_view.findViewById(R.id.tv_yw);
        tv_rentihw = adam_info_view.findViewById(R.id.tv_rt);
        im_a1 = adam_info_view.findViewById(R.id.img_adam1);
        im_a2 = adam_info_view.findViewById(R.id.img_adam2);
        im_a3 = adam_info_view.findViewById(R.id.img_adam3);
        im_a4 = adam_info_view.findViewById(R.id.img_adam4);
        im_a5 = adam_info_view.findViewById(R.id.img_adam5);
        im_a6 = adam_info_view.findViewById(R.id.img_adam6);
        im_a7 = adam_info_view.findViewById(R.id.img_adam7);
        im_a1.setImageResource(R.drawable.hwds);
        im_a2.setImageResource(R.drawable.jj_switch);
        im_a3.setImageResource(R.drawable.xc_switch);
        im_a4.setImageResource(R.drawable.wd_switch);
        im_a5.setImageResource(R.drawable.wd_switch);
        im_a6.setImageResource(R.drawable.collection_smoke);
        im_a7.setImageResource(R.drawable.collection_infrared_detector);
        bt_adam_light.setBackgroundResource(R.drawable.lamp_unpressed);
        bt_adam_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);
        bt_adam_ht.setBackgroundResource(R.drawable.ht_switch);
        bt_adam_qj.setBackgroundResource(R.drawable.qj_switch);
        bt_adam_yellow.setBackgroundResource(R.drawable.yellow_switch);
        bt_adam_green.setBackgroundResource(R.drawable.green_switch);
        bt_adam_red.setBackgroundResource(R.drawable.red_switch);
        bt_adam_bjd.setBackgroundResource(R.drawable.bjd_switch);


        String[] adam_content = {"Info", "Control"};
        MyAdapter myAdapter_adam = new MyAdapter(array_adam, adam_content);
        viewPager_adam.setAdapter(myAdapter_adam);

        if (Information.adam_type) {
            modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus(Information.ip, Information.adam_port));
        } else {
            modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(Information.adam_com, 9600));
        }

        adam_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getVal(Information.hwds, val -> tv_hwds.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.jjkg, val -> tv_jj.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.xckg, val -> tv_xc.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.wdkg1, val -> tv_wd1.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.wdkg2, val -> tv_wd2.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.ygtc, val -> tv_smoke.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    modbus4150.getVal(Information.rthw, val -> tv_rentihw.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        adam_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_adam_light:
                        if (adam_light) {
                            adam_light = false;
                            bt_adam_light.setBackgroundResource(R.drawable.lamp_pressed);
                            try {
                                modbus4150.openRelay(Information.light, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_light = true;
                            bt_adam_light.setBackgroundResource(R.drawable.lamp_unpressed);
                            try {
                                modbus4150.closeRelay(Information.light, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_fan:
                        if (adam_fan) {
                            adam_fan = false;
                            bt_adam_fan.setBackgroundResource(R.drawable.wind_speed_unpressed_pressed);
                            try {
                                modbus4150.openRelay(Information.fan, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_fan = true;
                            bt_adam_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);
                            try {
                                modbus4150.closeRelay(Information.fan, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_qj:
                        if (adam_qj) {
                            adam_qj = false;
                            bt_adam_qj.setBackgroundResource(R.drawable.qj_switch_click);
                            try {
                                modbus4150.openRelay(Information.qj, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_qj = true;
                            bt_adam_qj.setBackgroundResource(R.drawable.qj_switch);
                            try {
                                modbus4150.closeRelay(Information.qj, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_ht:
                        if (adam_ht) {
                            adam_ht = false;
                            bt_adam_ht.setBackgroundResource(R.drawable.ht_switch_click);
                            try {
                                modbus4150.openRelay(Information.ht, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_ht = true;
                            bt_adam_ht.setBackgroundResource(R.drawable.ht_switch);
                            try {
                                modbus4150.closeRelay(Information.ht, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_yellow:
                        if (adam_yellow) {
                            adam_yellow = false;
                            bt_adam_yellow.setBackgroundResource(R.drawable.yellow_switch_click);
                            try {
                                modbus4150.openRelay(Information.yellow, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_yellow = true;
                            bt_adam_yellow.setBackgroundResource(R.drawable.yellow_switch);
                            try {
                                modbus4150.closeRelay(Information.yellow, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_green:
                        if (adam_green) {
                            adam_green = false;
                            bt_adam_green.setBackgroundResource(R.drawable.green_switch_click);
                            try {
                                modbus4150.openRelay(Information.green, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_green = true;
                            bt_adam_green.setBackgroundResource(R.drawable.green_switch);
                            try {
                                modbus4150.closeRelay(Information.green, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_red:
                        if (adam_red) {
                            adam_red = false;
                            bt_adam_red.setBackgroundResource(R.drawable.red_switch_click);
                            try {
                                modbus4150.openRelay(Information.red, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_red = true;
                            bt_adam_red.setBackgroundResource(R.drawable.red_switch);
                            try {
                                modbus4150.closeRelay(Information.red, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case R.id.bt_adam_bjd:
                        if (adam_bjd) {
                            adam_bjd = false;
                            bt_adam_bjd.setBackgroundResource(R.drawable.bjd_switch_click);
                            try {
                                modbus4150.openRelay(Information.bjd, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            adam_bjd = true;
                            bt_adam_bjd.setBackgroundResource(R.drawable.bjd_switch);
                            try {
                                modbus4150.closeRelay(Information.bjd, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        };
        bt_adam_light.setOnClickListener(adam_click);
        bt_adam_fan.setOnClickListener(adam_click);
        bt_adam_qj.setOnClickListener(adam_click);
        bt_adam_ht.setOnClickListener(adam_click);
        bt_adam_yellow.setOnClickListener(adam_click);
        bt_adam_green.setOnClickListener(adam_click);
        bt_adam_red.setOnClickListener(adam_click);
        bt_adam_bjd.setOnClickListener(adam_click);

        rg_adam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_adam1:
                        viewPager_adam.setCurrentItem(0);
                        break;
                    case R.id.rb_adam2:
                        viewPager_adam.setCurrentItem(1);
                        break;
                }
            }
        });
        viewPager_adam.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        rb_adam1.setChecked(true);
                        rb_adam2.setChecked(false);
                        break;
                    case 1:
                        rb_adam1.setChecked(false);
                        rb_adam2.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    private void Camera_Activity() {
        Button bt_open = camera_view.findViewById(R.id.bt_open);
        Button bt_close = camera_view.findViewById(R.id.bt_close);
        Button bt_capture = camera_view.findViewById(R.id.bt_capture);

        camera_view.findViewById(R.id.bt_up).setBackgroundResource(R.drawable.bt_up);
        camera_view.findViewById(R.id.bt_down).setBackgroundResource(R.drawable.bt_down);
        camera_view.findViewById(R.id.bt_left).setBackgroundResource(R.drawable.bt_left);
        camera_view.findViewById(R.id.bt_right).setBackgroundResource(R.drawable.bt_right);
        camera_view.findViewById(R.id.bt_open).setBackgroundResource(R.drawable.click);
        camera_view.findViewById(R.id.bt_close).setBackgroundResource(R.drawable.click);
        camera_view.findViewById(R.id.bt_capture).setBackgroundResource(R.drawable.click);
        camera_view.findViewById(R.id.bt_up).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_down).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_left).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_right).setOnTouchListener(onTouchListener);

        textureView = camera_view.findViewById(R.id.tuvv);
        ImageView imageView = camera_view.findViewById(R.id.camera_imageview);
        ed_c_username = camera_view.findViewById(R.id.ed_c_user);
        ed_c_password = camera_view.findViewById(R.id.ed_c_password);
        ed_c_ip = camera_view.findViewById(R.id.ed_c_ip);
        ed_c_channel = camera_view.findViewById(R.id.ed_c_channel);

        camera_data = getActivity().getSharedPreferences("cameraData", Context.MODE_PRIVATE);
        camera_data_editor = camera_data.edit();
        Information.username = camera_data.getString("username", "admin");
        Information.password = camera_data.getString("password", "admin");
        Information.camera_ip = camera_data.getString("ip", "172.19.3.14");
        Information.channel = camera_data.getString("channel", "1");
        ed_c_username.setText(Information.username);
        ed_c_password.setText(Information.password);
        ed_c_ip.setText(Information.camera_ip);
        ed_c_channel.setText(Information.channel);
        View.OnClickListener camera_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_open:
                        Information.username = ed_c_username.getText().toString();
                        Information.password = ed_c_password.getText().toString();
                        Information.camera_ip = ed_c_ip.getText().toString();
                        Information.channel = ed_c_channel.getText().toString();
                        camera_data_editor.putString("username", Information.username);
                        camera_data_editor.putString("password", Information.password);
                        camera_data_editor.putString("ip", Information.camera_ip);
                        camera_data_editor.putString("channel", Information.channel);
                        camera_data_editor.apply();
                        if (Information.camera_enable) {
                            textureView.setVisibility(View.VISIBLE);
                            imageView.setVisibility(View.GONE);
                            if (cameraManager == null) {
                                cameraManager = CameraManager.getInstance();
                                cameraManager.setupInfo(textureView, Information.username, Information.password, Information.camera_ip, Information.channel);
                                cameraManager.openCamera();
                            }
                        }
                        break;
                    case R.id.bt_close:
                        textureView.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);
                        if (cameraManager != null) {
                            cameraManager.releaseCamera();
                            cameraManager = null;
                        }
                        break;
                    case R.id.bt_capture:
                        cameraManager.capture(Environment.getExternalStorageDirectory().getPath(), "capture.png");
                        break;
                }
            }
        };

        bt_open.setOnClickListener(camera_click);
        bt_close.setOnClickListener(camera_click);
        bt_capture.setOnClickListener(camera_click);

        onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                PTZ ptz = null;
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    ptz = PTZ.Stop;
                } else if (action == MotionEvent.ACTION_DOWN) {
                    switch (v.getId()) {
                        case R.id.bt_up:
                            ptz = PTZ.Up;
                            break;
                        case R.id.bt_down:
                            ptz = PTZ.Down;
                            break;
                        case R.id.bt_left:
                            ptz = PTZ.Left;
                            break;
                        case R.id.bt_right:
                            ptz = PTZ.Right;
                            break;
                    }
                }
                cameraManager.controlDir(ptz);
                return false;
            }
        };
        camera_view.findViewById(R.id.bt_up).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_left).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_right).setOnTouchListener(onTouchListener);
        camera_view.findViewById(R.id.bt_down).setOnTouchListener(onTouchListener);
    }

    private void Rfid_Activity() {
        tv_read1 = rfid_view.findViewById(R.id.tv_read1);
        tv_read2 = rfid_view.findViewById(R.id.tv_read2);
        btread = rfid_view.findViewById(R.id.btread);
        bt_write = rfid_view.findViewById(R.id.bt_write);
        bt_read = rfid_view.findViewById(R.id.bt_read);
        ed_write = rfid_view.findViewById(R.id.ed_write);
        bt_write.setBackgroundResource(R.drawable.click2);
        bt_read.setBackgroundResource(R.drawable.click2);
        btread.setBackgroundResource(R.drawable.click2);

        if (Information.rfid_type) {
            rfid = new RFID(DataBusFactory.newSocketDataBus(Information.ip, Information.rfid_port));
        } else {
            rfid = new RFID(DataBusFactory.newSerialDataBus(Information.rfid_com, 115200));
        }

        btread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rfid.readSingleEpc(val -> tv_read1.setText(val.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = ed_write.getText().toString();
                try {
                    rfid.writeData(data, isSuccess -> MyToast.msg(rfid_view.getContext(), isSuccess ? "写入成功" : "写入失败"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bt_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rfid.readData(str -> tv_read2.setText(str.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Led_Activity(View view) {
        bt_led_open = led_view.findViewById(R.id.bt_led_open);
        bt_led_close = led_view.findViewById(R.id.bt_led_close);
        bt_weizhi = led_view.findViewById(R.id.bt_weizhi);
        bt_speed = led_view.findViewById(R.id.bt_speed);
        bt_stoptime = led_view.findViewById(R.id.bt_stoptime);
        bt_time = led_view.findViewById(R.id.bt_time);
        bt_send = led_view.findViewById(R.id.bt_send);
        ed_text = led_view.findViewById(R.id.ed_led);
        tv_weizhi = led_view.findViewById(R.id.tv_weizhi);
        tv_speed = led_view.findViewById(R.id.tv_speed);
        tv_stoptime = led_view.findViewById(R.id.tv_stoptime);
        tv_time = led_view.findViewById(R.id.tv_time);
        bt_led_open.setBackgroundResource(R.drawable.click2);
        bt_led_close.setBackgroundResource(R.drawable.click2);
        bt_weizhi.setBackgroundResource(R.drawable.click2);
        bt_speed.setBackgroundResource(R.drawable.click2);
        bt_stoptime.setBackgroundResource(R.drawable.click2);
        bt_time.setBackgroundResource(R.drawable.click2);
        bt_send.setBackgroundResource(R.drawable.click2);
        if (Information.led_type) {
            ledScreen = new LedScreen(DataBusFactory.newSocketDataBus(Information.ip, Information.led_port));
        } else {
            ledScreen = new LedScreen(DataBusFactory.newSerialDataBus(Information.led_com, 9600));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setCancelable(false);
        String[] weizhi_arr = {"左移", "上移", "下移", "下覆盖", "上覆盖", "翻白显示", "闪烁显示", "立即打出"};
        String[] speed_arr = {"一级", "二级", "三级", "四级", "五级", "六级", "七级", "八级"};
        led_button = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bt_led_open:
                        try {
                            ledScreen.switchLed(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.bt_led_close:
                        try {
                            ledScreen.switchLed(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.bt_weizhi:
                        builder.setTitle("选择移动方式");
                        builder.setSingleChoiceItems(weizhi_arr, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        playType = PlayType.LEFT;
                                        tv_weizhi.setText("左移");
                                        break;
                                    case 1:
                                        playType = PlayType.UP;
                                        tv_weizhi.setText("上移");
                                        break;
                                    case 2:
                                        playType = PlayType.DOWN;
                                        tv_weizhi.setText("下移");
                                        break;
                                    case 3:
                                        playType = PlayType.DOWN_OVER;
                                        tv_weizhi.setText("下覆盖");
                                        break;
                                    case 4:
                                        playType = PlayType.UP_OVER;
                                        tv_weizhi.setText("上覆盖");
                                        break;
                                    case 5:
                                        playType = PlayType.WHITE;
                                        tv_weizhi.setText("翻白显示");
                                        break;
                                    case 6:
                                        playType = PlayType.SPANGLE;
                                        tv_weizhi.setText("闪烁显示");
                                        break;
                                    case 7:
                                        playType = PlayType.NOW;
                                        tv_weizhi.setText("立即打出");
                                        break;
                                }
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        break;
                    case R.id.bt_speed:
                        builder.setTitle("选择移动速度");
                        builder.setSingleChoiceItems(speed_arr, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                switch (i) {
                                    case 0:
                                        showSpeed = ShowSpeed.SPEED1;
                                        tv_speed.setText("一级");
                                        break;
                                    case 1:
                                        showSpeed = ShowSpeed.SPEED2;
                                        tv_speed.setText("二级");
                                        break;
                                    case 2:
                                        showSpeed = ShowSpeed.SPEED3;
                                        tv_speed.setText("三级");
                                        break;
                                    case 3:
                                        showSpeed = ShowSpeed.SPEED4;
                                        tv_speed.setText("四级");
                                        break;
                                    case 4:
                                        showSpeed = ShowSpeed.SPEED5;
                                        tv_speed.setText("五级");
                                        break;
                                    case 5:
                                        showSpeed = ShowSpeed.SPEED6;
                                        tv_speed.setText("六级");
                                        break;
                                    case 6:
                                        showSpeed = ShowSpeed.SPEED7;
                                        tv_speed.setText("七级");
                                        break;
                                    case 7:
                                        showSpeed = ShowSpeed.SPEED8;
                                        tv_speed.setText("八级");
                                        break;
                                }
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        break;
                    case R.id.bt_stoptime:
                        View led_dialog_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.led_dialog, null);
                        AlertDialog.Builder builder_V = new AlertDialog.Builder(view.getContext());
                        builder_V.setView(led_dialog_view);
                        builder_V.setTitle("请设置停止时间");
                        EditText ed_led_dialog = led_dialog_view.findViewById(R.id.ed_led_dialog);
                        Button bt_led_dialog = led_dialog_view.findViewById(R.id.bt_led_dialog);
                        AlertDialog aler1 = builder_V.create();
                        aler1.setCancelable(false);
                        ed_led_dialog.setText("" + stoptime);
                        aler1.show();
                        bt_led_dialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ed_led_dialog.getText().toString().isEmpty()) {
                                    MyToast.msg(getActivity().getApplicationContext(), "内容不能为空");
                                } else {
                                    stoptime = Integer.parseInt(ed_led_dialog.getText().toString());
                                    tv_stoptime.setText("" + stoptime);
                                    aler1.dismiss();
                                }
                            }
                        });
                        break;
                    case R.id.bt_time:
                        View led_dialog_view_ = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.led_dialog, null);
                        AlertDialog.Builder builder_V_ = new AlertDialog.Builder(view.getContext());
                        builder_V_.setView(led_dialog_view_);
                        builder_V_.setTitle("请设置有效时间");
                        EditText ed_led_dialog_ = led_dialog_view_.findViewById(R.id.ed_led_dialog);
                        Button bt_led_dialog_ = led_dialog_view_.findViewById(R.id.bt_led_dialog);
                        AlertDialog aler2 = builder_V_.create();
                        aler2.setCancelable(false);
                        ed_led_dialog_.setText("" + time);
                        aler2.show();
                        bt_led_dialog_.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (ed_led_dialog_.getText().toString().isEmpty()) {
                                    MyToast.msg(getActivity().getApplicationContext(), "内容不能为空");
                                } else {
                                    time = Integer.parseInt(ed_led_dialog_.getText().toString());
                                    tv_time.setText("" + time);
                                    aler2.dismiss();
                                }
                            }
                        });
                        break;
                    case R.id.bt_send:
                        try {
                            if (ed_text.getText().toString().isEmpty()) {
                                MyToast.msg(getActivity().getApplicationContext(), "发送内容不能为空");
                            } else {
                                ledScreen.sendTxt(ed_text.getText().toString(), playType, showSpeed, stoptime, time);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };
        bt_led_open.setOnClickListener(led_button);
        bt_led_close.setOnClickListener(led_button);
        bt_weizhi.setOnClickListener(led_button);
        bt_speed.setOnClickListener(led_button);
        bt_stoptime.setOnClickListener(led_button);
        bt_time.setOnClickListener(led_button);
        bt_send.setOnClickListener(led_button);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
        if (cameraManager != null) {
            cameraManager.releaseCamera();
            cameraManager = null;
        }
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
        if (rfid != null) {
            rfid.stopConnect();
        }
        if (ledScreen != null) {
            ledScreen.stopConnect();
        }
    }
}

class MyAdapter extends PagerAdapter {
    ArrayList<View> array = new ArrayList<>();
    public static String[] content;

    public MyAdapter() {

    }

    public MyAdapter(ArrayList<View> array, String[] content) {
        this.array = array;
        this.content = content;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(array.get(position));
        return array.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(array.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return content[position];
    }
}
