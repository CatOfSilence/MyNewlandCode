package com.nlecloud.demo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

import com.nlecloud.nlecloud.R;

public class MainActivity extends BaseActivity {

	private EditText mEtUserName;
	private EditText mEtPassWord;
	private Button mBtnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		initView();
		initListener();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		mEtUserName = (EditText) findViewById(R.id.et_user_name);
		mEtUserName.setText("18259129753");
		mEtPassWord = (EditText) findViewById(R.id.et_pass_word);
		mEtPassWord.setText("123456");
		mBtnLogin = (Button) findViewById(R.id.btn_login);
	}

	/**
	 * 初始化监听事件
	 */
	public void initListener() {
		mBtnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				signin();
			}

		});
	}

	private void signin() {
		String userName = mEtUserName.getText().toString().trim();
		String pwd = mEtPassWord.getText().toString().trim();
		final NetWorkBusiness netWorkBusiness = new NetWorkBusiness("", "http://api.nlecloud.com");
		netWorkBusiness.signIn(new SignIn(userName, pwd),new NCallBack<BaseResponseEntity<User>>(getApplicationContext()) {

			@Override
			protected void onResponse(BaseResponseEntity<User> response) {
				if(response.getStatus()==0){
					String accessToken = response.getResultObj().getAccessToken();
					DataStore.token = accessToken;
					Intent intent = new Intent(MainActivity.this, HomeActivity.class);
					Bundle bundle = new Bundle();
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				
				}else{
					Toast.makeText(getApplicationContext(), response.getMsg(), Toast.LENGTH_SHORT).show();	
					
				}
				
			}
		}); 
	}
}
