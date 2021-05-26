package com.example.newland.testzdkz0417;

import android.content.Context;
import android.net.Uri;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import retrofit2.http.Url;

public class Myhttp {
    private static String Token = "&AccessToken=79FB675A6F4AA5A71E34725866867700D7D2B4B8AF29190F1D8CCD8C018DFEA87F811157DAB74F3976C9A9C38FBD7A2823BE8FE24F2A5B40CEF2B1AA3CC4135A4C2CF214CD6015891233EB9F797AEBF445A545A689275732564D93534FD7F37799703478168E6440EC47E5F3EB658BF61FDDAF006CC67803DDC7E996DB7AFED225D17DB8D0E20F8D55D1FC24542554ED499CFEF91DA71A71FC953CB507C4DD3E7800DF7A084C78EAFB46FBD7E8454192945DD2C389CB3525E51ABCFA89A814BA98B4D313F77E87B61EFFD43D09B1DF02BD23AC5A928499400C67D3157621E4E5";
    private static String AccessToken;
    NetWorkBusiness netWorkBusiness;
    public String getData(String apiTag) throws Exception {
        URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId=235765&apiTag="+apiTag+Token);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream in = conn.getInputStream();
        int len = 0;
        byte b[] = new byte[1024];
        String result = null;
        while ((len=in.read(b))!=-1){
            result = new String(b,0,len);
        }
        conn.disconnect();
        return result;
    }
    public void test(Context context){
        netWorkBusiness = new NetWorkBusiness("","http://api.nlecloud.com");
        netWorkBusiness.signIn(new SignIn("18316461896", "asdffdsa"), new NCallBack<BaseResponseEntity<User>>(context) {
            @Override
            protected void onResponse(BaseResponseEntity<User> response) {
                AccessToken = response.getResultObj().getAccessToken();
                System.out.println(AccessToken);
            }
        });

        netWorkBusiness = new NetWorkBusiness(AccessToken,"http://api.nlecloud.com");
        netWorkBusiness.getSensor("235765", "z_fan", new NCallBack<BaseResponseEntity<SensorInfo>>(context) {
            @Override
            protected void onResponse(BaseResponseEntity<SensorInfo> response) {
                if (response.getStatus()==0) {
                    System.out.println(response.getResultObj().getCtrlUrl());
                }
            }
        });
        netWorkBusiness.control("235765", "m_fan", "1", new NCallBack<BaseResponseEntity>(context) {
            @Override
            protected void onResponse(BaseResponseEntity response) {
                if (response.getStatus()==0) {
                    System.out.println(response.getResultObj());
                }
            }
        });
    }
}
