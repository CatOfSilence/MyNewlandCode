package com.nlecloud.demo6;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

import com.example.demo6.R;

public class MainActivity extends Activity {

	private Button btFan, btLamp;
	private TextView tvTemperature, tvHumidity;
	private String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// 设备(网关)id
	private final String humApiTag = "m_humidity";
	private final String temApiTag = "m_temperature";
	private final String fanApiTag = "m_fan";
	private final String lambApiTag = "hklmxcvtvonh";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		btFan = (Button) findViewById(R.id.btFan);
		btLamp = (Button) findViewById(R.id.btLamp);
		tvTemperature = (TextView) findViewById(R.id.tvTemperatureValue);
		tvHumidity = (TextView) findViewById(R.id.tvHumidityValue);
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
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
		switch (v.getId()) {
		case R.id.btFan:
			if (btFan.getText().toString().equals("打开")) {

				// 测试7：开关执行器设备,第二个参数为开(true)关(false)设备 ,打开风扇
				netWorkBusiness.control(deviceId, fanApiTag, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btFan.setText("关闭");
						Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					}
				});
				// 测试7：开关执行器设备,第二个参数为开(true)关(false)设备 ,打开风扇
			} else {
				// 测试7：开关执行器设备,第二个参数为开(true)关(false)设备 ,打开风扇
				netWorkBusiness.control(deviceId, fanApiTag, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btFan.setText("打开");
						Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		case R.id.btLamp:
			if (btLamp.getText().toString().equals("打开")) {
				netWorkBusiness.control(deviceId, lambApiTag, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btLamp.setText("关闭");
						Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					}
				});
				// 测试7：开关执行器设备,第二个参数为开(true)关(false)设备,打开路灯
			} else {
				netWorkBusiness.control(deviceId, lambApiTag, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btLamp.setText("打开");
						Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
					}
				});
			}
			break;
		case R.id.btGetInfo:
			netWorkBusiness.getSensors(deviceId, temApiTag + "," + humApiTag, new NCallBack<BaseResponseEntity<List<SensorInfo>>>(
					getApplicationContext()) {

				@Override
				protected void onResponse(BaseResponseEntity<List<SensorInfo>> arg0) {
					List<SensorInfo> sensorInfos = arg0.getResultObj();
					for (SensorInfo sensorInfo : sensorInfos) {
						if (sensorInfo.getApiTag().equals(humApiTag)) {
							tvHumidity.setText(sensorInfo.getValue() + sensorInfo.getUnit());
						} else if (sensorInfo.getApiTag().equals(temApiTag)) {
							tvTemperature.setText(sensorInfo.getValue() + sensorInfo.getUnit());
						}
					}
				}
			});
			break;
		default:
			break;
		}
	}
}
