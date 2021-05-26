package com.example.newland.testdmjk0404;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    TextureView tuv;
    Button bt_open,bt_close;
    TextView tv_rt;
    CameraManager cameraManager = null;
    Modbus4150 modbus4150;
    ZigBee zigBee;
    Handler handler;
    Thread thread;
    boolean flag,rt;
    DecimalFormat df = new DecimalFormat("0");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tuv = findViewById(R.id.tuv);
        tv_rt = findViewById(R.id.tv_rt);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        findViewById(R.id.bt_up).setOnTouchListener(this);
        findViewById(R.id.bt_down).setOnTouchListener(this);
        findViewById(R.id.bt_left).setOnTouchListener(this);
        findViewById(R.id.bt_right).setOnTouchListener(this);
//        modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("172.16.6.15",2001),null);
        zigBee = new ZigBee(DataBusFactory.newSocketDataBus("192.168.6.16",2002),null);
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCamera();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
//                    modbus4150.getDIVal(6, new MdBus4150SensorListener() {
//                        @Override
//                        public void onVal(int i) {
//                            if(i==0){
//                                tv_rt.setText("人体红外：有人");
//                                flag = true;
//                            }else{
//                                tv_rt.setText("人体红外：无人");
//                                flag = false;
//                            }
//                        }
//
//                        @Override
//                        public void onFail(Exception e) {
//
//                        }
//                    });
                    tv_rt.setText(""+df.format(zigBee.getTmpHum()[0]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(flag){
                    openCamera();
                    rt = true;
                }


            }
        };
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        handler.sendEmptyMessage(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();


        Handler handler1 = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                try {
                    cameraManager.capture(Environment.getExternalStorageDirectory().getPath(),"发现目标.png");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    if(rt){
                        rt = false;
                        try {
                            Thread.sleep(5000);
                            handler1.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
    public void cameraInit(){
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(tuv,"admin","admin","192.168.6.14","1");
    }

    public void openCamera(){
        if(cameraManager == null){
            tuv.setVisibility(View.VISIBLE);
            cameraInit();
            cameraManager.openCamera();
        }
    }
    public void closeCamera(){
        if(cameraManager!=null){
            cameraManager.releaseCamera();
            cameraManager = null;
            tuv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(cameraManager!=null){
            cameraManager.releaseCamera();
        }
        if(modbus4150!=null)
            modbus4150.stopConnect();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        PTZ ptz = null;
        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            ptz = PTZ.Stop;
        }else if(action == MotionEvent.ACTION_DOWN){
            switch (view.getId()){
                case R.id.bt_up:
                    ptz = PTZ.Up;
                    break;
                case R.id.bt_down:
                    ptz = PTZ.Down;
                    break;
                case R.id.bt_left:
                    ptz = PTZ.Left;
                    break;
                case R.id.bt_right:
                    ptz = PTZ.Right;
                    break;
            }
        }
        try {
            cameraManager.controlDir(ptz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
