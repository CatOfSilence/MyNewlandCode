package com.newland.smartpark.fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.newland.smartpark.R;
import com.newland.smartpark.base.Adam4150;
import com.newland.smartpark.base.BaseFragment;
import com.newland.smartpark.base.ZigbeeOperator;
import com.newland.smartpark.golbal.Constants;


/**
 * 室内环境采集界面
 */

public class EnvironmentFragment extends BaseFragment {
    private View rootView;
    private TextView tvTemp,tvHumi,tvLight,tvSmoke;
    private ImageView imgTempFan,imgHumiFan;
    private AnimationDrawable tempAnim,humiAnim;
    //cbLamp、cbSmoke分别为照明灯、红灯对应的CheckBox，true对应亮灯图片，false对应灭灯图片
    private CheckBox cbLamp,cbSmoke;
    private Handler handler=new Handler();
    private ZigbeeOperator zigbeeOperator;
    private double tempData,humiData,lightData;
    private String smokeData;


    //标记风扇和灯的开关状态，默认为false；
    private boolean istempFanOpen,isHumiFanOpen,isLampOpen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fragment只加载一次
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView =  inflater.inflate(R.layout.fragment_environment, null);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }


    private void initView() {
        tvTemp=getActivity().findViewById(R.id.tv_temp);
        tvHumi=getActivity().findViewById(R.id.tv_humi);
        tvLight=getActivity().findViewById(R.id.tv_light);
        tvSmoke=getActivity().findViewById(R.id.tv_smoke);
        imgTempFan=getActivity().findViewById(R.id.imgTempFan);
        imgHumiFan=getActivity().findViewById(R.id.imgHumiFan);
        tempAnim= (AnimationDrawable) imgTempFan.getBackground();
        humiAnim= (AnimationDrawable) imgHumiFan.getBackground();
        cbLamp=getActivity().findViewById(R.id.cb_lamp);
        cbSmoke=getActivity().findViewById(R.id.cb_smoke);
        //实例化Zigbee对象
        zigbeeOperator=new ZigbeeOperator();
        //打开串口
        zigbeeOperator.openPort(Constants.COMS[1],38400);
        handler.post(runnable);
    }
   private Runnable runnable=new Runnable() {
        @Override
        public void run() {
            tempData=zigbeeOperator.getTemp();
            humiData=zigbeeOperator.getHumi();
            lightData=zigbeeOperator.getLight();
            smokeData=adam4150.getSmoke(Constants.DIPORTS[0]);
            tvTemp.setText(tempData+"℃");
            tvHumi.setText(humiData+"%RH");
            tvLight.setText(lightData+"Lx");
            tvSmoke.setText(smokeData);
            if(smokeData.equals("有烟")&&!sharePreferenceUtil.getIsOrangeOn()){
                //亮三色灯橙灯
                adam4150.openRelay(Constants.RELAYPORTS[5]);
                sharePreferenceUtil.setIsOrangeOn(true);
                //切换图片
                cbSmoke.setChecked(true);

            }else if(smokeData.equals("无烟")&&sharePreferenceUtil.getIsOrangeOn()){
                //灭三色灯橙灯
                adam4150.closeRelay(Constants.RELAYPORTS[5]);
                sharePreferenceUtil.setIsOrangeOn(false);
                //切换图片
                cbSmoke.setChecked(false);
            }
            //开关通风扇
            if(tempData>=sharePreferenceUtil.getTemp()&&!istempFanOpen){
                tempAnim.start();
                adam4150.openRelay(Constants.RELAYPORTS[0]);
                istempFanOpen=true;
            }else if(tempData<sharePreferenceUtil.getTemp()&&istempFanOpen){
                tempAnim.stop();
                adam4150.closeRelay(Constants.RELAYPORTS[0]);
                istempFanOpen=false;
            }
            //开关排气扇
            if(humiData>=sharePreferenceUtil.getHumi()&&!isHumiFanOpen){
                humiAnim.start();
                adam4150.openRelay(Constants.RELAYPORTS[1]);
                isHumiFanOpen=true;
            }else if(humiData<sharePreferenceUtil.getHumi()&&isHumiFanOpen){
                humiAnim.stop();
                adam4150.closeRelay(Constants.RELAYPORTS[1]);
                isHumiFanOpen=false;

            }
            //开关照明灯
            if(lightData<=sharePreferenceUtil.getLight()&&!isLampOpen){
                cbLamp.setChecked(true);
                adam4150.openRelay(Constants.RELAYPORTS[2]);
                isLampOpen=true;

            }else if(lightData>sharePreferenceUtil.getLight()&&isLampOpen){
                cbLamp.setChecked(false);
                adam4150.closeRelay(Constants.RELAYPORTS[2]);
                isLampOpen=false;
            }

            handler.postDelayed(runnable,1000);
        }
    };

    @Override
    public void getData(Intent intent) {
        //接收广播发送过来的数据，如果为true，关闭界面上橙色灯
        boolean isClose = intent.getBooleanExtra("closeOrange",false);
        if(isClose){
            //关闭界面上橙色灯
            cbSmoke.setChecked(false);

        }
    }

}
