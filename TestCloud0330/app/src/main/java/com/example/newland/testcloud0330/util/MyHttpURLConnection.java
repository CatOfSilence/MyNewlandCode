package com.example.newland.testcloud0330.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;


public class MyHttpURLConnection {
    static String Token = "FAF0AE89D992EE56F1573AE27DBC8DA44123784913D2FFFCBD10ADCDC780D65A3EB9A7BD5AC5FDF1F4BB6BBB8D2A9DED2DFAB3F24608C0048E4F17D47D29A4858AE4F1A3EF2D49331602425B54558AC720B15D748AD39273A66B9E77BA9390D67E9D16E3E1D747EFB561EBB1670E6DE8DAB753E3852FEBE6E0FBEE3F2E32BC9FE2C95CAD274E8C7834EB783535C032F8E29FA270B7EDE759066550481A16FE2924999E91AB2C06622F85AA12ED5160674B5720A2FBDB974B30F49AEA4CDE3379686D2553051ED1A5CAFCBC6FD277C8D4E12018EF8BB28918F809665D01966F95";

    public static void LoginByPost() throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId=227398&apiTag=m_microswitch" + Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(1000);
        InputStream in = conn.getInputStream();
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = in.read(data)) != -1) {
            System.out.println(new String(data, 0, len));
        }
        in.close();
    }

    public String sendPost(String url, String Params) throws IOException {

        OutputStream out = null;
        String response = "";

        try {
            URL httpUrl = new URL(url); //HTTP URL类 用这个类来创建连接
            //创建URL

            //建立连接
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            //POST请求
            out = conn.getOutputStream();
            out.write(Params.getBytes());
            out.flush();

            //读取响应
            byte[] b = new byte[1024];
            InputStream in = conn.getInputStream();
            int lines;

            while ((lines = in.read(b)) != -1) {
                System.out.println(new String(b,0,lines));
            }
            in.close();

            // 断开连接
            conn.disconnect();
            out.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        return response;
    }
}
