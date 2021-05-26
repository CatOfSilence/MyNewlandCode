package com.newland.smartpark.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.ecloud.DataCache;


/**
 * 设置云平台对话框 域名 端口 帐号 密码
 */

public class SettingNLECloudDialog extends Dialog {
    //云平台参数设置界面的各项控件
    private EditText loginnameYun, passwordYun, addressYun, portYun;
    private EditText gatewayDeviceIdLoRa, carbonMonoxideDeviceIdNB, flammableGasDeviceIdNB;
    private Button btn_yun_cancel;//取消按钮
    private Button btn_yun_confirm;//保存按钮
    private Context context;
    //用于记录各项参数的输入值
    private  String loginname;
    private  String password;
    private String address;
    private String port;
    private String loraid;
    private String nbid1;
    private String nbid2;
    private  DataCache dataCache;//缓存类对象

    public SettingNLECloudDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
        //关联布局文件
        this.setContentView(R.layout.dialog_setting_nlecloud);
        this.context = context;
        dataCache = new DataCache(context);
        //初始化组件
        initView();
        initData();
        addListener();
    }

    //查找控件
    private void initView() {
        loginnameYun = findViewById(R.id.loginnameYun);
        passwordYun = findViewById(R.id.passwordYun);
        addressYun = findViewById(R.id.addressYun);
        portYun = findViewById(R.id.portYun);
        gatewayDeviceIdLoRa = findViewById(R.id.gatewayDeviceIdLoRa);
        carbonMonoxideDeviceIdNB = findViewById(R.id.carbonMonoxideDeviceIdNB);
        flammableGasDeviceIdNB = findViewById(R.id.flammableGasDeviceIdNB);
        btn_yun_confirm = findViewById(R.id.btn_yun_confirm);
        btn_yun_cancel = findViewById(R.id.btn_yun_cancel);
    }

    //初始化数据
    private void initData() {
        loginnameYun.setText(dataCache.getLoginname());
        passwordYun.setText(dataCache.getPassword());
        addressYun.setText(dataCache.getAddress());
        portYun.setText(dataCache.getPort());
        gatewayDeviceIdLoRa.setText(dataCache.getGatewayDeviceIdLoRa());
        carbonMonoxideDeviceIdNB.setText(dataCache.getCarbonMonoxideDeviceIdNB());
        flammableGasDeviceIdNB.setText(dataCache.getFlammableGasDeviceIdNB());
    }

    //判断各项输入是否为空
    private boolean isInputNull() {
        loginname = loginnameYun.getText().toString().trim();
        password = passwordYun.getText().toString().trim();
        address = addressYun.getText().toString().trim();
        port = portYun.getText().toString().trim();
        loraid = gatewayDeviceIdLoRa.getText().toString().trim();
        nbid1 = carbonMonoxideDeviceIdNB.getText().toString().trim();
        nbid2 = flammableGasDeviceIdNB.getText().toString().trim();

        if (loginname.isEmpty() || loginname.length() == 0) {
            Toast.makeText(context, "用户名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.isEmpty() || password.length() == 0) {
            Toast.makeText(context, "密码不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.isEmpty() || address.length() == 0) {
            Toast.makeText(context, "云平台的域名不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (port.isEmpty() || port.length() == 0) {
            Toast.makeText(context, "云台平的端口不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (loraid.isEmpty() || loraid.length() == 0) {
            Toast.makeText(context, "云台平的端口不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nbid1.isEmpty() || nbid1.length() == 0) {
            Toast.makeText(context, "一氧化碳(NB)ID不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (nbid2.isEmpty() || nbid2.length() == 0) {
            Toast.makeText(context, "甲烷(NB)ID不能为空",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addListener() {
        //设置确定点击事件
        btn_yun_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputNull()) {//如果输入为空
                    return;
                }

                //保存云平台地址和端口号
                dataCache.saveECloudConnParam(address, port, loginname, password, loraid, nbid1, nbid2);
                Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();

                //对话框消失
                SettingNLECloudDialog.this.dismiss();
            }
        });
        //设置取消点击事件
        btn_yun_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框消失
                SettingNLECloudDialog.this.dismiss();
            }
        });
    }
}
