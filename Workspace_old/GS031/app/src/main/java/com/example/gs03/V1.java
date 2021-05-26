package com.example.gs03;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONException;

import lib.Json_data;
import lib.json_dispose;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class V1 extends Activity {
	//基本界面
	json_dispose js = new json_dispose();
	Handler hd1, hd2;
	TextView tvtime, tv1;
	ImageView iv1;
	EditText ed_wendu, ed_shidu, ed_yanwu, ed_ranqi, ed_guangzhao, ed_qiya,
			ed_co2, ed_pm25, ed_rthw;
	ToggleButton tb_lamp1, tb_lamp2, tb_chuanglian, tb_tv, tb_kongtiao, tb_dvd,
			tb_fan, tb_baojingdeng, tb_menjin;
	boolean aa = true;
	Animation mAnimation = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_v1);
		//绑定控件
		tvtime = (TextView) findViewById(R.id.TextView_v1_time);
		tv1 = (TextView) findViewById(R.id.TextView_v1);
		iv1 = (ImageView) findViewById(R.id.ImageView_v1);
		ed_wendu = (EditText) findViewById(R.id.EditText_wendu);
		ed_shidu = (EditText) findViewById(R.id.EditText_shidu);
		ed_yanwu = (EditText) findViewById(R.id.EditText_yanwu);
		ed_ranqi = (EditText) findViewById(R.id.EditText_ranqi);
		ed_guangzhao = (EditText) findViewById(R.id.EditText_guangzhao);
		ed_qiya = (EditText) findViewById(R.id.EditText_qiya);
		ed_co2 = (EditText) findViewById(R.id.EditText_Co2);
		ed_pm25 = (EditText) findViewById(R.id.EditText_Pm25);
		ed_rthw = (EditText) findViewById(R.id.EditText_rthw);
		tb_lamp1 = (ToggleButton) findViewById(R.id.ToggleButton_lamp1);
		tb_lamp2 = (ToggleButton) findViewById(R.id.ToggleButton_lamp2);
		tb_chuanglian = (ToggleButton) findViewById(R.id.ToggleButton_chuanglian);
		tb_tv = (ToggleButton) findViewById(R.id.ToggleButton_tv);
		tb_kongtiao = (ToggleButton) findViewById(R.id.ToggleButton_kongtiao);
		tb_dvd = (ToggleButton) findViewById(R.id.ToggleButton_dvd);
		tb_fan = (ToggleButton) findViewById(R.id.ToggleButton_fan);
		tb_baojingdeng = (ToggleButton) findViewById(R.id.ToggleButton_baojingdeng);
		tb_menjin = (ToggleButton) findViewById(R.id.ToggleButton_menjin);
		//控件无法被选中
		ed_wendu.setFocusable(false);
		ed_shidu.setFocusable(false);
		ed_yanwu.setFocusable(false);
		ed_ranqi.setFocusable(false);
		ed_guangzhao.setFocusable(false);
		ed_qiya.setFocusable(false);
		ed_co2.setFocusable(false);
		ed_pm25.setFocusable(false);
		ed_rthw.setFocusable(false);
		//采集参数线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (aa) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					hd2.sendEmptyMessage(0);
				}
			}
		}).start();
		//采集数据的Handler
		hd2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				js.receive();
				try {
					ed_wendu.setText(js.receive_data.get(Json_data.Temp).toString());
					ed_shidu.setText(js.receive_data.get(Json_data.Humidity).toString());
					ed_yanwu.setText(js.receive_data.get(Json_data.Smoke).toString());
					ed_ranqi.setText(js.receive_data.get(Json_data.Gas).toString());
					ed_guangzhao.setText(js.receive_data.get(Json_data.Illumination).toString());
					ed_qiya.setText(js.receive_data.get(Json_data.AirPressure).toString());
					ed_co2.setText(js.receive_data.get(Json_data.Co2).toString());
					ed_pm25.setText(js.receive_data.get(Json_data.PM25).toString());
					ed_rthw.setText(js.receive_data.get(Json_data.StateHumanInfrared).toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}
		};
		//时间
		hd1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Date d = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy年MM月dd日 hh:mm:ss");
				String s = sdf.format(d);
				tvtime.setText(s);
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					hd1.sendEmptyMessage(0);
				}
			}
		}).start();
		
		//电器控制
		tb_lamp1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.Lamp, 0, 4);
			}
			else {
				js.control(Json_data.Lamp, 0, 4);
			}
			}
		});
		tb_lamp2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.Lamp, 0, 8);
			}
			else {
				js.control(Json_data.Lamp, 0, 8);
			}
			}
		});
		tb_chuanglian.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.Curtain, 0, 1);
			}
			else {
				js.control(Json_data.Curtain, 0, 0);
			}
			}
		});
		tb_tv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.InfraredLaunch, 0, 1);
			}
			else {
				js.control(Json_data.InfraredLaunch, 0, 1);
			}
			}
		});
		tb_kongtiao.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					js.control(Json_data.InfraredLaunch, 0, 1);
				}
				else {
					js.control(Json_data.InfraredLaunch, 0, 1);
				}
			}
		});
		tb_dvd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					js.control(Json_data.InfraredLaunch, 0, 1);
				}
				else {
					js.control(Json_data.InfraredLaunch, 0, 1);
				}
			}
		});
		tb_fan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.Fan, 0, 1);
			}
			else {
				js.control(Json_data.Fan, 0, 0);
			}
			}
		});
		tb_baojingdeng.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			if (arg1) {
				js.control(Json_data.WarningLight, 0, 1);
			}
			else {
				js.control(Json_data.WarningLight, 0, 0);
			}
			}
		});
		tb_menjin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					js.control(Json_data.RFID_Open_Door, 0, 1);
					tv1.setText("欢迎主人回家!");
					mAnimation = AnimationUtils.loadAnimation(V1.this,
							R.anim.anim);
					tv1.setAnimation(mAnimation);
					mAnimation.start();
				} else {
					js.control(Json_data.RFID_Open_Door, 0, 0);
					tv1.setText("");
				}
			}
		});
		//图片监听
		iv1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			finish();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		aa=false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.v1, menu);
		return true;
	}

}
