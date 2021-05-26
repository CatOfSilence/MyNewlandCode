package com.example.newland.newlandcontrol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;

import nledu.com.ipcamera.PTZ;
import nledu.com.ipcamera.CameraManager;

public class IndexActivity extends Fragment {
    //Index主页面
    ViewPager viewPager;
    ArrayList<View> array, array_adam;
    TabLayout tabLayout;
    View zigbee_view, camera_view, adam_view, rfid_view, led_view, adam_info_view, adam_control_view;
    Thread thread;
    DecimalFormat df;
    Handler zigbee_handler, adam_handler;

    //Zigbee
    TextView tv_temp, tv_hum, tv_light, tv_fire, tv_renti;
    TextView tv_four_temp, tv_four_hum, tv_four_co2, tv_four_noise;
    ImageView im_z1, im_z2, im_z3, im_z4, im_z5, im_z6, im_z7, im_z8, im_z9;
    Button bt_light, bt_fan;
    boolean zigbee_light, zigbee_fan;
    Zigbee zigbee;

    //CAMERA
    Button bt_up, bt_bottom, bt_left, bt_right, bt_c_open, bt_c_close, bt_c_cap;

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
        camera_view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.camera, null);
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

        Zigbee_Activity();
        Camera_Activity();
        Adam_Activity();
        Rfid_Activity();
        Led_Activity();



        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
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
//        thread.start();
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
        if (Infomation.type) {
            zigbee = new Zigbee(DataBusFactory.newSocketDataBus(Infomation.ip, 951));
            MyToast.msg(getActivity().getApplicationContext(), "起飞");
        } else {
            zigbee = new Zigbee(DataBusFactory.newSerialDataBus(Infomation.com, 38400));
            MyToast.msg(getActivity().getApplicationContext(), "啊这");
        }

        zigbee_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    double vals[] = zigbee.getFourEnter();
                    tv_four_temp.setText("" + df.format(FourChannelValConvert.getTemperature(vals[0])));
                    tv_four_hum.setText("" + df.format(FourChannelValConvert.getTemperature(vals[1])));
                    tv_four_co2.setText("" + df.format(FourChannelValConvert.getCO2(vals[2])));
                    tv_four_noise.setText("" + df.format(FourChannelValConvert.getNoice(vals[3])));
                    tv_temp.setText("" + df.format(zigbee.getTmpHum()[0]));
                    tv_hum.setText("" + df.format(zigbee.getTmpHum()[1]));
                    tv_light.setText("" + df.format(zigbee.getLight()));
                    tv_fire.setText("" + df.format(zigbee.getFire()));
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
                        zigbee.ctrlDoubleRelay(Infomation.zigbee_id, Infomation.zigbee_light, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    zigbee_light = false;
                    bt_light.setBackgroundResource(R.drawable.lamp_unpressed);
                    try {
                        zigbee.ctrlDoubleRelay(Infomation.zigbee_id, Infomation.zigbee_light, false, null);
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
                        zigbee.ctrlDoubleRelay(Infomation.zigbee_id, Infomation.zigbee_fan, true, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    zigbee_fan = false;
                    bt_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);
                    try {
                        zigbee.ctrlDoubleRelay(Infomation.zigbee_id, Infomation.zigbee_fan, false, null);
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

        if (Infomation.type) {
            modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus(Infomation.ip, 950));
        } else {
            modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(Infomation.com, 9600));
        }

        adam_handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    modbus4150.getVal(0, val -> tv_hwds.setText("" + val));
                    modbus4150.getVal(1, val -> tv_jj.setText("" + val));
                    modbus4150.getVal(2, val -> tv_xc.setText("" + val));
                    modbus4150.getVal(3, val -> tv_wd1.setText("" + val));
                    modbus4150.getVal(4, val -> tv_wd2.setText("" + val));
                    modbus4150.getVal(5, val -> tv_smoke.setText("" + val));
                    modbus4150.getVal(6, val -> tv_rentihw.setText("" + val));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        adam_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    switch (v.getId()) {
                        case R.id.bt_adam_light:
                            if (adam_light) {
                                adam_light = false;
                                bt_adam_light.setBackgroundResource(R.drawable.lamp_pressed);
                                modbus4150.openRelay(1, null);
                            } else {
                                adam_light = true;
                                bt_adam_light.setBackgroundResource(R.drawable.lamp_unpressed);
                                modbus4150.closeRelay(1, null);
                            }
                            break;
                        case R.id.bt_adam_fan:
                            if (adam_fan) {
                                adam_fan = false;
                                bt_adam_fan.setBackgroundResource(R.drawable.wind_speed_unpressed_pressed);
                                modbus4150.openRelay(0, null);
                            } else {
                                adam_fan = true;
                                bt_adam_fan.setBackgroundResource(R.drawable.wind_speed_unpressed);
                                modbus4150.closeRelay(0, null);
                            }
                            break;
                        case R.id.bt_adam_qj:
                            if (adam_qj) {
                                adam_qj = false;
                                bt_adam_qj.setBackgroundResource(R.drawable.qj_switch_click);
                                modbus4150.openRelay(2, null);
                            } else {
                                adam_qj = true;
                                bt_adam_qj.setBackgroundResource(R.drawable.qj_switch);
                                modbus4150.closeRelay(2, null);
                            }
                            break;
                        case R.id.bt_adam_ht:
                            if (adam_ht) {
                                adam_ht = false;
                                bt_adam_ht.setBackgroundResource(R.drawable.ht_switch_click);
                                modbus4150.openRelay(3, null);
                            } else {
                                adam_ht = true;
                                bt_adam_ht.setBackgroundResource(R.drawable.ht_switch);
                                modbus4150.closeRelay(3, null);
                            }
                            break;
                        case R.id.bt_adam_yellow:
                            if (adam_yellow) {
                                adam_yellow = false;
                                bt_adam_yellow.setBackgroundResource(R.drawable.yellow_switch_click);
                                modbus4150.openRelay(6, null);
                            } else {
                                adam_yellow = true;
                                bt_adam_yellow.setBackgroundResource(R.drawable.yellow_switch);
                                modbus4150.closeRelay(6, null);
                            }
                            break;
                        case R.id.bt_adam_green:
                            if (adam_green) {
                                adam_green = false;
                                bt_adam_green.setBackgroundResource(R.drawable.green_switch_click);
                                modbus4150.openRelay(5, null);
                            } else {
                                adam_green = true;
                                bt_adam_green.setBackgroundResource(R.drawable.green_switch);
                                modbus4150.closeRelay(5, null);
                            }
                            break;
                        case R.id.bt_adam_red:
                            if (adam_red) {
                                adam_red = false;
                                bt_adam_red.setBackgroundResource(R.drawable.red_switch_click);
                                modbus4150.openRelay(4, null);
                            } else {
                                adam_red = true;
                                bt_adam_red.setBackgroundResource(R.drawable.red_switch);
                                modbus4150.closeRelay(4, null);
                            }
                            break;
                        case R.id.bt_adam_bjd:
                            if (adam_bjd) {
                                adam_bjd = false;
                                bt_adam_bjd.setBackgroundResource(R.drawable.bjd_switch_click);
                                modbus4150.openRelay(7, null);
                            } else {
                                adam_bjd = true;
                                bt_adam_bjd.setBackgroundResource(R.drawable.bjd_switch);
                                modbus4150.closeRelay(7, null);
                            }
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        camera_view.findViewById(R.id.bt_up).setBackgroundResource(R.drawable.bt_up);
        camera_view.findViewById(R.id.bt_down).setBackgroundResource(R.drawable.bt_down);
        camera_view.findViewById(R.id.bt_left).setBackgroundResource(R.drawable.bt_left);
        camera_view.findViewById(R.id.bt_right).setBackgroundResource(R.drawable.bt_right);
        camera_view.findViewById(R.id.bt_open).setBackgroundResource(R.drawable.click);
        camera_view.findViewById(R.id.bt_close).setBackgroundResource(R.drawable.click);
        camera_view.findViewById(R.id.bt_capture).setBackgroundResource(R.drawable.click);
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

        if (Infomation.type) {
            rfid = new RFID(DataBusFactory.newSocketDataBus(Infomation.ip, 952));
        } else {
            rfid = new RFID(DataBusFactory.newSerialDataBus(Infomation.com, 115200));
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

    private void Led_Activity() {
        bt_led_open = led_view.findViewById(R.id.bt_led_open);
        bt_led_close = led_view.findViewById(R.id.bt_led_close);
        bt_weizhi = led_view.findViewById(R.id.bt_weizhi);
        bt_speed = led_view.findViewById(R.id.bt_speed);
        bt_stoptime = led_view.findViewById(R.id.bt_stoptime);
        bt_time = led_view.findViewById(R.id.bt_time);
        bt_send = led_view.findViewById(R.id.bt_send);
        bt_led_open.setBackgroundResource(R.drawable.click2);
        bt_led_close.setBackgroundResource(R.drawable.click2);
        bt_weizhi.setBackgroundResource(R.drawable.click2);
        bt_speed.setBackgroundResource(R.drawable.click2);
        bt_stoptime.setBackgroundResource(R.drawable.click2);
        bt_time.setBackgroundResource(R.drawable.click2);
        bt_send.setBackgroundResource(R.drawable.click2);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
        if (rfid != null) {
            rfid.stopConnect();
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
