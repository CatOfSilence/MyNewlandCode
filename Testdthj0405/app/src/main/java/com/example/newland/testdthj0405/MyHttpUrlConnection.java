package com.example.newland.testdthj0405;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyHttpUrlConnection {
    static String Token = "&AccessToken=2CE88FA558FE52B6E9F39D5960D364D64278B51A61E77953BCCD6D56D9C1C6C2D632C4F887BE92F4FAA7E2D532691D87F62CEB2A8DD10B6D670AE523FDC724364C293665856CC0C786AE1F3BD16AC1F8C9378F357AEE819D2CA5D6CDCC58ED74BCBFC4CE404675BCAF045B24CA8B4FAF47CCDC4B1A719B41C74D9F0C89B4B709B61B6D54BB5AAAE6A0A6E29B1343C6AA207525BAC9B2994474121A3E1932383DFF60C2B3AAD210F736F89FAC3E1B891F4F0E532E6FFF12681B8C5311CAC6A28E10EAD8BB1F5B7E79CC6FF024797CC3920575BEBAE70D976DC8E05F257F94B232";

    public void sendCmd(String apiTag,String value) throws Exception {

        URL url = new URL("http://api.nlecloud.com/Cmds?deviceId=227514&apiTag="+apiTag+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setInstanceFollowRedirects(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Connection","keep-alive");
        conn.connect();

        OutputStream out = conn.getOutputStream();
        out.write(value.getBytes());
        out.flush();

        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        while ((len = in.read(b))!=-1){
            System.out.println(new String(b,0,len));
        }

        out.close();
        in.close();
        conn.disconnect();

    }
    public String getData(String apiTag) throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId=227514&apiTag="+apiTag+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        String result = null;
        while ((len = in.read(b))!=-1){
            result = new String(b,0,len);
        }

        in.close();
        conn.disconnect();
        return result;
    }
}
