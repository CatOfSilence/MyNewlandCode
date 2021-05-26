package com.newland.smartpark.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class SharePreferenceUtil {
    private SharedPreferences.Editor editor;
    private static SharePreferenceUtil instant;
    private SharedPreferences sp;
    public static final String LOCAL_FILE_NAME = "SmartPark";

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public synchronized static SharePreferenceUtil getInstant(Context context) {
        if (instant == null)
            instant = new SharePreferenceUtil(context, LOCAL_FILE_NAME);
        return instant;
    }

    public void addKey(String key, String Value) {
        editor.putString(key, Value);
        editor.commit();
    }

    public String getKey(String key) {
        return sp.getString(key, "");
    }

    //清空
    public void clear() {
        editor.clear();
        editor.commit();
    }

    public void setIsKeepPWD(Boolean bool) {
        editor.putBoolean("IsKeepPWD", bool);
        editor.commit();
    }

    public boolean getIsKeepPWD() {

        return sp.getBoolean("IsKeepPWD", false);
    }

    public void setIsIntoGuide(Boolean isIntoGuide) {
        editor.putBoolean("IsIntoGuide", isIntoGuide);
        editor.commit();
    }

    public Boolean getIsIntoGuide() {
        return sp.getBoolean("IsIntoGuide", false);
    }


    public void setPWD(String pwd) {
        editor.putString("pwd", pwd);
        editor.commit();
    }

    public String getPWD() {

        return sp.getString("pwd", "");
    }

    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }

    public String getUserName() {

        return sp.getString("userName", "");
    }

    public void setTemp(int id) {
        editor.putInt("temp", id);
        editor.commit();
    }

    public int getTemp() {
        return sp.getInt("temp", 30);
    }

    public void setCamUserName(String camUserName) {
        editor.putString("camUserName", camUserName);
        editor.commit();
    }

    public String getCamUserName() {
        return sp.getString("camUserName", "");
    }

    public void setCamPWD(String camPWD) {
        editor.putString("camPWD", camPWD);
        editor.commit();
    }

    public String getCamPWD() {
        return sp.getString("camPWD", "");
    }


    public void setCamIP(String camip) {
        editor.putString("camip", camip);
        editor.commit();
    }

    public String getCamIP() {
        return sp.getString("camip", "");
    }
    public void setCamChannel(String channel) {
        editor.putString("channel", channel);
        editor.commit();
    }

    public String getCamChannel() {
        return sp.getString("channel", "");
    }

    public void setHumi(int id) {
        editor.putInt("humi", id);
        editor.commit();
    }

    public int getHumi() {
        return sp.getInt("humi", 30);
    }

    public void setLight(int id) {
        editor.putInt("light", id);
        editor.commit();
    }

    public int getLight() {
        return sp.getInt("light", 3000);
    }


    public boolean getIsRedOn() {
        return sp.getBoolean("IsRedOn", false);
    }
    public void setIsRedOn(boolean IsRedOn) {
        editor.putBoolean("IsRedOn", IsRedOn);
        editor.commit();
    }
    public boolean getIsOrangeOn() {
        return sp.getBoolean("IsOrangeOn", false);
    }
    public void setIsOrangeOn(boolean IsOrangeOn) {
        editor.putBoolean("IsOrangeOn", IsOrangeOn);
        editor.commit();
    }






    //云平台NB 甲烷
    public void setNB2IDYun(String id) {
        editor.putString("flammableGasDeviceIdNB",id);
        editor.commit();
    }
    public String getNB2IDYun() {
        return sp.getString("flammableGasDeviceIdNB","39520");
    }

    //云平台NB 一氧化碳
    public void setNB1IDYun(String id) {
        editor.putString("carbonMonoxideDeviceIdNB",id);
        editor.commit();
    }
    public String getNB1IDYun() {
        return sp.getString("carbonMonoxideDeviceIdNB","39519");
    }

    //云平台LoRaid
    public void setLoRaIDYun(String id) {
        editor.putString("gatewayDeviceIdLoRa",id);
        editor.commit();
    }
    public String getLoRaIDYun() {
        return sp.getString("gatewayDeviceIdLoRa","39518");
    }

    //云平台端口
    public void setPortYun(String port) {
        editor.putString("port",port);
        editor.commit();
    }
    public String getPortYun() {
        return sp.getString("port","80");
    }

    //云平台域名
    public void setAddressYun(String address) {
        editor.putString("address",address);
        editor.commit();
    }
    public String getAddressYun() {
        return sp.getString("address","api.nlecloud.com");
    }

    //云平台密码
    public void setPasswordYun(String pass) {
        editor.putString("password", pass);
        editor.commit();
    }
    public String getPasswordYun() {
        return sp.getString("password","");
    }

    //云平台帐号
    public void setUsernameYun(String name) {
        editor.putString("loginname", name);
        editor.commit();
    }
    public String getUsernameYun() {
        return sp.getString("loginname","");
    }


}
