package com.nlecloud.demo7;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private TextView mTvLight;
	private RadioButton mRbAuto;
	private RadioButton mRbManual;
	private EditText mEtValue;
	private Button mBtnSaveChange;
	private Button mBtnOpen;
	private ImageView mIvLamp;
	private Runnable mGetLightRunnable;
	private boolean lampIsOpen = false;
	// 临界值
	private Double criticalValue = 1000.0;
	// 当前值
	private Double nowValue = 0.0;

	private String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// 设备(网关)id
	private final String lambApiTag = "hklmxcvtvonh";
	private final String lightApiTag = "z_light";

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			SensorInfo lightSensor = (SensorInfo) msg.obj;
			switch (msg.what) {
			case 1:
				nowValue = Double.valueOf(lightSensor.getValue());
				mTvLight.setText(lightSensor.getValue() + lightSensor.getUnit());
				// 如果光照值大于临界值则打开照明灯

				if (mRbAuto.isChecked()) {// 如果选择的是自动模式则自动开照明灯
					if (nowValue < criticalValue) {
						lampIsOpen = true;
						onOff();
					} else {
						lampIsOpen = false;
						onOff();
					}
				}
				break;
			default:
				break;
			}
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					getData();
				}
			}, 1000);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initListener();
		// getData();
	}

	/**
	 * 初始化视图
	 */
	public void initView() {
		mTvLight = (TextView) findViewById(R.id.tvLight);
		mRbAuto = (RadioButton) findViewById(R.id.rbAuto);
		mRbManual = (RadioButton) findViewById(R.id.rbManual);
		mEtValue = (EditText) findViewById(R.id.etValue);
		mEtValue.setText(String.valueOf(criticalValue));
		mBtnSaveChange = (Button) findViewById(R.id.btnSaveChange);
		mBtnOpen = (Button) findViewById(R.id.btnOpen);
		mIvLamp = (ImageView) findViewById(R.id.ivLamp);

	}

	/**
	 * 初始化监听事件和数据
	 */
	public void initListener() {
		// 自动模式
		mRbAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mRbManual.isChecked()) {
					mRbManual.setChecked(!isChecked);
				}
			}
		});
		// 手动模式
		mRbManual.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mRbAuto.isChecked()) {
					mRbAuto.setChecked(!isChecked);
				}
			}
		});
		// 保存设置的临界值
		mBtnSaveChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRbAuto.isChecked()) {
					if (!mEtValue.getText().toString().isEmpty()) {
						criticalValue = Double.valueOf(mEtValue.getText().toString());
					}
					getData();
				} else {
					ShowToast("请先选择自动模式");
				}

			}
		});
		// 手动打开灯
		mBtnOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRbManual.isChecked()) {
					if (!lampIsOpen) {
						lampIsOpen = true;
						onOff();
					} else {
						lampIsOpen = false;
						onOff();
					}
				} else {
					ShowToast("请先选择手动模式");
				}
			}
		});
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

	/**
	 * 获取数据
	 */
	public void getData() {
		if (mGetLightRunnable == null) {
			mGetLightRunnable = createRunnable();
		}
		new Thread(mGetLightRunnable).start();
	}

	/*
	 * 创建获取传感器数据Runnable
	 */
	public Runnable createRunnable() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
				netWorkBusiness.getSensor(deviceId, lightApiTag,
						new NCallBack<BaseResponseEntity<SensorInfo>>(getApplicationContext()) {

							@Override
							protected void onResponse(BaseResponseEntity<SensorInfo> arg0) {
								Message msg = new Message();
								msg.obj = arg0.getResultObj();
								msg.what = 1;
								mHandler.sendMessage(msg);
							}
						});
			}
		};

		return runnable;
	}

	/**
	 * 打开或关闭照明灯
	 */
	public void onOff() {
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
		if (lampIsOpen) {// 打开照明灯

			netWorkBusiness.control(deviceId, lambApiTag, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
				@Override
				protected void onResponse(BaseResponseEntity response) {
					mBtnOpen.setText("关闭");
					lampIsOpen = true;
					mIvLamp.setImageResource(R.drawable.lamp_on);
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
				}
			});

		} else {// 关闭照明灯

			netWorkBusiness.control(deviceId, lambApiTag, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
				@Override
				protected void onResponse(BaseResponseEntity response) {
					mBtnOpen.setText("打开");
					lampIsOpen = false;
					mIvLamp.setImageResource(R.drawable.lamp_off);
					Toast.makeText(getApplicationContext(), "操作成功", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
