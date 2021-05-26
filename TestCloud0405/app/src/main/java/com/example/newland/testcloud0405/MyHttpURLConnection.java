package com.example.newland.testcloud0405;

import android.icu.util.Output;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyHttpURLConnection {

    static String Token = null;
    public MyHttpURLConnection(String json) throws Exception {
        if(Token == null) {
            URL url = new URL("http://api.nlecloud.com/Users/Login");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.connect();

            OutputStream out = conn.getOutputStream();
            out.write(json.getBytes());
            out.flush();

            InputStream in = conn.getInputStream();
            int len = 0;
            byte[] b = new byte[1024];
            String result = null;
            while ((len = in.read(b)) != -1) {
                result = new String(b, 0, len);
                System.out.println(result);
            }

            out.close();
            in.close();
            conn.disconnect();

            JSONObject js = new JSONObject(result);
            JSONObject ResultObj = js.getJSONObject("ResultObj");
            Token = "&AccessToken=" + ResultObj.getString("AccessToken");
        }
    }
    public String getData(String deviceId,String apiTag) throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId="+deviceId+"&apiTag="+apiTag+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream in = conn.getInputStream();
        int len = 0;
        String result = null;
        byte[] b = new byte[1024];
        while ((len = in.read(b))!=-1){
            result = new String(b,0,len);
        }

        in.close();
        conn.disconnect();

        return result;

    }

    public void sendCmd(String deviceId,String apiTag,String value) throws Exception {
        URL url = new URL("http://api.nlecloud.com/Cmds?deviceId="+deviceId+"&apiTag="+apiTag+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Connection","keep-alive");
        conn.connect();

        OutputStream out = conn.getOutputStream();
        out.write(value.getBytes());
        out.flush();

        InputStream in = conn.getInputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while ((len = in.read(b))!=-1){
            System.out.println(new String(b,0,len));
        }
    }
}
