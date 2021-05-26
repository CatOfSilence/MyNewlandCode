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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class V3 extends Activity {
	//模式界面
	json_dispose js = new json_dispose();
	ImageView iv1;
	Handler hd1;
	TextView tvtime;
	RadioGroup rg;
	RadioButton rb1, rb2, rb3, rb4;
	ToggleButton tb1;
	float guangzhao = 0, yanwu = 0, rthw = 0;
	boolean kongtiao = false;
	boolean aa = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_v3);
		//控件绑定
		iv1 = (ImageView) findViewById(R.id.ImageView_v3);
		tvtime = (TextView) findViewById(R.id.TextView_v3_time);
		rg = (RadioGroup) findViewById(R.id.radioGroup1);
		rb1 = (RadioButton) findViewById(R.id.radio0);
		rb2 = (RadioButton) findViewById(R.id.radio1);
		rb3 = (RadioButton) findViewById(R.id.radio2);
		rb4 = (RadioButton) findViewById(R.id.radio3);
		tb1 = (ToggleButton) findViewById(R.id.ToggleButton_moshi);
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
		//图片监听
		iv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		//模式线程代码
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (aa) {
					js.receive();
					try {
						guangzhao = Float.valueOf(js.receive_data.get(Json_data.Illumination).toString());
						yanwu = Float.valueOf(js.receive_data.get(Json_data.Smoke).toString());
						rthw = Float.valueOf(js.receive_data.get(Json_data.StateHumanInfrared).toString());
						Thread.sleep(1000);
						if (tb1.isChecked()) {
							if (rb1.isChecked()) {
								kongtiao = true;
								js.control(Json_data.Lamp, 0, 0);
								Thread.sleep(200);
								js.control(Json_data.Curtain, 0, 1);
								Thread.sleep(200);
								if (guangzhao > 200) {
									js.control(Json_data.Fan, 0, 1);
								} else {
									js.control(Json_data.Fan, 0, 0);
								}
							}
							else if (rb2.isChecked()) {
								kongtiao = true;
								js.control(Json_data.Lamp, 0, 1);
								Thread.sleep(200);
								js.control(Json_data.Curtain, 0, 0);
								Thread.sleep(200);
								if (yanwu > 230) {
									js.control(Json_data.Fan, 0, 1);
								} else {
									js.control(Json_data.Fan, 0, 0);
								}
							}else if (rb3.isChecked()) {
								if (!kongtiao) {
									js.control(Json_data.InfraredLaunch, 0, 1);
									kongtiao = true;
								}
								js.control(Json_data.Lamp, 0, 1);
								Thread.sleep(5000);
								js.control(Json_data.Lamp, 0, 0);
							}else if (rb4.isChecked()) {
								kongtiao = true;
								if (rthw > 0) {
									js.control(Json_data.WarningLight, 0, 1);
									js.control(Json_data.Lamp, 0, 1);
								} 
							}
						}
						else {
							js.control(Json_data.WarningLight, 0, 0);
							kongtiao = true;
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		aa=false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.v3, menu);
		return true;
	}

}
