package com.newland.smartpark.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.utils.SharePreferenceUtil;


/**
 * 设置阈值对话框
 */

public class SettingCameraDialog extends Dialog {

    private EditText etUserName,etPWD,etIP,etChannel;
    private Button btnCancel;
    private Button btnConfirm;
    private String camUserName,camPwd,camChannel,camIP;
    private Context context;
    private SharePreferenceUtil sharePreferenceUtil;


    public SettingCameraDialog(@NonNull Context context) {
        super(context,R.style.Dialog);
        //关联布局文件
        this.setContentView(R.layout.dialog_setting_camera);
        this.context=context;
        sharePreferenceUtil=SharePreferenceUtil.getInstant(context);
        //初始化组件
        initView();
        loadData();
        addListener();
    }


    private void initView() {
        etUserName=findViewById(R.id.et_video_username);
        etPWD=findViewById(R.id.et_video_pwd);
        etIP=findViewById(R.id.et_video_ip);
        etChannel=findViewById(R.id.et_video_channel);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm =  findViewById(R.id.btn_confirm);
    }
    private void loadData() {
        String ip = sharePreferenceUtil.getCamIP();
        String userName = sharePreferenceUtil.getCamUserName();
        String pwd = sharePreferenceUtil.getCamPWD();
        String channel = sharePreferenceUtil.getCamChannel();
        etIP.setText(ip);
        etUserName.setText(userName);
        etPWD.setText(pwd);
        etChannel.setText(channel);

    }
    //判断摄像头信息是否为空
    private boolean isCameraDataNull() {
        camUserName = etUserName.getText().toString().trim();
        camPwd = etPWD.getText().toString().trim();
        camIP = etIP.getText().toString().trim();
        camChannel = etChannel.getText().toString().trim();
        if (camUserName.isEmpty()) {
            Toast.makeText(context, "摄像头的用户名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (camPwd.isEmpty()) {
            Toast.makeText(context, "摄像头的密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (camIP.isEmpty()) {
            Toast.makeText(context, "摄像头的IP不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (camChannel.isEmpty()) {
            Toast.makeText(context, "摄像头的Channel不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void addListener() {
        //设置确定点击事件
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCameraDataNull()){
                    return;
                }
                sharePreferenceUtil.setCamIP(camIP);
                sharePreferenceUtil.setCamUserName(camUserName);
                sharePreferenceUtil.setCamPWD(camPwd);
                sharePreferenceUtil.setCamChannel(camChannel);
                //对话框消失
                SettingCameraDialog.this.dismiss();
            }
        });
        //设置取消点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框消失
                SettingCameraDialog.this.dismiss();
            }
        });
    }


}
