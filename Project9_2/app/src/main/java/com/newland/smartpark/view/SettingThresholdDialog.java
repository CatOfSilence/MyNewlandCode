package com.newland.smartpark.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.newland.smartpark.R;
import com.newland.smartpark.utils.SharePreferenceUtil;


/**
 * 设置阈值对话框
 */

public class SettingThresholdDialog extends Dialog {

    private TextView tvTempValue,tvHumiValue,tvLightValue;
    private Button btnCancel;
    private Button btnConfirm;
    private SeekBar sbTemp,sbHumi,sbLight;
    private OnClickListener cancelListener;
    private OnClickListener confirmListener;
    private int tempProgress = 30;        //默认值
    private int humiProgress = 30;        //默认值
    private int lightProgress = 3000;        //默认值


    public SettingThresholdDialog(@NonNull Context context) {
        super(context,R.style.Dialog);
        //关联布局文件
        this.setContentView(R.layout.dialog_setting_threshold);
        //初始化组件
        initView();
        addListener();
    }

    private void initView() {
        sbTemp =  findViewById(R.id.sb_temp);
        sbHumi =  findViewById(R.id.sb_humi);
        sbLight =  findViewById(R.id.sb_light);
        tvTempValue = findViewById(R.id.tv_tempValue);
        tvHumiValue = findViewById(R.id.tv_humiValue);
        tvLightValue = findViewById(R.id.tv_lightValue);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm =  findViewById(R.id.btn_confirm);
    }

    private void addListener() {
        //温度SeekBar状态改变监听
        sbTemp.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //SeekBar进度显示到TextView上
                tvTempValue.setText(String.valueOf(progress));
                tempProgress=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //湿度SeekBar状态改变监听
        sbHumi.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //SeekBar进度显示到TextView上
                tvHumiValue.setText(String.valueOf(progress));
                humiProgress=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //光照SeekBar状态改变监听
        sbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //SeekBar进度显示到TextView上
                tvLightValue.setText(String.valueOf(progress));
                lightProgress=progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //设置确定点击事件
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null)
                    confirmListener.onClick(tempProgress,humiProgress,lightProgress);
                //对话框消失
                SettingThresholdDialog.this.dismiss();
            }
        });
        //设置取消点击事件
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对话框消失
                SettingThresholdDialog.this.dismiss();
            }
        });
    }
    //设置进度条默认值
    public void setDefProgress(int tempProgress,int humiProgress,int lightProgress){
        sbTemp.setProgress(tempProgress);
        sbHumi.setProgress(humiProgress);
        sbLight.setProgress(lightProgress);
    }


    //设置确定监听
    public void setConfirmListener(SettingThresholdDialog.OnClickListener listener){
        this.confirmListener=listener;
    }

    //回调接口
    public interface OnClickListener {
        //点击后返回，进度条的进度
        public void onClick(int tempProgress,int humiProgress,int lightProgress);
    }

}
