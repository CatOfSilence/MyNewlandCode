package com.example.analogdemo;

import com.example.analoglib.Analog4150ServiceAPI;
import com.example.analoglib.AnalogHelper;
import com.example.analoglib.OnFireResponse;
import com.example.analoglib.OnPersonResponse;
import com.example.analoglib.OnReceiveCompleteListener;
import com.example.analoglib.OnSmorkResponse;
import com.example.analoglib.ReceiveThread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView mTvFire;
	private TextView mTvPerson;
	private TextView mTvSmoke;
	
	public static char[] STREET_OPEN = {0x01,0x05,0x00,0x12,0xFF,0x00,0x2C,0x3F};
	public static char[] STREET_CLOSE = {0x01,0x05,0x00,0x12,0x00,0x00,0x6D,0xCF};
	public static char[] CORRIDOR_OPEN = {0x01,0x05,0x00,0x11,0xFF,0x00,0xDC,0x3F};
	public static char[] CORRIDOR_CLOSE = {0x01,0x05,0x00,0x11,0x00,0x00,0x9D,0xCF};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		//�򿪴���
		openUart();
		//�������ݽ��ջص�
		getData();
		//���Ͳɼ�����
		Analog4150ServiceAPI.send4150();
	}

	/**
	 * ����UI
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String fireStr = (String) msg.obj;
				mTvFire.setText("���� ��"+fireStr);
				break;
			case 2:
				String personStr = (String) msg.obj;
				mTvPerson.setText("���� ��"+personStr);
				break;
			case 3:
				String smokeStr = (String) msg.obj;
				mTvSmoke.setText("���� ��"+smokeStr);
				break;
			}
		};
	};

	/**
	 * ��ʼ��UI
	 * @Description:  TODO<�������˷�������ʲô��> 
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void initView() {
		mTvFire = (TextView) findViewById(R.id.tv_fire);
		mTvPerson = (TextView) findViewById(R.id.tv_person);
		mTvSmoke = (TextView) findViewById(R.id.tv_smoke);
		/**
		 * ·�ƿ���
		 */
		findViewById(R.id.btn_street_open).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//����·�ƿ��̵�������
						Analog4150ServiceAPI.sendRelayControl(STREET_OPEN);
					}
				});
		findViewById(R.id.btn_street_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Analog4150ServiceAPI.sendRelayControl(STREET_CLOSE);
					}
				});
		/**
		 * ¥���ƿ���
		 */
		findViewById(R.id.btn_corridor_open).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Analog4150ServiceAPI.sendRelayControl(CORRIDOR_OPEN);
					}
				});
		findViewById(R.id.btn_corridor_close).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Analog4150ServiceAPI.sendRelayControl(CORRIDOR_CLOSE);
					}
				});
	}

	/**
	 * 
	 * @Description:  �򿪴���
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void openUart() {
		//Analog4150ServiceAPI.closeUart();
		Analog4150ServiceAPI.openPort(3,0, 3);
		ReceiveThread thread = new ReceiveThread();
		//���ý��ջص�����
		thread.setOnCompleteListener(createOnReceiveCompleteListener());
		thread.start();
	}

	/**
	 * 
	 * @Description:  ��ȡ����
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void getData() {
		Analog4150ServiceAPI.getFire("����", new OnFireResponse() {

			@Override
			public void onValue(String value) {
				Message msg = new Message();
				msg.obj = value;
				msg.what = 1;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onValue(boolean value) {
			}
		});
		Analog4150ServiceAPI.getPerson("����", new OnPersonResponse() {

			@Override
			public void onValue(String value) {
				Message msg = new Message();
				msg.obj = value;
				msg.what = 2;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onValue(boolean value) {
			}
		});
		Analog4150ServiceAPI.getSmork("����", new OnSmorkResponse() {

			@Override
			public void onValue(String value) {
				Message msg = new Message();
				msg.obj = value;
				msg.what = 3;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onValue(boolean value) {
			}
		});
	}

	/**
	 * 
	 * @Description:  ���ղɼ����ݻص�
	 * @param @return
	 * @return OnReceiveCompleteListener 
	 * @throw
	 */
	private OnReceiveCompleteListener createOnReceiveCompleteListener() {
		return new OnReceiveCompleteListener() {

			@Override
			public void onError() {
			}

			@Override
			public void onComplete(boolean isfunction4017) {
				//��ɲɼ����������
				Analog4150ServiceAPI.send4150();
			}
		};
	}

	@Override
	protected void onDestroy() {
		//�رմ���
		Analog4150ServiceAPI.closeUart();
		super.onDestroy();
	}

}
