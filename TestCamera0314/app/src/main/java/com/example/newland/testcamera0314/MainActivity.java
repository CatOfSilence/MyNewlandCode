package com.example.newland.testcamera0314;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    TextureView tuv;
    EditText ed_user, ed_pw, ed_ip, ed_channel;
    Button bt_open, bt_close, bt_capture;
    CameraManager cameraManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tuv = findViewById(R.id.tuv);
        ed_user = findViewById(R.id.ed_username);
        ed_pw = findViewById(R.id.ed_password);
        ed_ip = findViewById(R.id.ed_ip);
        ed_channel = findViewById(R.id.ed_channel);
        bt_open = findViewById(R.id.bt_open);
        bt_close = findViewById(R.id.bt_close);
        bt_capture = findViewById(R.id.bt_capture);

        findViewById(R.id.bt_up).setOnTouchListener(this);
        findViewById(R.id.bt_down).setOnTouchListener(this);
        findViewById(R.id.bt_left).setOnTouchListener(this);
        findViewById(R.id.bt_right).setOnTouchListener(this);
        cameraManager = CameraManager.getInstance();


        bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                cameraManager.openCamera();
                tuv.setVisibility(View.VISIBLE);
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
        bt_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraManager.capture(Environment.getExternalStorageDirectory().getPath(), "截图.png");
                Toast.makeText(MainActivity.this, "截图成功", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void init() {
        cameraManager.setupInfo(tuv, ed_user.getText().toString(), ed_pw.getText().toString(), ed_ip.getText().toString(), ed_channel.getText().toString());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        PTZ ptz = null;

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            ptz = PTZ.Stop;
        } else if (action == MotionEvent.ACTION_DOWN) {
            switch (view.getId()) {
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