package com.newland.smartpark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.base.BaseFragment;
import com.newland.smartpark.ecloud.NewLandECloud;
import com.newland.smartpark.ecloud.DataCache;
import com.newland.view.SensorInstrumentView;

import java.io.IOException;
import java.util.Map;


/**
 * 园区环境采集界面
 */

public class CloudFragment extends BaseFragment {
    private View rootView;
    //井盖超声波
    private SensorInstrumentView well_ultrasonicLab;
    //井内甲烷
    private SensorInstrumentView methaneLab;
    //井盖三轴中z轴
    private TextView triaxialLab_z;
    //井盖三轴中x轴
    private TextView triaxialLab_x;
    //井盖三轴中y轴
    private TextView triaxialLab_y;
    //井盖状态
    private TextView cover_faultLab;
    //PH值
    private SensorInstrumentView m_phLab;
    //浑度
    private SensorInstrumentView m_turbiLab;
    //电导率
    private SensorInstrumentView m_conductivityLab;
    //PM2.5
    private SensorInstrumentView m_pm2_5Lab;
    //垃圾桶超声波
    private SensorInstrumentView trashcan_ultrasonicLab;

    //一氧化碳(NB-Iot设备)
    private SensorInstrumentView carbonMonoxideLab;
    //可燃气(NB-Iot设备)
    private SensorInstrumentView flammableGasLab;

    // 开启自动采集云平台数据按钮
    private Button getDataFromYun;
    // 云平台连接成功与否
    private boolean isConnsuccess = false;
    //是否停止采集的标志
    private boolean isStop;
    //登录云平台按钮
    private Button loginYun;
    //数据缓存类对象
    private DataCache dataCache;

//消息对象
Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 2) {
            loginYun.setText("云平台连接成功");

        } else if (msg.what == 3) {
            loginYun.setText("登录云平台");
        } else if (msg.what == 1) {
            Map<String, String> map = (Map) msg.obj;
            if (map != null) {

                //更新UI界面

                //更新井盖水位（超声波）
                double val1 = Double.valueOf(map.get("well_ultrasonic"));
                int tval1 = (int) (val1 / well_ultrasonicLab_max * 100);
                well_ultrasonicLab.setProgress(tval1);

                // 更新甲烷值
                double val2 = Double.valueOf(map.get("methane"));
                int tval2 = (int) (val2 / methaneLab_max * 100);
                methaneLab.setProgress(tval2);

                //更新井盖三轴值
                String triaxial = (map.get("triaxial"));
                if (triaxial != null && !triaxial.isEmpty()) {
                    String[] triaxial_xyz = triaxial.split("\\|");
                    if (triaxial_xyz.length == 3) {
                        triaxialLab_x.setText(triaxial_xyz[0]);//+ "mm"
                        triaxialLab_y.setText(triaxial_xyz[1]);
                        triaxialLab_z.setText(triaxial_xyz[2]);
                    }
                }

                //更新井盖状态
                boolean coverIsOk = Boolean.parseBoolean(map.get("cover_fault"));
                if (coverIsOk) {
                    cover_faultLab.setText("井盖正常");
                } else {
                    cover_faultLab.setText("井盖故障");
                }

                //更新PH数据
                double val3 = Double.valueOf(map.get("PH"));
                int tval3 = (int) (val3 / m_phLab_max * 100);
                m_phLab.setProgress(tval3);

                //更新浊度的数据
                double val4 = Double.valueOf(map.get("Turbi"));
                int tval4 = (int) (val4 / m_turbiLab_max * 100);
                m_turbiLab.setProgress(tval4);

                // 更新电导率的数据
                double val5 = Double.valueOf(map.get("Conductivity"));
                int tval5 = (int) (val5 / m_conductivityLab_max * 100);
                m_conductivityLab.setProgress(tval5);

                //更新PM2.5的数据
                double val6 = Double.valueOf(map.get("m_pm2_5"));
                int tval6 = (int) (val6 / m_pm2_5Lab_max * 100);
                m_pm2_5Lab.setProgress(tval6);

                //更新垃圾桶超声波监测数据
                double val7 = Double.valueOf(map.get("trashcan"));
                int tval7 = (int) (val7 / trashcan_ultrasonicLab_max * 100);
                trashcan_ultrasonicLab.setProgress(tval7);

                //更新NB-IoT设备监测数据(一氧化碳)
                double val8 = Double.valueOf(map.get("CarbonMonoxideStr"));
                int tval8 = (int) (val8 / carbonMonoxideLab_max * 100);
                carbonMonoxideLab.setProgress(tval8);

                // 更新NB-IoT设备监测数据(可燃气)
                double val9 = Double.valueOf(map.get("FlammableGas"));
                int tval9 = (int) (val9 / flammableGasLab_max * 100);
                flammableGasLab.setProgress(tval9);
            }
        }
    }
};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fragment只加载一次
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_cloud, null);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();//初始化控件
        dataCache = new DataCache(getContext());
        loginYunListener();//登录云平台的按钮事件
        getDataListener();//采集数据的按钮事件
    }

    //注意：设置各传感器的最大值，此处是模拟数据，读者应该按真实数据进行设置
    int well_ultrasonicLab_max = 100;
    int methaneLab_max = 100;
    int m_phLab_max = 14;
    int m_turbiLab_max = 10;
    int m_conductivityLab_max = 100;
    int m_pm2_5Lab_max = 250;
    int trashcan_ultrasonicLab_max = 100;
    int carbonMonoxideLab_max = 100;
    int flammableGasLab_max = 100;

