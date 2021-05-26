package com.example.newland.testcamera0328;

import android.media.Image;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    CameraManager cameraManager;
    Button bt1, bt2,bt_cap;
    TextureView tuv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        tuv = findViewById(R.id.tuv);
        iv = findViewById(R.id.iv);
        bt_cap = findViewById(R.id.btcap);
        cameraManager = CameraManager.getInstance();
        initCamera();
        findViewById(R.id.btup).setOnTouchListener(this);
        findViewById(R.id.btleft).setOnTouchListener(this);
        findViewById(R.id.btright).setOnTouchListener(this);
        findViewById(R.id.btdown).setOnTouchListener(this);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraManager != null)
                    cameraManager.releaseCamera();
                tuv.setVisibility(View.VISIBLE);
                iv.setVisibility(View.INVISIBLE);
                openCamera();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tuv.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.VISIBLE);
                if (cameraManager != null)
                    cameraManager.releaseCamera();
            }
        });
        bt_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraManager.capture(Environment.getExternalStorageDirectory().getPath(),"AAA.png");
            }
        });

    }

    public void initCamera() {
        cameraManager.setupInfo(tuv, "admin", "admin", "172.19.28.14", "1");
    }

    public void openCamera() {
        initCamera();
        cameraManager.openCamera();
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        PTZ ptz = null;
        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            ptz = PTZ.Stop;
        } else if (action == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
                case R.id.btup:
                    ptz = PTZ.Up;
                    break;
                case R.id.btleft:
                    ptz = PTZ.Left;
                    break;
                case R.id.btright:
                    ptz = PTZ.Right;
                    break;
                case R.id.btdown:
                    ptz = PTZ.Down;
                    break;
            }
        }
        cameraManager.controlDir(ptz);
        return false;
    }
}
