package com.example.newland.testypt0406;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyUrl {
    static String token = "&AccessToken=F3086A917F5E062A23A881740A81399C48E50B0BC2B23BACFE314CCABD5605E71FED9D0A9FF11234BF0F5F406BC5B226EE3A86CE37E4E34D0AAD53C4BA45FDAC3687CE55D5EF4F9BA95DEDBFEBF349D0B6AC5D1958EDD081F0000D9567BA9596B8F5363C6B47A941D2824FBD148402EBDB2AAC1F69B2273DFC9B9886E0FF1EE8B39FA9382B142E7109DED34E2894D9C0FD35591A03A8F2C59160A1B9E4684B3AAFCA38EE7A304421B151423D4393BCFE2F4BFCA0BD7EB3A48C872B4B58A4443D2BA34244874034472D1A83D05B330EE9DAF11868D15FF34C191BB99418F16702";
    public void sendCmd(String apiTag,String value) throws Exception {
        URL url = new URL("http://api.nlecloud.com/Cmds?deviceId=228187&apiTag="+apiTag+token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Connection","keep-alive");
        conn.connect();

        OutputStream out = conn.getOutputStream();
        out.write(value.getBytes());
        out.flush();

        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        while ((len = in.read(b))!= -1){
//            System.out.println(new String(b,0,len));
        }
        in.close();
        out.close();
        conn.disconnect();
    }
    public String getData(String apiTag) throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId=228187&apiTag="+apiTag+token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        String str = null;
        while ((len = in.read(b))!= -1){
            str = new String(b,0,len);
//            System.out.println(str);
        }
        in.close();
        conn.disconnect();
        return str;
    }
}
