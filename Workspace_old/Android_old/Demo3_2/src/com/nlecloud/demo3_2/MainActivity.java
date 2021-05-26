package com.nlecloud.demo3_2;

import com.example.demo3_2.R;
import com.nlecloud.nlecloudlibrary.api.ApiResponse;
import com.nlecloud.nlecloudlibrary.api.net.HttpEngine;
import com.nlecloud.nlecloudlibrary.core.ActionCallbackListener;
import com.nlecloud.nlecloudlibrary.core.AppAction;
import com.nlecloud.nlecloudlibrary.core.AppActionImpl;
import com.nlecloud.nlecloudlibrary.domain.AccessToken;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText etTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		etTime = (EditText) findViewById(R.id.etTime);
		mAppAction = new AppActionImpl(this);
		//���÷�������ַ
	    HttpEngine.SERVER_URL= "http://www.nlecloud.com";
	    //����1���˺� ,����2:����,����3:��Ŀ��ʾ��
        mAppAction.login("cpzc", "cpzc999","CloudSchool", new ActionCallbackListener<AccessToken>() {
			@Override
			public void onSuccess(AccessToken data) {
				//��¼�ɹ����÷��������ص�Token���´����󽫴���Token
				HttpEngine.ACCESSTOKEN = data.getAccessToken();
//				System.out.println("======================"+data.getAccessToken());
			}
			@Override
			public void onFailure(String errorEven, String message) {
				Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void myclick(View v) {
		switch (v.getId()) {
		case R.id.btSet:
			if(etTime.getText().toString().trim().equals("")){
				Toast.makeText(MainActivity.this,
						"����ʱ�䲻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
			}else{
			System.out.println("============"+Integer.parseInt(etTime.getText().toString().trim()));
			//����3��������ѯʱ�� ����
			mAppAction.setTimeSpan(Integer.parseInt(etTime.getText().toString().trim()),new ActionCallbackListener<ApiResponse<String>>() {

				@Override
				public void onSuccess(ApiResponse<String> data) {
					Toast.makeText(MainActivity.this,
							"���óɹ�����ѯʱ��Ϊ��" + etTime.getText().toString().trim() + "��",
							Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onFailure(String errorEven, String message) {
					Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
				}
			});
			}
			break;
		default:
			break;
		}
	}
}
