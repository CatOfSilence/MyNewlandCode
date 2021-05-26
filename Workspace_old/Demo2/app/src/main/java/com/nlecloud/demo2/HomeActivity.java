/**
 * 
 */
package com.nlecloud.demo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.nlecloud.nlecloud.R;

/**
 * @author JH
 * 
 */
public class HomeActivity extends BaseActivity {

	private TextView mTvToken;
	private Button mBtnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		initView();
		initListener();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		mTvToken = (TextView) findViewById(R.id.tv_token);
		mBtnLogout = (Button) findViewById(R.id.btn_logout);
		mTvToken.setText(DataStore.token);

	}

	/**
	 * 初始化监听事件
	 */
	public void initListener() {
		mBtnLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(HomeActivity.this, "你点击了按钮",
				// Toast.LENGTH_SHORT).show();
				DataStore.token = "";
				Intent intent = new Intent();
				intent.setClass(HomeActivity.this, MainActivity.class);
				startActivity(intent);
				HomeActivity.this.finish();
			}
		});
	}
}
