package com.example.newland.testzdkz0417;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;


public class MainActivity extends AppCompatActivity {

    Modbus4150 modbus4150;
    ImageView iv_fan;
    TextView tv_wd;
    Myhttp myhttp;
    Handler handler;
    Thread thread;
    String wd,fan;
    int wd_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_fan = findViewById(R.id.iv_fan);
        tv_wd = findViewById(R.id.tv_wd);
        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.21.17.16",2001),null);

        myhttp = new Myhttp();
        myhttp.test(this);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (wd != null) {
                        JSONObject js = new JSONObject(wd);
                        wd_ = js.getJSONObject("ResultObj").getInt("Value");
                        tv_wd.setText(""+wd_);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(wd_ > 30){
                    try {
                        modbus4150.ctrlRelay(0,true,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    iv_fan.setImageResource(R.drawable.co_fan_open);
                }else if(wd_ < 30){
                    try {
                        modbus4150.ctrlRelay(0,false,null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    iv_fan.setImageResource(R.drawable.co_fan_close);
                }
            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        wd = myhttp.getData("z_temperature");
                        myhttp.test(MainActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(0);
                }
            }
        });
        thread.start();
    }
}
