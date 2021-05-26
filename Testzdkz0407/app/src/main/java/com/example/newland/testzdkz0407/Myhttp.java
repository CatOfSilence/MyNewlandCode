package com.example.newland.testzdkz0407;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Myhttp {
    static String Token = "&AccessToken=";
    public String login(String json) throws Exception {
        URL url = new URL(MyData.url);
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
        out.write(json.getBytes());
        out.flush();

        InputStream in = conn.getInputStream();
        int len=0;
        byte b[] = new byte[1024];
        String result = "";
        while ((len=in.read(b))!=-1){
            result = new String(b,0,len);
        }

        in.close();
        out.close();
        conn.disconnect();
        return result;
    }
    public String getData() throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId="+MyData.xmbs+"&apiTag="+MyData.wdbs+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        String result = "";
        while ((len=in.read(b))!=-1){
            result = new String(b,0,len);
            System.out.printf(result);
        }

        in.close();
        conn.disconnect();
        return result;
    }
}
