package com.nlecloud.demo5;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

import com.example.demo5.R;

public class MainActivity extends Activity {

	private TextView tvLight;

	
	private String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// 设备(网关)id
	private final String lightApiTag = "m_light";// 路灯API
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}

	public void initView() {
		tvLight = (TextView) findViewById(R.id.tvLightValue);
		//设置服务器地址
	    //参数1：账号 ,参数2:密码,参数3:项目标示符
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness("", baseUrl);
		netWorkBusiness.signIn(new SignIn(name, pwd), new NCallBack<BaseResponseEntity<User>>(getApplicationContext()) {

			@Override
			protected void onResponse(BaseResponseEntity<User> response) {
				if (response.getStatus() == 0) {
					token = response.getResultObj().getAccessToken();
				} else {
					Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void myclick(View v) {
		if (TextUtils.isEmpty(token)) {
			Toast.makeText(getApplicationContext(), "token获取中,请稍候", Toast.LENGTH_SHORT).show();
			return;
		}
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
		netWorkBusiness.getSensor(deviceId, lightApiTag, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {

			@Override
			protected void onResponse(BaseResponseEntity<SensorInfo> arg0) {
				SensorInfo sensorInfo=arg0.getResultObj();
				tvLight.setText(sensorInfo.getValue());
			}
		});
		
	}

}
