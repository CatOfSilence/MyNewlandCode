package com.example.newland.testdthj0405;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    MyHttpUrlConnection conn;
    TextView tv_wd,tv_sd;
    String wd,sd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_wd = findViewById(R.id.tv_wd);
        tv_sd = findViewById(R.id.tv_sd);
        conn = new MyHttpUrlConnection();
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    tv_wd.setText("温度："+jsonJx(wd)+"℃");
                    tv_sd.setText("湿度："+jsonJx(sd)+"Rh");
                    System.out.println(wd);
                    System.out.println(sd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        wd = conn.getData("z_temperature");
                        Thread.sleep(100);
                        sd = conn.getData("z_humidity");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }
    public String jsonJx(String json) throws JSONException {
        if(json!=null) {
            JSONObject js = new JSONObject(json);
            JSONObject ResultObj = js.getJSONObject("ResultObj");
            return ResultObj.getString("Value");
        }else{
            return null;
        }
    }
    public void open(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    conn.sendCmd("m_fan","1");
                    Thread.sleep(100);
                    conn.sendCmd("z_fan","1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void close(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    conn.sendCmd("m_fan","0");
                    Thread.sleep(100);
                    conn.sendCmd("z_fan","0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
