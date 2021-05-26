package nledu.com.p2pyuantaicamera_demo;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;

import nledu.com.ipcamera.CameraManager;
import nledu.com.ipcamera.PTZ;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private View layer;
    private TextureView textureView;
    private CameraManager cameraManager;

    private EditText etUserName;
    private EditText etPwd;
    private EditText etIP;
    private EditText etChannel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layer = findViewById(R.id.temp);
        textureView = findViewById(R.id.svCamera);
        etUserName = findViewById(R.id.etUserName);
        etPwd = findViewById(R.id.etPwd);
        etIP = findViewById(R.id.etIP);
        etChannel = findViewById(R.id.etChannel);

        //register event
        findViewById(R.id.up).setOnTouchListener(this);
        findViewById(R.id.left).setOnTouchListener(this);
        findViewById(R.id.right).setOnTouchListener(this);
        findViewById(R.id.down).setOnTouchListener(this);

        initCameraManager();
    }

    private void initCameraManager() {
        String userName = etUserName.getText().toString();
        String pwd = etPwd.getText().toString();
        String ip = etIP.getText().toString();
        String channel = etChannel.getText().toString();
        cameraManager = CameraManager.getInstance();
        cameraManager.setupInfo(textureView, userName, pwd, ip, channel);
    }

    public void open(View view) {
        layer.setVisibility(View.GONE);
        initCameraManager();
        cameraManager.openCamera();
    }

    public void release(View view) {
        if (cameraManager != null) {
            cameraManager.releaseCamera();
        }
        layer.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {
        int action = arg1.getAction();
        PTZ ptz = null;
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            ptz = PTZ.Stop;
        } else if (action == MotionEvent.ACTION_DOWN) {
            int viewId = arg0.getId();
            switch (viewId) {
                case R.id.up:
                    ptz = PTZ.Up;
                    break;
                case R.id.down:
                    ptz = PTZ.Down;
                    break;
                case R.id.left:
                    ptz = PTZ.Left;
                    break;
                case R.id.right:
                    ptz = PTZ.Right;
                    break;
            }
        }
        cameraManager.controlDir(ptz);
        return false;
    }

    public void capture(View view) {
        cameraManager.capture(Environment.getExternalStorageDirectory().getPath(), "abc.png");
    }
}