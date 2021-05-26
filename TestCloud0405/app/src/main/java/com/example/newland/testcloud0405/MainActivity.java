package com.example.newland.testcloud0405;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    String login = "{'Account':'18316461896','Password':'asdffdsa','IsRememberMe':'true'}";
    String wd;
    MyHttpURLConnection mhu;
    TextView tv;
    Button bt_open;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        bt_open = findViewById(R.id.button);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    JSONObject js = new JSONObject(wd);
                    JSONObject ResultObj = js.getJSONObject("ResultObj");
                    tv.setText("微动开关2："+ResultObj.getString("Value"));
                    System.out.println("微动开关2："+ResultObj.getString("Value"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    mhu = new MyHttpURLConnection(login);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    try {
                        Thread.sleep(2000);
                        wd = mhu.getData("227398","m_microswitch");
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public void open(View view){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    mhu.sendCmd("227398","m_lamp","1");
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
                    mhu.sendCmd("227398","m_lamp","0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
