package com.nlecloud.demo3;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.DeviceInfo;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

import com.example.demo3.R;

public class MainActivity extends Activity {
	private Spinner mSpinner;
	private List<String> list;
	private List<String> apiTagList;
	// ִ����ID
	private int item;
	// �Ƿ��ȡ�б�
	private boolean flag = false;

	private  String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// �豸(����)id

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mSpinner = (Spinner) findViewById(R.id.spinner);
		// ���÷�������ַ
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

		list = new ArrayList<String>();
		list.add("ID�б�");
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item, R.id.tvItem, list);
		mSpinner.setAdapter(adapter);
		mSpinner.setPrompt("ִ����ID�б�");
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				item = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	public void myclick(View v) {
		if (TextUtils.isEmpty(token)) {
			Toast.makeText(getApplicationContext(), "���Ȼ�ȡtoken", Toast.LENGTH_SHORT).show();
			return;
		}
		if (v.getId() != R.id.btGetInfo && !flag) {
			Toast.makeText(getApplicationContext(), "���Ȼ�ȡ�豸�б�", Toast.LENGTH_SHORT).show();
			return;
		}
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);

		switch (v.getId()) {
		case R.id.btOn:
			// ����7������ִ�����豸
			 netWorkBusiness.control(deviceId,apiTagList.get(item), 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
                 @Override
                 protected void onResponse(BaseResponseEntity response) {
                		if (response.getStatus() == 0) {
    						Toast.makeText(getApplicationContext(), "���Ƴɹ�", Toast.LENGTH_SHORT).show();
    					} else {
    						Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
    					}
                 }
             });
			break;
		case R.id.btOff:
			netWorkBusiness.control(deviceId, apiTagList.get(item), 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
				@Override
				protected void onResponse(BaseResponseEntity arg0) {
					if (arg0.getStatus() == 0) {
						Toast.makeText(getApplicationContext(), "���Ƴɹ�", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				}
			});
			break;
		case R.id.btGetInfo:
			// ����5����ȡ�б�
			netWorkBusiness.getDeviceInfo(deviceId, new NCallBack<BaseResponseEntity<DeviceInfo>>(getApplicationContext()) {

				@Override
				protected void onResponse(BaseResponseEntity<DeviceInfo> arg0) {
					if (arg0.getStatus() == 0) {
						Toast.makeText(MainActivity.this, "��ȡ�ɹ�", Toast.LENGTH_SHORT).show();
						list.clear();
						flag = true;
						apiTagList = new ArrayList<String>();
						for (SensorInfo targetSensorInfo : arg0.getResultObj().getSensors()) {
							apiTagList.add(targetSensorInfo.getApiTag());
							list.add(String.valueOf(targetSensorInfo.getName()));
						}
					} else {
						Toast.makeText(getApplicationContext(), "��ȡʧ��", Toast.LENGTH_SHORT).show();
					}
					
					
					
					if (arg0.getStatus() == 0) {
						Toast.makeText(getApplicationContext(), "���Ƴɹ�", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "����ʧ��", Toast.LENGTH_SHORT).show();
					}					
				}
			});
			break;
		default:
			break;
		}
	}
}
