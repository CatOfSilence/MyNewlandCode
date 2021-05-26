package com.newland.smartpark.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.newland.smartpark.base.Adam4150;
import com.newland.smartpark.golbal.Constants;
import com.newland.smartpark.utils.NotificationUtils;
import com.newland.smartpark.utils.SharePreferenceUtil;


/**
 * 后台服务
 */

public class MyService extends Service {
    private boolean isRun = true;
    //根据对应的警报，创建对应的提示框
    private Adam4150 adam4150;
    private Intent intent;
    private SharePreferenceUtil sharePreferenceUtil;
    private Boolean isRedOn, isOrangeOn;//标记红灯和橙灯的当前亮灭状态


    @Override
    public void onCreate() {
        super.onCreate();
        sharePreferenceUtil = SharePreferenceUtil.getInstant(this);
        intent = new Intent();
        intent.setAction("senddata");
        adam4150 = new Adam4150();
        adam4150.openPort(Constants.COMS[0], 9600);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRun) {
                    try {
                        isRedOn = sharePreferenceUtil.getIsRedOn();
                        isOrangeOn = sharePreferenceUtil.getIsOrangeOn();
                        //如果微动开关被触发
                        if (adam4150.getMicroSwitch(Constants.DIPORTS[2])) {
                            //如果红灯亮，灭红灯
                            if (isRedOn) {
                                adam4150.closeRelay(Constants.RELAYPORTS[3]);
                                sharePreferenceUtil.setIsRedOn(false);
                                //设置Intent传递的红灯参数对应的值为false，表明要关闭界面上的红灯
                                intent.putExtra("closeRed", true);
                            }
                            //如果橙色灯亮，灭橙色灯
                            if (isOrangeOn) {
                                adam4150.closeRelay(Constants.RELAYPORTS[5]);
                                sharePreferenceUtil.setIsOrangeOn(false);
                                //设置Intent传递的橙灯参数对应的值为false，表明要关闭界面上的橙色灯
                                intent.putExtra("closeOrange", true);
                            }
                            //亮绿灯
                            adam4150.openRelay(Constants.RELAYPORTS[4]);
                        }
                        //获取烟雾数据
                        String smoke = adam4150.getSmoke(Constants.DIPORTS[0]);
                        //如果有烟,发送通知
                        if (smoke == "有烟") {
                            //发送通知
                            NotificationUtils noti = new NotificationUtils(MyService.this, "烟雾警报");
                        }

                        //发送广播
                        sendBroadcast(intent);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 服务退出时，停止线程，关闭4150串口
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        isRun = false;
        adam4150.closePort();
    }


}
