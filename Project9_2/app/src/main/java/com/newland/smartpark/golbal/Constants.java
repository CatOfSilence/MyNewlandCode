package com.newland.smartpark.golbal;

/**
 * 常量类
 */

public class Constants {



    //室内环境采集页面
    public static final int FRAGMENT_ENVIRONMENT = 0;

    //园区监控页面
    public static final int FRAGMENT_ENTRANCE = 1;

    //园区环境采集（云平台实现）
    public static final int FRAGMENT_CLOUD = 2;

    //分别对应通风扇、排气扇、灯泡、三色灯红灯、三色灯绿灯、三色灯橙色灯接的DO口
    public static final int[] RELAYPORTS = {0, 1, 2, 3, 4, 5};
    //分别对应烟雾传感器、红外对射接,微动开关的DI口
    public static final int[] DIPORTS = {4, 2,5};
    //分别4150模块、协调器接到平板上的串口
    public static final int[] COMS = {1, 2};

}
