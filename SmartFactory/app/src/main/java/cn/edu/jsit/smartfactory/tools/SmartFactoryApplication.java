package cn.edu.jsit.smartfactory.tools;

import android.app.Application;

public class SmartFactoryApplication extends Application {

    private String serverAddress="";
    private String projectLabel="";
    private String cloudAccount="";
    private String cloudAccountPassword="";
    private String cameraAddress="";
    private String tempSensorId="";
    private float tempThresholdValue=0;
    private String humSensorId="";
    private float humThresholdValue=0;
    private String lightSensorId="";
    private float lightThresholdValue=0;
    private String bodySensorId="";
    private String lightControllerId="";
    private String ventilationControllerId="";
    private String airControllerId="";

    private boolean isLogin=false;
    public static String language="";

    @Override
    public void onCreate(){
        super.onCreate();
    }

    public void setServerAddress(String s){
        this.serverAddress = s;
    }

    public String getServerAddress(){
        return this.serverAddress;
    }

    public void setProjectLabel(String s){
        this.projectLabel = s;
    }

    public String getProjectLabel(){
        return this.projectLabel;
    }

    public void setCloudAccount(String s){
        this.cloudAccount = s;
    }

    public String getCloudAccount(){
        return this.cloudAccount;
    }

    public void setCloudAccountPassword(String s){
        this.cloudAccountPassword =s;
    }

    public String getCloudAccountPassword(){
        return this.cloudAccountPassword;
    }

    public void setCameraAddress(String s){
        this.cameraAddress = s;
    }

    public String getCameraAddress(){
        return this.cameraAddress;
    }

    public void setTempSensorId(String s){
        this.tempSensorId = s;
    }

    public String getTempSensorId(){
        return this.tempSensorId;
    }

    public void setTempThresholdValue(float s){
        this.tempThresholdValue = s;
    }

    public float getTempThresholdValue(){
        return this.tempThresholdValue;
    }

    public void setHumSensorId(String s){
        this.humSensorId = s;
    }

    public String getHumSensorId(){
        return this.humSensorId;
    }

    public void setHumThresholdValue(float s){
        this.humThresholdValue = s;
    }

    public float getHumThresholdValue(){
        return this.humThresholdValue;
    }

    public void setLightSensorId(String s){
        this.lightSensorId = s;
    }

    public String getLightSensorId(){
        return this.lightSensorId;
    }

    public void setLightThresholdValue(float s){
        this.lightThresholdValue = s;
    }

    public float getLightThresholdValue(){
        return this.humThresholdValue;
    }

    public void setBodySensorId(String s){
        this.bodySensorId = s;
    }

    public String getBodySensorId(){
        return this.bodySensorId;
    }

    public void setLightControllerId(String s){
        this.lightControllerId = s;
    }

    public String getLightControllerId(){
        return this.lightControllerId;
    }

    public void setVentilationControllerId(String s){
        this.ventilationControllerId = s;
    }

    public String getVentilationControllerId(){
        return this.ventilationControllerId;
    }

    public void setAirControllerId(String s){
        this.airControllerId = s;
    }

    public String getAirControllerId(){
        return this.airControllerId;
    }

    public void setIsLogin(boolean s){
        this.isLogin = s;
    }

    public boolean getIsLogin(){
        return this.isLogin;
    }


}
