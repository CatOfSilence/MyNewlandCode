package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.jsit.smartfactory.tools.HttpRequest;
import cn.edu.jsit.smartfactory.tools.SmartFactoryApplication;

public class MonitorActivity extends BaseActivity implements View.OnClickListener {

    SmartFactoryApplication smartFactory;
    private WebView webView;
    private TextView btn_up, btn_down, btn_left, btn_right;
    private Button toggle_camera;
    private Boolean camera_state = false;
    private LoadThread thread;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                if(camera_state){
                    webView.loadUrl("http://"+smartFactory.getCameraAddress()+
                    "/snapshot.cgi?user=admin&pwd=&strm=0&resolution=32");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        smartFactory = (SmartFactoryApplication) getApplication();
        initView();
        thread = new LoadThread();
        thread.start();
    }

    private void initView() {
        btn_up = findViewById(R.id.btn_ptz_up);
        btn_down = findViewById(R.id.btn_ptz_down);
        btn_left = findViewById(R.id.btn_ptz_left);
        btn_right = findViewById(R.id.btn_ptz_right);
        toggle_camera = findViewById(R.id.btn_camera);
        btn_up.setOnClickListener(this);
        btn_down.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        toggle_camera.setOnClickListener(this);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera:
                if (camera_state) {
                    camera_state = false;
                    toggle_camera.setText(R.string.start_camera);
                } else {
                    camera_state = true;
                    toggle_camera.setText(R.string.close_camera);
                }
                break;
            case R.id.btn_ptz_up:
                HttpRequest.send("http://" + smartFactory.getCameraAddress()+
                "/decoder_control.cgi?command=2&user=admin&pwd=");
                break;
            case R.id.btn_ptz_down:
                HttpRequest.send("http://" + smartFactory.getCameraAddress()+
                        "/decoder_control.cgi?command=0&user=admin&pwd=");
                break;
            case R.id.btn_ptz_left:
                HttpRequest.send("http://" + smartFactory.getCameraAddress()+
                        "/decoder_control.cgi?command=6&user=admin&pwd=");
                break;
            case R.id.btn_ptz_right:
                HttpRequest.send("http://" + smartFactory.getCameraAddress()+
                        "/decoder_control.cgi?command=4&user=admin&pwd=");
                break;

        }
    }

    public class LoadThread extends Thread{
        @Override
        public void run(){
            while(true){
                try{
                    Thread.sleep(500);
                    Message message = new Message();
                    message.what = 1;
                    mHandler.sendMessage(message);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
