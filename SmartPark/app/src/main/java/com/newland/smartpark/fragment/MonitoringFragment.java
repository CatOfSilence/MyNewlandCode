package com.newland.smartpark.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.newland.smartpark.R;
import com.newland.smartpark.activity.ShowActivity;
import com.newland.smartpark.base.BaseFragment;
import com.newland.smartpark.golbal.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;


/**
 * 园区监控界面
 */

public class MonitoringFragment extends BaseFragment implements View.OnTouchListener {
    private View rootView;
    public static TextureView textureView;
    public static CameraManager cameraManager;
    //控制摄像头上下左右转动的ImageButton
    private ImageButton ibTop, ibBottom, ibLeft, ibRight;
    //显示有人无人的文本框
    private TextView tvPerson;
    //开启监控/结束监控ToggleButton
    private ToggleButton tbOpenOrClose;
    //红灯对应的CheckBox，true对应亮灯图片，false对应灭灯图片
    private CheckBox cbRed;
    //图片名
    private String currPicName;
    private String ip;
    private String userName;
    private String pwd;
    private String channel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != rootView) {
            //fragment只加载一次
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_monitoring, null);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textureView = getActivity().findViewById(R.id.showCamera);
        ibTop = getActivity().findViewById(R.id.ibTop);
        ibLeft = getActivity().findViewById(R.id.ibLeft);
        ibRight = getActivity().findViewById(R.id.ibRight);
        ibBottom = getActivity().findViewById(R.id.ibBottom);
        tvPerson = getActivity().findViewById(R.id.tv_person);
        tbOpenOrClose = getActivity().findViewById(R.id.tbOpenOrClose);
        cbRed = getActivity().findViewById(R.id.cb_red);
        ibTop.setOnTouchListener(this);
        ibLeft.setOnTouchListener(this);
        ibRight.setOnTouchListener(this);
        ibBottom.setOnTouchListener(this);
        // TODO: 2  添加“开启监控”ToggleButton的状态监听，如果是“开启监控”状态，打开摄像头，否则如果摄像头不为空，释放摄像头
        tbOpenOrClose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean open) {
                if (open) {
                    //打开摄像头
                    handler.post(openCameraRunnable);
                } else {
                    if (cameraManager != null) {
                        //释放摄像头
                        cameraManager.releaseCamera();
                    }
                }
            }
        });
        //执行Runnable，有人开红灯，如果有人并且监控是开启的，拍照
        handler.post(runnable);
    }

    //拍照（截图）
    public void capture() {
        final File f = new File(Environment.getExternalStorageDirectory().getPath(), "pic");
        if (!f.exists()) {
            f.mkdirs();
        }
        currPicName = getPicName();

        if (textureView != null) {
            new Thread() {
                public void run() {
                    Bitmap bitmap = textureView.getBitmap();
                    try {
                        if (bitmap.getByteCount() != 0) {
                            File file = new File(f.getPath() + "/" + currPicName);
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            fos.close();
                            ShowActivity.imageList.add(currPicName);
                        }
                    } catch (Exception var4) {
                        var4.printStackTrace();
                    }

                }
            }.start();
        }
    }

    //图片名保存为当前系统时间的年月日时分秒.png
    public String getPicName() {
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return sd.format(date) + ".png";
    }

    // TODO 5: 从其他页面回到园区监控页面的时候，如果监控按钮是开启状态，0.3s后打开摄像头
    @Override
    public void onResume() {
        super.onResume();
        if (tbOpenOrClose.isChecked()) {
            //如果开启监控，0.3s后打开摄像头
            handler.postDelayed(openCameraRunnable, 300);

        }
    }

    Handler handler = new Handler();
    //有人开红灯，如果有人并且监控是开启的，拍照
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String person = adam4150.getInfrared(Constants.DIPORTS[1]);
            if (person.equals("有人") && !sharePreferenceUtil.getIsRedOn()) {
                //开红灯
                adam4150.openRelay(Constants.RELAYPORTS[3]);
                sharePreferenceUtil.setIsRedOn(true);
                cbRed.setChecked(true);
            } else if (person.equals("无人") && sharePreferenceUtil.getIsRedOn()) {
                //关红灯
                adam4150.closeRelay(Constants.RELAYPORTS[3]);
                sharePreferenceUtil.setIsRedOn(false);
                cbRed.setChecked(false);
            }
            tvPerson.setText(person);
            // TODO 4: 如果有人，判断摄像头地址信息是否为空，为空时弹出提示，不为空时监控如果是开启的，拍照
            if (person.equals("有人")) {
                //获取Sharepreferences中保存的摄像头地址信息
                getCameraData();
                if (ip.isEmpty() || userName.isEmpty() || pwd.isEmpty() || channel.isEmpty()) {
                    Toast.makeText(getActivity(), "请点击右上角图标设置摄像头的地址信息", Toast.LENGTH_SHORT).show();
                } else {
                    if (tbOpenOrClose.isChecked()) {
                        //如果有人并且监控是开启的，拍照
                        capture();
                    }
                }
            }
            handler.postDelayed(runnable, 1000);
        }
    };
    // TODO: 1  添加名为openCameraRunnable的Runnable，判断摄像头地址信息是否为空，为空弹出提示并return，否则打开摄像头
    Runnable openCameraRunnable = new Runnable() {
        @Override
        public void run() {
            //获取Sharepreferences中保存的摄像头地址信息
            getCameraData();
            if (ip.isEmpty() || userName.isEmpty() || pwd.isEmpty() || channel.isEmpty()) {
                Toast.makeText(getActivity(), "请点击右上角图标设置摄像头的地址信息", Toast.LENGTH_SHORT).show();
                return;
            }
            cameraManager = CameraManager.getInstance();
            cameraManager.setupInfo(textureView, userName, pwd, ip, channel);
            cameraManager.openCamera();
        }
    };

    //获取Sharepreferences中保存的摄像头地址信息
    private void getCameraData() {
        ip = sharePreferenceUtil.getCamIP();
        userName = sharePreferenceUtil.getCamUserName();
        pwd = sharePreferenceUtil.getCamPWD();
        channel = sharePreferenceUtil.getCamChannel();
    }

    //控制摄像头上下左右转动的按钮的触摸事件
    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        // TODO: 3 在控制摄像头上下左右转动的按钮的触摸事件中判断：如果监控未开启，弹出开启监控的提示，返回false；如果监控已开启，移动按钮或手抬起时控制摄像头转动
        try {
            int action = arg1.getAction();
            PTZ ptz = null;
            if (!tbOpenOrClose.isChecked()) {
                if (action == MotionEvent.ACTION_UP) {//如果未开始监听，手抬起来的时候弹出提示
                    Toast.makeText(getActivity(), "请先开启监控", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                //移动按钮或手抬起时控制摄像头转动
                if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                    ptz = PTZ.Stop;
                } else if (action == MotionEvent.ACTION_DOWN) {
                    int viewId = arg0.getId();
                    switch (viewId) {
                        case R.id.ibTop:
                            ptz = PTZ.Up;
                            break;
                        case R.id.ibBottom:
                            ptz = PTZ.Down;
                            break;
                        case R.id.ibLeft:
                            ptz = PTZ.Left;
                            break;
                        case R.id.ibRight:
                            ptz = PTZ.Right;
                            break;
                    }
                }
            }
            cameraManager.controlDir(ptz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;//返回false还可以继续监听对应组件的点击事件（OnClickListener），返回true则不能。
    }


    @Override
    public void getData(Intent intent) {
        //接收广播发送过来的数据，如果为true，关闭界面上红灯
        boolean isClose = intent.getBooleanExtra("closeRed", false);
        if (isClose) {
            //关闭界面上红灯
            cbRed.setChecked(false);
        }

    }


}
