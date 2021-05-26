package com.example.newland.testcamera;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    String user,password,ip,channel;
    TextureView textureView;
    CameraManager cameraManager;
    EditText ed_user,ed_password,ed_ip,ed_channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed_user = findViewById(R.id.ed_user);
        ed_password = findViewById(R.id.ed_password);
        ed_ip = findViewById(R.id.ed_ip);
        ed_channel = findViewById(R.id.ed_channel);
        textureView = findViewById(R.id.tuv);

        findViewById(R.id.bt_up).setOnTouchListener(this);
        findViewById(R.id.bt_down).setOnTouchListener(this);
        findViewById(R.id.bt_left).setOnTouchListener(this);
        findViewById(R.id.bt_right).setOnTouchListener(this);

        info();
    }
    public void info(){
        user = ed_user.getText().toString();
        password = ed_password.getText().toString();
        ip = ed_ip.getText().toString();
        channel = ed_channel.getText().toString();
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(textureView,user,password,ip,channel);
    }

    public void open(View view){
        info();
        textureView.setVisibility(View.VISIBLE);
        cameraManager.openCamera();
    }

    public void close(View view){
        if(cameraManager != null){
            cameraManager.releaseCamera();
        }
        textureView.setVisibility(View.GONE);
    }

    public void capture(View view){
        cameraManager.capture(Environment.getExternalStorageDirectory().getPath(),"芜湖起飞.png");
        Toast.makeText(getApplicationContext(),"截图成功，图片存放至："+Environment.getExternalStorageDirectory().getPath(),Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        PTZ ptz = null;
        if(action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP){
            ptz = PTZ.Stop;
        }
        else if(action == MotionEvent.ACTION_DOWN){
            switch (v.getId()){
                case R.id.bt_up:
                    ptz = PTZ.Up;
                    break;
                case R.id.bt_left:
                    ptz = PTZ.Left;
                    break;
                case R.id.bt_down:
                    ptz = PTZ.Down;
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
