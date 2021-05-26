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
	// �ٽ�ֵ
	private Double criticalValue = 1000.0;
	// ��ǰֵ
	private Double nowValue = 0.0;

	private String token;
	private final String name = "18259129753";
	private final String pwd = "123456";
	private final String baseUrl = "http://api.nlecloud.com";
	private final String deviceId = "24647";// �豸(����)id
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
				// �������ֵ�����ٽ�ֵ���������

				if (mRbAuto.isChecked()) {// ���ѡ������Զ�ģʽ���Զ���������
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
	 * ��ʼ����ͼ
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
	 * ��ʼ�������¼�������
	 */
	public void initListener() {
		// �Զ�ģʽ
		mRbAuto.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mRbManual.isChecked()) {
					mRbManual.setChecked(!isChecked);
				}
			}
		});
		// �ֶ�ģʽ
		mRbManual.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (mRbAuto.isChecked()) {
					mRbAuto.setChecked(!isChecked);
				}
			}
		});
		// �������õ��ٽ�ֵ
		mBtnSaveChange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mRbAuto.isChecked()) {
					if (!mEtValue.getText().toString().isEmpty()) {
						criticalValue = Double.valueOf(mEtValue.getText().toString());
					}
					getData();
				} else {
					ShowToast("����ѡ���Զ�ģʽ");
				}

			}
		});
		// �ֶ��򿪵�
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
					ShowToast("����ѡ���ֶ�ģʽ");
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
	 * ��ȡ����
	 */
	public void getData() {
		if (mGetLightRunnable == null) {
			mGetLightRunnable = createRunnable();
		}
		new Thread(mGetLightRunnable).start();
	}

	/*
	 * ������ȡ����������Runnable
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
	 * �򿪻�ر�������
	 */
	public void onOff() {
		NetWorkBusiness netWorkBusiness = new NetWorkBusiness(token, baseUrl);
		if (lampIsOpen) {// ��������

			netWorkBusiness.control(deviceId, lambApiTag, 1, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
				@Override
				protected void onResponse(BaseResponseEntity response) {
					mBtnOpen.setText("�ر�");
					lampIsOpen = true;
					mIvLamp.setImageResource(R.drawable.lamp_on);
					Toast.makeText(getApplicationContext(), "�����ɹ�", Toast.LENGTH_SHORT).show();
				}
			});

		} else {// �ر�������

			netWorkBusiness.control(deviceId, lambApiTag, 0, new NCallBack<BaseResponseEntity>(getApplicationContext()) {
				@Override
				protected void onResponse(BaseResponseEntity response) {
					mBtnOpen.setText("��");
					lampIsOpen = false;
					mIvLamp.setImageResource(R.drawable.lamp_off);
					Toast.makeText(getApplicationContext(), "�����ɹ�", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

}
