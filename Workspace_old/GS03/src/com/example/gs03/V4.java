package com.example.gs03;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

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
import android.widget.TextView;
import android.widget.ToggleButton;

public class V4 extends Activity {
	//绘图界面
	json_dispose js = new json_dispose();
	ImageView iv1;
	Handler hd1, hd2;
	TextView tvtime;
	LineDraw lineDraw;
	TableDraw tableDraw;
	ToggleButton tb1;
	public ArrayList<String> XTitle = new ArrayList<String>();
	public ArrayList<String> YTitle = new ArrayList<String>();
	public ArrayList<Float> Values = new ArrayList<Float>();
	public ArrayList<Integer> Colors = new ArrayList<Integer>();
	boolean aa = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_v4);
		//控件绑定
		iv1 = (ImageView) findViewById(R.id.ImageView_v4);
		tvtime = (TextView) findViewById(R.id.TextView_v4_time);
		lineDraw = (LineDraw) findViewById(R.id.LineDraw);
		tableDraw = (TableDraw) findViewById(R.id.TableDraw);
		tb1 = (ToggleButton) findViewById(R.id.ToggleButton_v4);
		//循环增加值添加到数组
		for (int i = 0; i < 5; i++) {
			YTitle.add(400 * i + "");
		}
		//设置数组的值
		XTitle.add(0, "温度");
		XTitle.add(1, "湿度");
		XTitle.add(2, "烟雾");
		XTitle.add(3, "燃气");
		XTitle.add(4, "光照");
		XTitle.add(5, "气压");
		XTitle.add(6, "CO2");
		XTitle.add(7, "PM2.5");
		XTitle.add(8, "人体");
		//颜色数组
		Colors.add(0, getResources().getColor(R.color.wendu));
		Colors.add(1, getResources().getColor(R.color.shidu));
		Colors.add(2, getResources().getColor(R.color.yanwu));
		Colors.add(3, getResources().getColor(R.color.ranqi));
		Colors.add(4, getResources().getColor(R.color.guangzhao));
		Colors.add(5, getResources().getColor(R.color.qiya));
		Colors.add(6, getResources().getColor(R.color.co2));
		Colors.add(7, getResources().getColor(R.color.pm2_5));
		Colors.add(8, getResources().getColor(R.color.rthw));
		//线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (aa) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					hd2.sendEmptyMessage(0);

				}
			}
		}).start();
		//绘图值的添加
		hd2 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (tb1.isChecked()) {
					js.receive();
					try {
						Values.add(
								0,
								Float.valueOf(js.receive_data.get(
										Json_data.Temp).toString()));
						Values.add(
								1,
								Float.valueOf(js.receive_data.get(
										Json_data.Humidity).toString()));
						Values.add(
								2,
								Float.valueOf(js.receive_data.get(
										Json_data.Smoke).toString()));
						Values.add(3, Float.valueOf(js.receive_data.get(
								Json_data.Gas).toString()));
						Values.add(
								4,
								Float.valueOf(js.receive_data.get(
										Json_data.Illumination).toString()));
						Values.add(
								5,
								Float.valueOf(js.receive_data.get(
										Json_data.AirPressure).toString()));
						Values.add(6, Float.valueOf(js.receive_data.get(
								Json_data.Co2).toString()));
						Values.add(
								7,
								Float.valueOf(js.receive_data.get(
										Json_data.PM25).toString()));
						Values.add(7, Float.valueOf(js.receive_data.get(
								Json_data.StateHumanInfrared).toString()));

					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					lineDraw.invalidate();
					tableDraw.invalidate();
				}
			}
		};
		//数组和绘图关联
		lineDraw.drawline(XTitle, YTitle, Values, Colors);
		tableDraw.drawline(XTitle, Values);
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
		//图片
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
		getMenuInflater().inflate(R.menu.v4, menu);
		return true;
	}

}
