package com.example.testcamera0922;

import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    TextureView tv;
    CameraManager cameraManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textureview);
        findViewById(R.id.bt_up).setOnTouchListener(this);
        findViewById(R.id.bt_down).setOnTouchListener(this);
        findViewById(R.id.bt_left).setOnTouchListener(this);
        findViewById(R.id.bt_right).setOnTouchListener(this);
    }
    public void init(){
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(tv,"admin","admin","172.19.15.14","1");
    }
    public void openCamera(View view){
        init();
        cameraManager.openCamera();
    }
    public void closeCamera(View view){
        if(cameraManager!=null){
            cameraManager.releaseCamera();
        }
    }
    public void capture(View view){
        cameraManager.capture(Environment.getExternalStorageDirectory().getPath(),"img.png");
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        PTZ ptz = null;
        if(action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_UP){
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
        cameraManager.controlDir(ptz);
        return false;
    }
}