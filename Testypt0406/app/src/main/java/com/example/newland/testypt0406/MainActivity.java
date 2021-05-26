package com.example.newland.testypt0406;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView iv;
    boolean flag;
    MyUrl myUrl;
    String str,fan1,fan2;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.iv_fan);
        tv = findViewById(R.id.tv_wd);
        myUrl = new MyUrl();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (str != null) {
                    try {
                        tv.setText(jx(str));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (jx_b(fan1)||jx_b(fan2)) {
                        iv.setImageResource(R.drawable.co_fan_open);
                        flag = true;
                    } else if (!jx_b(fan1)||!jx_b(fan2)) {
                        iv.setImageResource(R.drawable.co_fan_close);
                        flag = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        str = myUrl.getData("z_temperature");
                        Thread.sleep(100);
                        fan1 = myUrl.getData("m_fan");
                        Thread.sleep(100);
                        fan2 = myUrl.getData("z_fan");
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flag){
                    open();
                    iv.setImageResource(R.drawable.co_fan_open);
                    flag = true;
                }else{
                    close();
                    iv.setImageResource(R.drawable.co_fan_close);
                    flag = false;
                }
            }
        });
    }
    public void open(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    myUrl.sendCmd("m_fan","1");
                    for (int i = 0; i < 1000; i++);
                    myUrl.sendCmd("z_fan","1");
                    System.out.println(myUrl.getData("m_fan"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void close(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    myUrl.sendCmd("m_fan","0");
                    for (int i = 0; i < 1000; i++);
                    myUrl.sendCmd("z_fan","0");
                    System.out.println(myUrl.getData("m_fan"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public String jx(String str) throws Exception {
        if(str.length()!=0) {
            JSONObject js = new JSONObject(str);
            JSONObject ResultObj = js.getJSONObject("ResultObj");
            return ResultObj.getString("Value");
        }else{
            return null;
        }
    }
    public boolean jx_b(String str) throws Exception {
        if(str.length()!=0) {
            JSONObject js = new JSONObject(str);
            JSONObject ResultObj = js.getJSONObject("ResultObj");
            return ResultObj.getBoolean("Value");
        }else{
            return false;
        }
    }
}
