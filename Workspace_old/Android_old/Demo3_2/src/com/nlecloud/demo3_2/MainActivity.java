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
		//设置服务器地址
	    HttpEngine.SERVER_URL= "http://www.nlecloud.com";
	    //参数1：账号 ,参数2:密码,参数3:项目标示符
        mAppAction.login("cpzc", "cpzc999","CloudSchool", new ActionCallbackListener<AccessToken>() {
			@Override
			public void onSuccess(AccessToken data) {
				//登录成功设置服务器返回的Token，下次请求将带上Token
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
						"设置时间不能为空！",
						Toast.LENGTH_SHORT).show();
			}else{
			System.out.println("============"+Integer.parseInt(etTime.getText().toString().trim()));
			//测试3：设置轮询时间 测试
			mAppAction.setTimeSpan(Integer.parseInt(etTime.getText().toString().trim()),new ActionCallbackListener<ApiResponse<String>>() {

				@Override
				public void onSuccess(ApiResponse<String> data) {
					Toast.makeText(MainActivity.this,
							"设置成功！轮询时间为：" + etTime.getText().toString().trim() + "秒",
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
