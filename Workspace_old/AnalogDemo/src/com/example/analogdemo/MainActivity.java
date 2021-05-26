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
		//打开串口
		openUart();
		//设置数据接收回调
		getData();
		//发送采集命令
		Analog4150ServiceAPI.send4150();
	}

	/**
	 * 更改UI
	 */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				String fireStr = (String) msg.obj;
				mTvFire.setText("火焰 ："+fireStr);
				break;
			case 2:
				String personStr = (String) msg.obj;
				mTvPerson.setText("人体 ："+personStr);
				break;
			case 3:
				String smokeStr = (String) msg.obj;
				mTvSmoke.setText("烟雾 ："+smokeStr);
				break;
			}
		};
	};

	/**
	 * 初始化UI
	 * @Description:  TODO<请描述此方法是做什么的> 
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void initView() {
		mTvFire = (TextView) findViewById(R.id.tv_fire);
		mTvPerson = (TextView) findViewById(R.id.tv_person);
		mTvSmoke = (TextView) findViewById(R.id.tv_smoke);
		/**
		 * 路灯开关
		 */
		findViewById(R.id.btn_street_open).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						//发送路灯开继电器命令
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
		 * 楼道灯开关
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
	 * @Description:  打开串口
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void openUart() {
		//Analog4150ServiceAPI.closeUart();
		Analog4150ServiceAPI.openPort(3,0, 3);
		ReceiveThread thread = new ReceiveThread();
		//设置接收回调监听
		thread.setOnCompleteListener(createOnReceiveCompleteListener());
		thread.start();
	}

	/**
	 * 
	 * @Description:  获取数据
	 * @param 
	 * @return void 
	 * @throw
	 */
	private void getData() {
		Analog4150ServiceAPI.getFire("火焰", new OnFireResponse() {

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
		Analog4150ServiceAPI.getPerson("人体", new OnPersonResponse() {

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
		Analog4150ServiceAPI.getSmork("烟雾", new OnSmorkResponse() {

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
	 * @Description:  接收采集数据回调
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
				//完成采集后继续发送
				Analog4150ServiceAPI.send4150();
			}
		};
	}

	@Override
	protected void onDestroy() {
		//关闭串口
		Analog4150ServiceAPI.closeUart();
		super.onDestroy();
	}

}
