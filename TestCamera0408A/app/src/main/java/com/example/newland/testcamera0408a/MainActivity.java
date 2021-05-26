package com.example.newland.testcamera0408a;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    TextureView tuv;
    CameraManager cameraManager;
    Button bt_open,bt_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tuv = findViewById(R.id.tuv);
        bt_close = findViewById(R.id.bt2);
        bt_open = findViewById(R.id.bt1);
        findViewById(R.id.bt3).setOnTouchListener(this);
        findViewById(R.id.bt4).setOnTouchListener(this);
        findViewById(R.id.bt5).setOnTouchListener(this);
        findViewById(R.id.bt6).setOnTouchListener(this);
        Init();
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraManager.openCamera();
                tuv.setVisibility(View.VISIBLE);
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cameraManager!=null) {
                    cameraManager.releaseCamera();
                    tuv.setVisibility(View.INVISIBLE);
                }

            }
        });

    }
    public void Init(){
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(tuv,"admin","admin","172.21.7.14","1");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        PTZ ptz = null;
        if(action == MotionEvent.ACTION_CANCEL||action == MotionEvent.ACTION_UP){
            ptz = PTZ.Stop;
        }else if(action == MotionEvent.ACTION_DOWN){
            switch (view.getId()){
                case R.id.bt3:
                    ptz = PTZ.Up;
                    break;
                case R.id.bt4:
                    ptz = PTZ.Left;
                    break;
                case R.id.bt5:
                    ptz = PTZ.Right;
                    break;
                case R.id.bt6:
                    ptz = PTZ.Down;
                    break;
            }
        }
        cameraManager.controlDir(ptz);
        return false;
    }
}
