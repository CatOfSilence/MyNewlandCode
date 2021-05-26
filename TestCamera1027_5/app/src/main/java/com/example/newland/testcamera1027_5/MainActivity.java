package com.example.newland.testcamera1027_5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.os.Environment;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{
    Button bt_open,bt_close,bt_capture;
    Button bt_up,bt_left,bt_down,bt_right;
    CameraManager cameraManager;
    TextureView textureView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        bt_capture = findViewById(R.id.bt_capture);
        bt_up = findViewById(R.id.bt_up);
        bt_left = findViewById(R.id.bt_left);
        bt_down = findViewById(R.id.bt_down);
        bt_right = findViewById(R.id.bt_right);
        textureView = findViewById(R.id.ture);
        init();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.bt_open:
                        cameraManager.openCamera();
                        textureView.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bt_close:
                        if(cameraManager!=null){
                            cameraManager.releaseCamera();
                            textureView.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case R.id.bt_capture:
                        cameraManager.capture(Environment.getExternalStorageDirectory().getPath(),"123.png");
                        break;
                }
            }
        };
        bt_open.setOnClickListener(onClickListener);
        bt_close.setOnClickListener(onClickListener);
        bt_capture.setOnClickListener(onClickListener);
        bt_up.setOnTouchListener(this);
        bt_down.setOnTouchListener(this);
        bt_left.setOnTouchListener(this);
        bt_right.setOnTouchListener(this);
    }
    void init(){
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(textureView,"admin","admin","192.168.27.4","1");
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