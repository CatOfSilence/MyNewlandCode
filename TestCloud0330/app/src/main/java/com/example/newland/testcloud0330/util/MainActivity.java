package com.example.newland.testcloud0330.util;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.newland.testcloud0330.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
//                        try {
//                            getData("227398");
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                                sendCmd(225515,"z_fan");
//                                String str = OkHttpHelper.sendCmd();
//                                System.out.println(str);
//                        try {
////                            MyHttpURLConnection.LoginByPost("18316461896","asdffdsa");
//                            new MyHttpURLConnection().LoginByPost();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        try {
                            String json =
                                    "{'Account':'" + "18316461896" + "','password':'" + "asdffdsa" + "','IsRememberMe':'true'}";
                            new MyHttpURLConnection().sendPost("http://api.nlecloud.com/Users/Login",json);
//                            new MyHttpURLConnection().sendPost("http://api.nlecloud.com/Cmds?deviceId=227398&apiTag=m_lamp&AccessToken="+MyHttpURLConnection.Token,"0");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        Init();

    }
    public void Init(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpHelper.accessToken = login("18316461896", "asdffdsa");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
    public String login(String loginName, String password) throws IOException, JSONException {
        String json =
                "{'Account':'" + loginName + "','password':'" + password + "','IsRememberMe':'true'}";
        String url = "http://api.nlecloud.com/Users/Login";
        String content = OkHttpHelper.post(url, json);
//        System.out.println(content);
        JSONObject jsonObj = new JSONObject(content);
        JSONObject innerJson = jsonObj.getJSONObject("ResultObj");
        System.out.println(innerJson.toString());
        String accessToken = innerJson.getString("AccessToken");
//        System.out.println("AccessToken =" +accessToken);

        String status = jsonObj.getString("Status");
//        System.out.println("status ="+status);
        return accessToken;
    }

    public String getData(String deviceId) throws IOException, JSONException {
        System.out.println(OkHttpHelper.accessToken);
        String json =
                "{'deviceId':'"+ deviceId +"'}";
        String url = "http://api.nlecloud.com/Devices/"+deviceId;
        String content = OkHttpHelper.get(url);
        System.out.println(content);
        JSONObject jsonObject = new JSONObject(content);
        JSONObject ResultObj = jsonObject.getJSONObject("ResultObj");
        JSONArray Sensors = ResultObj.getJSONArray("Sensors");
        JSONObject t0 = Sensors.getJSONObject(0);
        String name = t0.getString("Value");
        System.out.println("温度："+name);
        return null;
    }
    public void sendCmd(int deviceId,String apiTag) throws IOException {
        String json =
                "{'deviceId':'"+deviceId+"','apiTag':'"+apiTag+"',1}";
        String url = "http://api.nlecloud.com/Cmds?deviceId=225515&apiTag=z_fan";
        String content = OkHttpHelper.get(url);
        System.out.println(content);
    }
}
