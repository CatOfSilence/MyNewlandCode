package com.example.newland.newlandcontrol;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SetActivity extends Fragment {
    Button bt_set, bt_zigbee, bt_camera, bt_adam, bt_rfid, bt_led;
    boolean rg_ = true;

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

        bt_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                View v1 = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.set_ip_com, null);
                builder.setView(v1);
                RadioGroup rg = v1.findViewById(R.id.rg);
                RadioButton rb1 = v1.findViewById(R.id.rb1);
                RadioButton rb2 = v1.findViewById(R.id.rb2);
                EditText ed_set1 = v1.findViewById(R.id.ed_set1);
                EditText ed_set2 = v1.findViewById(R.id.ed_set2);
                Button bt_confirm = v1.findViewById(R.id.bt_confirm);
                Button bt_cancel = v1.findViewById(R.id.bt_cancel);
                TextView tv1 = v1.findViewById(R.id.tv1);
                TextView tv2 = v1.findViewById(R.id.tv2);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1200;
                lp.height = 608;
                alertDialog.getWindow().setAttributes(lp);

                if (rg_) {
                    rb1.setChecked(true);
                    rb2.setChecked(false);
                    tv1.setText("IP:");
                    tv2.setText("COM:");
                    ed_set1.setInputType(InputType.TYPE_CLASS_TEXT);
                    ed_set1.setText("" + Infomation.ip);
                    ed_set2.setText("" + Infomation.ip_com);
                } else {
                    rb1.setChecked(false);
                    rb2.setChecked(true);
                    tv1.setText("COM:");
                    tv2.setText("波特率:");
                    ed_set1.setInputType(InputType.TYPE_CLASS_NUMBER);
                    ed_set1.setText("" + Infomation.com);
                    ed_set2.setText("" + Infomation.Baud_rate);
                }


                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb1:
                                Infomation.type = true;
                                rg_ = true;
                                tv1.setText("IP:");
                                tv2.setText("COM:");
                                ed_set1.setText("" + Infomation.ip);
                                ed_set2.setText("" + Infomation.ip_com);
                                ed_set1.setInputType(InputType.TYPE_CLASS_TEXT);
                                break;
                            case R.id.rb2:
                                Infomation.type = false;
                                rg_ = false;
                                tv1.setText("COM:");
                                tv2.setText("波特率:");
                                ed_set1.setText("" + Infomation.com);
                                ed_set2.setText("" + Infomation.Baud_rate);
                                ed_set1.setInputType(InputType.TYPE_CLASS_NUMBER);
                                break;
                        }
                    }
                });
                bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ed_set1.getText().toString().isEmpty() || ed_set2.getText().toString().isEmpty()) {
                            MyToast.msg(view.getContext(), "这波配合的并不是很到位");
                        } else {
                            if (rg_) {
                                Infomation.ip = ed_set1.getText().toString();
                                Infomation.ip_com = Integer.parseInt(ed_set2.getText().toString());
                            } else {
                                Infomation.com = Integer.parseInt(ed_set1.getText().toString());
                                Infomation.Baud_rate = Integer.parseInt(ed_set2.getText().toString());
                            }
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
        bt_zigbee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View v_zigbee = LayoutInflater.from(view.getContext()).inflate(R.layout.set_zigbee, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(v_zigbee);

                EditText ed_setid = v_zigbee.findViewById(R.id.ed_setid);
                EditText ed_setlight = v_zigbee.findViewById(R.id.ed_setlight);
                EditText ed_setfan = v_zigbee.findViewById(R.id.ed_setfan);
                Button bt_confirm = v_zigbee.findViewById(R.id.bt_z_confirm);
                Button bt_cancel = v_zigbee.findViewById(R.id.bt_z_cancel);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
                lp.width = 1200;
                lp.height = 608;
                alertDialog.getWindow().setAttributes(lp);
                ed_setid.setText(""+Infomation.zigbee_id);
                ed_setlight.setText(""+Infomation.zigbee_light);
                ed_setfan.setText(""+Infomation.zigbee_fan);
                bt_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ed_setid.getText().toString().isEmpty() || ed_setlight.getText().toString().isEmpty() || ed_setfan.getText().toString().isEmpty()) {
                            MyToast.msg(view.getContext(), "这波配合的并不是很到位");
                        } else {
                            Infomation.zigbee_id = Integer.parseInt(ed_setid.getText().toString());
                            Infomation.zigbee_light = Integer.parseInt(ed_setlight.getText().toString());
                            Infomation.zigbee_fan = Integer.parseInt(ed_setfan.getText().toString());
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
    }
}
