package com.example.newland.newlandcontrolandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

public class SetActivity extends Fragment {
    Button bt_set, bt_zigbee, bt_camera, bt_adam, bt_rfid, bt_led;
    boolean rg_ = true;
    boolean reload = false;
    SharedPreferences dataStorage;
    SharedPreferences.Editor editor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_set, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_set = view.findViewById(R.id.bt_set);
        bt_zigbee = view.findViewById(R.id.bt_set_zigbee);
        bt_camera = view.findViewById(R.id.bt_set_camera);
        bt_adam = view.findViewById(R.id.bt_set_adam);
        bt_rfid = view.findViewById(R.id.bt_set_rfid);
        bt_led = view.findViewById(R.id.bt_set_led);
        dataStorage = getActivity().getSharedPreferences("dataStorage", Context.MODE_PRIVATE);
        editor = dataStorage.edit();
        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View v1 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.set_ip_com, null);
                builder.setView(v1);
                EditText ed_set1 = v1.findViewById(R.id.ed_set1);
                Button bt_confirm = v1.findViewById(R.id.bt_confirm);
                Button bt_cancel = v1.findViewById(R.id.bt_cancel);
                Button bt_reload = v1.findViewById(R.id.bt_reload);
                Switch sw_setip = v1.findViewById(R.id.sw_setip);
                TextView tv_setip = v1.findViewById(R.id.tv_setip);
                TextView tv1 = v1.findViewById(R.id.tv1);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1480;
                lp.height = 920;
                alertDialog.getWindow().setAttributes(lp);

                Information.ip = dataStorage.getString("ip","192.168.1.16");
                Information.control_thread = dataStorage.getBoolean("control_thread",true);
                ed_set1.setText(Information.ip);
                if (Information.control_thread) {
                    sw_setip.setChecked(true);
                    tv_setip.setText("启动");
                } else {
                    sw_setip.setChecked(false);
                    tv_setip.setText("关闭");
                }
                bt_reload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reload = true;
                        MyToast.msg(getContext().getApplicationContext(), "警告：已选中恢复默认设置功能，按确定后生效！！！");
                    }
                });
                bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ed_set1.getText().toString().isEmpty()) {
                            MyToast.msg(view.getContext(), "这波配合的并不是很到位");
                        } else {
                            Information.ip = ed_set1.getText().toString();
                            editor.putString("ip",Information.ip);
                            editor.apply();
                            alertDialog.dismiss();
                        }
                        if (reload) {
                            reload = false;
                            reloadSet();
                            MyToast.msg(getContext().getApplicationContext(), "所有设置已恢复初始值");
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                sw_setip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            Information.control_thread = true;
                            tv_setip.setText("启动");
                            editor.putBoolean("control_thread",Information.control_thread);
                            editor.apply();
                        } else {
                            Information.control_thread = false;
                            tv_setip.setText("关闭");
                            editor.putBoolean("control_thread",Information.control_thread);
                            editor.apply();
                        }
                    }
                });
            }
        });
        bt_zigbee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v_zigbee = LayoutInflater.from(view.getContext()).inflate(R.layout.set_zigbee, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(v_zigbee);
                EditText ed_setid = v_zigbee.findViewById(R.id.ed_zigbee_setid);
                EditText ed_setlight = v_zigbee.findViewById(R.id.ed_zigbee_setlight);
                EditText ed_setfan = v_zigbee.findViewById(R.id.ed_zigbee_setfan);
                EditText ed_setzigbee_port = v_zigbee.findViewById(R.id.ed_zigbee_setport);
                EditText ed_setzigbee_com = v_zigbee.findViewById(R.id.ed_zigbee_setcom);
                TextView tv_zigbee_type = v_zigbee.findViewById(R.id.tv_zigbee_type);
                Switch sw_zigbee_type = v_zigbee.findViewById(R.id.sw_zigbee_type);
                Button bt_confirm = v_zigbee.findViewById(R.id.bt_z_confirm);
                Button bt_cancel = v_zigbee.findViewById(R.id.bt_z_cancel);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1580;
                lp.height = 920;
                alertDialog.getWindow().setAttributes(lp);
                Information.zigbee_id = dataStorage.getInt("zigbee_id",1);
                Information.zigbee_light = dataStorage.getInt("zigbee_light",2);
                Information.zigbee_fan = dataStorage.getInt("zigbee_fan",1);
                Information.zigbee_port = dataStorage.getInt("zigbee_port",951);
                Information.zigbee_com = dataStorage.getInt("zigbee_com",2);
                Information.zigbee_type = dataStorage.getBoolean("zigbee_type",true);
                ed_setid.setText("" + Information.zigbee_id);
                ed_setlight.setText("" + Information.zigbee_light);
                ed_setfan.setText("" + Information.zigbee_fan);
                ed_setzigbee_port.setText("" + Information.zigbee_port);
                ed_setzigbee_com.setText("" + Information.zigbee_com);
                if (Information.zigbee_type) {
                    sw_zigbee_type.setChecked(false);
                    tv_zigbee_type.setText("Socket通讯");
                } else {
                    sw_zigbee_type.setChecked(true);
                    tv_zigbee_type.setText("串口通讯");
                }
                sw_zigbee_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            Information.zigbee_type = true;
                            tv_zigbee_type.setText("Socket通讯");
                            editor.putBoolean("zigbee_type",Information.zigbee_type);
                            editor.apply();
                        } else {
                            Information.zigbee_type = false;
                            tv_zigbee_type.setText("串口通讯");
                            editor.putBoolean("zigbee_type",Information.zigbee_type);
                            editor.apply();
                        }
                    }
                });
                bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ed_setid.getText().toString().isEmpty() || ed_setlight.getText().toString().isEmpty() || ed_setfan.getText().toString().isEmpty()) {
                            MyToast.msg(view.getContext(), "这波配合的并不是很到位");
                        } else {
                            Information.zigbee_id = Integer.parseInt(ed_setid.getText().toString());
                            Information.zigbee_light = Integer.parseInt(ed_setlight.getText().toString());
                            Information.zigbee_fan = Integer.parseInt(ed_setfan.getText().toString());
                            Information.zigbee_port = Integer.parseInt(ed_setzigbee_port.getText().toString());
                            Information.zigbee_com = Integer.parseInt(ed_setzigbee_com.getText().toString());

                            editor.putInt("zigbee_id",Information.zigbee_id);
                            editor.putInt("zigbee_light",Information.zigbee_light);
                            editor.putInt("zigbee_fan",Information.zigbee_fan);
                            editor.putInt("zigbee_port",Information.zigbee_port);
                            editor.putInt("zigbee_com",Information.zigbee_com);
                            editor.apply();
                            alertDialog.dismiss();
                        }
                    }
                });
                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v_camera = LayoutInflater.from(view.getContext()).inflate(R.layout.set_camera, null);
                builder.setView(v_camera);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1580;
                lp.height = 920;
                alertDialog.getWindow().setAttributes(lp);
                TextView tv_camera = v_camera.findViewById(R.id.tv_setcamera);
                Switch sw_camera = v_camera.findViewById(R.id.sw_camera);
                Button bt_setcamera = v_camera.findViewById(R.id.bt_setcamera);
                Information.camera_enable = dataStorage.getBoolean("camera_enable",true);
                if (Information.camera_enable) {
                    sw_camera.setChecked(true);
                    tv_camera.setText("开启中......");
                } else {
                    sw_camera.setChecked(false);
                    tv_camera.setText("关闭中......");
                }
                sw_camera.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            tv_camera.setText("开启中......");
                            Information.camera_enable = true;
                            editor.putBoolean("camera_enable",Information.camera_enable);
                            editor.apply();
                        } else {
                            tv_camera.setText("关闭中......");
                            Information.camera_enable = false;
                            editor.putBoolean("camera_enable",Information.camera_enable);
                            editor.apply();
                        }
                    }
                });
                bt_setcamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        bt_adam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v_adam = LayoutInflater.from(view.getContext()).inflate(R.layout.set_adam, null);
                Switch sw_adam = v_adam.findViewById(R.id.sw_adam_type);
                TextView tv_adam_type = v_adam.findViewById(R.id.tv_adam_type);
                EditText ed_setadam_port = v_adam.findViewById(R.id.ed_adam_setport);
                EditText ed_setadam_com = v_adam.findViewById(R.id.ed_adam_setcom);
                Button bt_adam = v_adam.findViewById(R.id.bt_setadam);
                Button bt_adam_cancel = v_adam.findViewById(R.id.bt_setadam_cancel);
                //input
                Spinner sp_hwds = v_adam.findViewById(R.id.sp_hwds);
                Spinner sp_jjkg = v_adam.findViewById(R.id.sp_jjkg);
                Spinner sp_xckg = v_adam.findViewById(R.id.sp_xckg);
                Spinner sp_wdkg1 = v_adam.findViewById(R.id.sp_wdkg1);
                Spinner sp_wdkg2 = v_adam.findViewById(R.id.sp_wdkg2);
                Spinner sp_ygtc = v_adam.findViewById(R.id.sp_ygtc);
                Spinner sp_rthw = v_adam.findViewById(R.id.sp_rthw);
                //output
                Spinner sp_light = v_adam.findViewById(R.id.sp_light);
                Spinner sp_fan = v_adam.findViewById(R.id.sp_fan);
                Spinner sp_ht = v_adam.findViewById(R.id.sp_ht);
                Spinner sp_qj = v_adam.findViewById(R.id.sp_qj);
                Spinner sp_yellow = v_adam.findViewById(R.id.sp_yellow);
                Spinner sp_green = v_adam.findViewById(R.id.sp_green);
                Spinner sp_red = v_adam.findViewById(R.id.sp_red);
                Spinner sp_bjd = v_adam.findViewById(R.id.sp_bjd);
                builder.setView(v_adam);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1780;
                lp.height = 1020;
                alertDialog.getWindow().setAttributes(lp);

                Information.adam_port = dataStorage.getInt("adam_port",951);
                Information.adam_com = dataStorage.getInt("adam_com",2);
                Information.adam_type = dataStorage.getBoolean("adam_type",true);
                ed_setadam_port.setText("" + Information.adam_port);
                ed_setadam_com.setText("" + Information.adam_com);
                if (Information.adam_type) {
                    sw_adam.setChecked(false);
                    tv_adam_type.setText("Socket通讯");
                } else {
                    sw_adam.setChecked(true);
                    tv_adam_type.setText("串口通讯");
                }
                String[] input = {"0", "1", "2", "3", "4", "5", "6"};
                String[] output = {"0", "1", "2", "3", "4", "5", "6", "7"};
                ArrayAdapter<String> inputAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.item_spinner, input);
                ArrayAdapter<String> outputAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.item_spinner, output);
                sp_hwds.setAdapter(inputAdapter);
                sp_jjkg.setAdapter(inputAdapter);
                sp_xckg.setAdapter(inputAdapter);
                sp_wdkg1.setAdapter(inputAdapter);
                sp_wdkg2.setAdapter(inputAdapter);
                sp_ygtc.setAdapter(inputAdapter);
                sp_rthw.setAdapter(inputAdapter);
                sp_light.setAdapter(outputAdapter);
                sp_fan.setAdapter(outputAdapter);
                sp_ht.setAdapter(outputAdapter);
                sp_qj.setAdapter(outputAdapter);
                sp_yellow.setAdapter(outputAdapter);
                sp_green.setAdapter(outputAdapter);
                sp_red.setAdapter(outputAdapter);
                sp_bjd.setAdapter(outputAdapter);

                Information.hwds = dataStorage.getInt("hwds",0);
                Information.jjkg = dataStorage.getInt("jjkg",1);
                Information.xckg = dataStorage.getInt("xckg",2);
                Information.wdkg1 = dataStorage.getInt("wdkg1",3);
                Information.wdkg2 = dataStorage.getInt("wdkg2",4);
                Information.ygtc = dataStorage.getInt("ygtc",5);
                Information.rthw = dataStorage.getInt("rthw",6);
                Information.light = dataStorage.getInt("light",1);
                Information.fan = dataStorage.getInt("fan",0);
                Information.ht = dataStorage.getInt("ht",3);
                Information.qj = dataStorage.getInt("qj",2);
                Information.yellow = dataStorage.getInt("yellow",6);
                Information.green = dataStorage.getInt("green",5);
                Information.red = dataStorage.getInt("red",4);
                Information.bjd = dataStorage.getInt("bjd",7);
                sp_hwds.setSelection(Information.hwds);
                sp_jjkg.setSelection(Information.jjkg);
                sp_xckg.setSelection(Information.xckg);
                sp_wdkg1.setSelection(Information.wdkg1);
                sp_wdkg2.setSelection(Information.wdkg2);
                sp_ygtc.setSelection(Information.ygtc);
                sp_rthw.setSelection(Information.rthw);
                sp_light.setSelection(Information.light);
                sp_fan.setSelection(Information.fan);
                sp_ht.setSelection(Information.ht);
                sp_qj.setSelection(Information.qj);
                sp_yellow.setSelection(Information.yellow);
                sp_green.setSelection(Information.green);
                sp_red.setSelection(Information.red);
                sp_bjd.setSelection(Information.bjd);
                sw_adam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            Information.adam_type = true;
                            tv_adam_type.setText("Socket通讯");
                            editor.putBoolean("adam_type",Information.adam_type);
                            editor.apply();
                        } else {
                            Information.adam_type = false;
                            tv_adam_type.setText("串口通讯");
                            editor.putBoolean("adam_type",Information.adam_type);
                            editor.apply();
                        }
                    }
                });
                bt_adam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Information.hwds = Integer.parseInt(sp_hwds.getSelectedItem().toString());
                        Information.jjkg = Integer.parseInt(sp_jjkg.getSelectedItem().toString());
                        Information.xckg = Integer.parseInt(sp_xckg.getSelectedItem().toString());
                        Information.wdkg1 = Integer.parseInt(sp_wdkg1.getSelectedItem().toString());
                        Information.wdkg2 = Integer.parseInt(sp_wdkg2.getSelectedItem().toString());
                        Information.ygtc = Integer.parseInt(sp_ygtc.getSelectedItem().toString());
                        Information.rthw = Integer.parseInt(sp_rthw.getSelectedItem().toString());
                        Information.light = Integer.parseInt(sp_light.getSelectedItem().toString());
                        Information.fan = Integer.parseInt(sp_fan.getSelectedItem().toString());
                        Information.ht = Integer.parseInt(sp_ht.getSelectedItem().toString());
                        Information.qj = Integer.parseInt(sp_qj.getSelectedItem().toString());
                        Information.yellow = Integer.parseInt(sp_yellow.getSelectedItem().toString());
                        Information.green = Integer.parseInt(sp_green.getSelectedItem().toString());
                        Information.red = Integer.parseInt(sp_red.getSelectedItem().toString());
                        Information.bjd = Integer.parseInt(sp_bjd.getSelectedItem().toString());
                        Information.adam_port = Integer.parseInt(ed_setadam_port.getText().toString());
                        Information.adam_com = Integer.parseInt(ed_setadam_com.getText().toString());

                        editor.putInt("hwds",Information.hwds);
                        editor.putInt("jjkg",Information.jjkg);
                        editor.putInt("xckg",Information.xckg);
                        editor.putInt("wdkg1",Information.wdkg1);
                        editor.putInt("wdkg2",Information.wdkg2);
                        editor.putInt("ygtc",Information.ygtc);
                        editor.putInt("rthw",Information.rthw);
                        editor.putInt("light",Information.light);
                        editor.putInt("fan",Information.fan);
                        editor.putInt("ht",Information.ht);
                        editor.putInt("qj",Information.qj);
                        editor.putInt("yellow",Information.yellow);
                        editor.putInt("green",Information.green);
                        editor.putInt("red",Information.red);
                        editor.putInt("bjd",Information.bjd);
                        editor.putInt("adam_port",Information.adam_port);
                        editor.putInt("adam_com",Information.adam_com);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
                bt_adam_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        bt_rfid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v_rfid = LayoutInflater.from(view.getContext()).inflate(R.layout.set_rfid, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(v_rfid);
                EditText ed_setrfid_port = v_rfid.findViewById(R.id.ed_rfid_setport);
                EditText ed_setrfid_com = v_rfid.findViewById(R.id.ed_rfid_setcom);
                TextView tv_rfid_type = v_rfid.findViewById(R.id.tv_rfid_type);
                Switch sw_rfid_type = v_rfid.findViewById(R.id.sw_rfid_type);
                Button bt_setrfid = v_rfid.findViewById(R.id.bt_set_rfid);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1580;
                lp.height = 920;
                alertDialog.getWindow().setAttributes(lp);
                Information.rfid_port = dataStorage.getInt("rfid_port",951);
                Information.rfid_com = dataStorage.getInt("rfid_com",2);
                Information.rfid_type = dataStorage.getBoolean("rfid_type",true);
                ed_setrfid_port.setText("" + Information.rfid_port);
                ed_setrfid_com.setText("" + Information.rfid_com);
                if (Information.rfid_type) {
                    sw_rfid_type.setChecked(false);
                    tv_rfid_type.setText("Socket通讯");
                } else {
                    sw_rfid_type.setChecked(true);
                    tv_rfid_type.setText("串口通讯");
                }
                sw_rfid_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            Information.rfid_type = true;
                            tv_rfid_type.setText("Socket通讯");
                            editor.putBoolean("rfid_type",Information.rfid_type);
                            editor.apply();
                        } else {
                            Information.rfid_type = false;
                            tv_rfid_type.setText("串口通讯");
                            editor.putBoolean("rfid_type",Information.rfid_type);
                            editor.apply();
                        }
                    }
                });
                bt_setrfid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Information.rfid_port = Integer.parseInt(ed_setrfid_port.getText().toString());
                        Information.rfid_com = Integer.parseInt(ed_setrfid_com.getText().toString());

                        editor.putInt("rfid_port",Information.rfid_port);
                        editor.putInt("rfid_com",Information.rfid_com);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
            }
        });
        bt_led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v_led = LayoutInflater.from(view.getContext()).inflate(R.layout.set_led, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(v_led);
                EditText ed_setled_port = v_led.findViewById(R.id.ed_led_setport);
                EditText ed_setled_com = v_led.findViewById(R.id.ed_led_setcom);
                TextView tv_led_type = v_led.findViewById(R.id.tv_led_type);
                Switch sw_led_type = v_led.findViewById(R.id.sw_led_type);
                Button bt_setled = v_led.findViewById(R.id.bt_set_led);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getWindow().setGravity(Gravity.CENTER);
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1580;
                lp.height = 920;
                alertDialog.getWindow().setAttributes(lp);
                Information.led_port = dataStorage.getInt("led_port",951);
                Information.led_com = dataStorage.getInt("led_com",2);
                Information.led_type = dataStorage.getBoolean("led_type",true);
                ed_setled_port.setText("" + Information.led_port);
                ed_setled_com.setText("" + Information.led_com);
                if (Information.led_type) {
                    sw_led_type.setChecked(false);
                    tv_led_type.setText("Socket通讯");
                } else {
                    sw_led_type.setChecked(true);
                    tv_led_type.setText("串口通讯");
                }
                sw_led_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (!isChecked) {
                            Information.led_type = true;
                            tv_led_type.setText("Socket通讯");
                            editor.putBoolean("led_type",Information.led_type);
                            editor.apply();
                        } else {
                            Information.led_type = false;
                            tv_led_type.setText("串口通讯");
                            editor.putBoolean("led_type",Information.led_type);
                            editor.apply();
                        }
                    }
                });
                bt_setled.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Information.led_port = Integer.parseInt(ed_setled_port.getText().toString());
                        Information.led_com = Integer.parseInt(ed_setled_com.getText().toString());

                        editor.putInt("led_port",Information.led_port);
                        editor.putInt("led_com",Information.led_com);
                        editor.apply();
                        alertDialog.dismiss();
                    }
                });
            }
        });
    }

    public void reloadSet() {
        Information.ip = "192.168.1.16";
        Information.control_thread = true;
        Information.zigbee_id = 0001;
        Information.zigbee_fan = 1;
        Information.zigbee_light = 2;
        Information.zigbee_port = 951;
        Information.zigbee_com = 2;
        Information.zigbee_type = true;
        Information.adam_port = 951;
        Information.adam_com = 2;
        Information.adam_type = true;
        Information.hwds = 0;
        Information.jjkg = 1;
        Information.xckg = 2;
        Information.wdkg1 = 3;
        Information.wdkg2 = 4;
        Information.ygtc = 5;
        Information.rthw = 6;
        Information.light = 1;
        Information.fan = 0;
        Information.ht = 3;
        Information.qj = 2;
        Information.yellow = 6;
        Information.green = 5;
        Information.red = 4;
        Information.bjd = 7;
        Information.camera_enable = true;
        Information.rfid_port = 951;
        Information.rfid_com = 2;
        Information.rfid_type = true;
        Information.led_port = 951;
        Information.led_com = 2;
        Information.led_type = true;
        editor.clear();
        editor.apply();
    }
}
