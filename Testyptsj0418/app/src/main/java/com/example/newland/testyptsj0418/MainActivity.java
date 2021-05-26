package com.example.newland.testyptsj0418;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class MainActivity extends AppCompatActivity {
    NetWorkBusiness netWorkBusiness;
    String Token = "";
    TextView tv_wd;
    ImageView iv_fan;
    Handler handler;
    Thread thread;
    boolean fan1, fan2;
    double wd = 0;
    boolean fan_sw;
    int x = 0;
    DecimalFormat df = new DecimalFormat("0");

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        iv_fan = findViewById(R.id.ivfan);

        init();
        iv_fan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fan_sw) {
                    netWorkBusiness.control("236179", "z_fan", 1, new NCallBack<BaseResponseEntity>(MainActivity.this) {
                        @Override
                        protected void onResponse(BaseResponseEntity response) {
                            System.out.println(response.getMsg());
                        }
                    });
                    for (int i = 0; i < 1000; i++) ;
                    netWorkBusiness.control("236179", "m_fan", 1, new NCallBack<BaseResponseEntity>(MainActivity.this) {
                        @Override
                        protected void onResponse(BaseResponseEntity response) {
                            System.out.println(response.getMsg());
                        }
                    });
                    iv_fan.setImageResource(R.drawable.co_fan_open);
                } else {
                    netWorkBusiness.control("236179", "z_fan", 0, new NCallBack<BaseResponseEntity>(MainActivity.this) {
                        @Override
                        protected void onResponse(BaseResponseEntity response) {
                            System.out.println(response.getMsg());
                        }
                    });
                    for (int i = 0; i < 1000; i++) ;
                    netWorkBusiness.control("236179", "m_fan", 0, new NCallBack<BaseResponseEntity>(MainActivity.this) {
                        @Override
                        protected void onResponse(BaseResponseEntity response) {
                            System.out.println(response.getMsg());
                        }
                    });
                    iv_fan.setImageResource(R.drawable.co_fan_close);
                }
                fan_sw = !fan_sw;
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                netWorkBusiness = new NetWorkBusiness(Token, "http://api.nlecloud.com");

                netWorkBusiness.getSensor("236179", "z_temperature", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                        if (response.getStatus() == 0) {
                            wd = Double.parseDouble(response.getResultObj().getValue());
                        }
                    }
                });
                netWorkBusiness.getSensor("236179", "z_fan", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                        if (response.getStatus() == 0) {
                            fan1 = Boolean.parseBoolean(response.getResultObj().getValue());
                        }
                    }
                });
                netWorkBusiness.getSensor("236179", "m_fan", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
                    @Override
                    protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                        if (response.getStatus() == 0) {
                            fan2 = Boolean.parseBoolean(response.getResultObj().getValue());
                        }
                    }
                });
                tv_wd.setText("" + df.format(wd));
                x++;
                if (x == 1) {
                    if (!fan1 && !fan2) {
                        iv_fan.setImageResource(R.drawable.co_fan_close);
                        fan_sw = false;
                    } else if (fan1 || fan2) {
                        iv_fan.setImageResource(R.drawable.co_fan_open);
                        fan_sw = true;
                    }
                    x = 0;
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
    }

    public void init() {
        netWorkBusiness = new NetWorkBusiness("", "http://api.nlecloud.com");
        netWorkBusiness.signIn(new SignIn("18316461896", "asdffdsa"), new NCallBack<BaseResponseEntity<User>>(MainActivity.this) {
            @Override
            protected void onResponse(BaseResponseEntity<User> response) {
                if (response.getStatus() == 0) {
                    Token = response.getResultObj().getAccessToken();
                    System.out.println(response.getResultObj().getAccessToken());
                }
            }
        });
        netWorkBusiness = new NetWorkBusiness(Token, "http://api.nlecloud.com");
    }
}
