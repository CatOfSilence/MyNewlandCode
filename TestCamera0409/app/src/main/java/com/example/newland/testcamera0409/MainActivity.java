package com.example.newland.testcamera0409;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    TextureView tuv;
    CameraManager cameraManager;
    Button bt_open, bt_close, bt_cap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tuv = findViewById(R.id.tuv);

        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        bt_cap = findViewById(R.id.bt_cap);
        cameraManager = CameraManager.getInstance();
        findViewById(R.id.bt_up).setOnTouchListener(this);
        findViewById(R.id.bt_down).setOnTouchListener(this);
        findViewById(R.id.bt_left).setOnTouchListener(this);
        findViewById(R.id.bt_right).setOnTouchListener(this);
        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tuv.setVisibility(View.VISIBLE);
                cameraManager.setupInfo(tuv, "adamin", "admin", "172.21.7.14", "1");
                cameraManager.openCamera();
            }
        });
        bt_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraManager != null) {
                    cameraManager.releaseCamera();
                    tuv.setVisibility(View.INVISIBLE);
                }
            }
        });
        bt_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraManager.capture(Environment.getExternalStorageDirectory().getPath(), "??????.png");
            }
        });
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
                case R.id.bt_left:
                    ptz = PTZ.Left;
                    break;
                case R.id.bt_right:
                    ptz = PTZ.Right;
                    break;
                case R.id.bt_down:
                    ptz = PTZ.Down;
                    break;
            }
        }
        cameraManager.controlDir(ptz);
        return false;
    }
}
