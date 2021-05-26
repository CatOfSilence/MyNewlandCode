package com.nlecloud.demo4;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.example.demo4.R;

public class MainActivity extends Activity {

	private Button btLamp;
	private TextView tvPersonValue;
	// ���崫�������ص�ֵ1�������ˡ�0��������
	private String str;

	private String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// �豸(����)id
	private final String persionApiTag = "m_body";// ���崫����apiTag
	private final String lambApiTag = "hklmxcvtvonh";// ·��apiTag

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �����ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		btLamp = (Button) findViewById(R.id.btLamp);
		tvPersonValue = (TextView) findViewById(R.id.tvPersonValue);
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
			Toast.makeText(getApplicationContext(), "token��ȡ��,���Ժ�", Toast.LENGTH_SHORT).show();
			return;
		}
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
		switch (v.getId()) {
		case R.id.btGetInfo:
			netWorkBusiness.getSensor(deviceId, persionApiTag, new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {

				@Override
				protected void onResponse(BaseResponseEntity<SensorInfo> arg0) {
					if ("1".equals(arg0.getResultObj().getValue())) {
						str = "����";
					} else {
						str = "����";
					}
					tvPersonValue.setText(str);
				}
			});
			// ����4��ͨ������id��ȡ���µĴ������� ����ȡ���崫������
			break;
		case R.id.btLamp:
			if (btLamp.getText().toString().equals("��")) {
				netWorkBusiness.control(deviceId, lambApiTag, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btLamp.setText("�ر�");
					}
				});

			} else {
				netWorkBusiness.control(deviceId, lambApiTag, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
					@Override
					protected void onResponse(BaseResponseEntity response) {
						btLamp.setText("��");
					}
				});
			}
			break;
		default:
			break;
		}
	}
}