private void loginYunListener()
{
    //登录云平台
    loginYun.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 以下完成云平台登录
            NewLandECloud nlecloud = new NewLandECloud(DataCache.getAddress(), DataCache.getPort());
            String tname = DataCache.getLoginname();
            String tpass = DataCache.getPassword();
            if (tname == null || tname.equals("0") || tpass == null || tpass.equals("0"))  //DataCache.getLoginname(), DataCache.getPassword()
            {
                Toast.makeText(getContext(), "请先设置相关参数", Toast.LENGTH_LONG).show();
                return;
            }
            //再一次点击时停止数据采集，显示登录云平台
            if(!(loginYun.getText().toString().trim().equals("登录云平台")))
            {
                loginYun.setText("登录云平台");
                isConnsuccess=false;
                isStop=true;
                getDataFromYun.setText("开始采集");
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isOk = false;
                    try {
                        // 登录云平台
                        isOk = nlecloud.login(DataCache.getLoginname(), DataCache.getPassword());
                        if (isOk) {
                            //发送登录成功的消息
                            Message msg = new Message();
                            msg.what = 2;
                            handler.sendMessage(msg);
                            //设置登录成功的标志为真
                            isConnsuccess = true;
                        } else {
                            //发送登录失败的消息
                            Message msg = new Message();
                            msg.what = 3;
                            handler.sendMessage(msg);
                            //设置登录成功的标志为假
                            isConnsuccess = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    });

}
private void getDataListener()
{
    //采集数据
    getDataFromYun.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isConnsuccess) {
                Toast.makeText(getContext(), "请先登录云平台", Toast.LENGTH_LONG).show();
                return;
            }
            if (getDataFromYun.getText().toString().trim().equals("开始采集")) {
                isStop = false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DataCache dataCache = new DataCache(getContext());
                        //获取设备id
                        NewLandECloud ecloud = new NewLandECloud(null, null);
                        String LoRaGateId = dataCache.getGatewayDeviceIdLoRa();
                        String carbonMonoxideIdNB = dataCache.getCarbonMonoxideDeviceIdNB();
                        String flammableGasIdNB = dataCache.getFlammableGasDeviceIdNB();

                        while (!isStop) {

                            try {
                                //获取多个设备中多个传感器的数据值
                                Map<String, String> map  = ecloud.getDevicesData(LoRaGateId, carbonMonoxideIdNB, flammableGasIdNB);
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = map;
                                //把采集到的数据通过消息机制发出去
                                handler.sendMessage(msg);
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                getDataFromYun.setText("结束采集");
            } else {
                //结束采集
                isStop = true;
                getDataFromYun.setText("开始采集");
            }
        }
    });
}

private void initView() {
    well_ultrasonicLab = rootView.findViewById(R.id.well_ultrasonicLab);
    well_ultrasonicLab.setMaxLange(well_ultrasonicLab_max);

    methaneLab = rootView.findViewById(R.id.methaneLab);
    methaneLab.setMaxLange(methaneLab_max);

    triaxialLab_z = rootView.findViewById(R.id.triaxialLab_z);
    triaxialLab_x = rootView.findViewById(R.id.triaxialLab_x);
    triaxialLab_y = rootView.findViewById(R.id.triaxialLab_y);
    cover_faultLab = rootView.findViewById(R.id.cover_faultLab);

    m_phLab = rootView.findViewById(R.id.m_phLab);
    m_phLab.setMaxLange(m_phLab_max);

    m_turbiLab = rootView.findViewById(R.id.m_turbiLab);
    m_turbiLab.setMaxLange(m_turbiLab_max);

    m_conductivityLab = rootView.findViewById(R.id.m_conductivityLab);
    m_conductivityLab.setMaxLange(m_conductivityLab_max);

    m_pm2_5Lab = rootView.findViewById(R.id.m_pm2_5Lab);
    m_pm2_5Lab.setMaxLange(m_pm2_5Lab_max);

    trashcan_ultrasonicLab = rootView.findViewById(R.id.trashcan_ultrasonicLab);
    trashcan_ultrasonicLab.setMaxLange(trashcan_ultrasonicLab_max);

    carbonMonoxideLab = rootView.findViewById(R.id.carbonMonoxideLab);
    carbonMonoxideLab.setMaxLange(carbonMonoxideLab_max);

    flammableGasLab = rootView.findViewById(R.id.flammableGasLab);
    flammableGasLab.setMaxLange(flammableGasLab_max);

    getDataFromYun = rootView.findViewById(R.id.getDataFromYun);
    loginYun = rootView.findViewById(R.id.loginYun);
}


    @Override
    public void getData(Intent intent) {

    }
}
//  well_ultrasonicLab.setText(map.get("well_ultrasonic"));