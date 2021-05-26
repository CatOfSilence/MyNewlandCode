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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class V2 extends Activity {
	//联动界面
	json_dispose js = new json_dispose();
	ImageView iv1;
	Handler hd1,hd2;
	TextView tvtime;
	CheckBox ch1, ch2;
	Spinner sp1_1, sp1_2, sp2_1, sp2_2;
	EditText ed1, ed2;
	float yz1 = 0, yz2 = 0, cjz1 = 0, cjz2 = 0, wendu = 0, guangzhao = 0;
	boolean tj1 = false, tj2 = false;
	boolean aa = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_v2);
		//控件绑定
		iv1 = (ImageView) findViewById(R.id.ImageView_v2);
		tvtime = (TextView) findViewById(R.id.TextView_v2_time);
		ch1 = (CheckBox) findViewById(R.id.CheckBox1);
		ch2 = (CheckBox) findViewById(R.id.CheckBox2);
		sp1_1 = (Spinner) findViewById(R.id.Spinner1_1);
		sp1_2 = (Spinner) findViewById(R.id.Spinner1_2);
		sp2_1 = (Spinner) findViewById(R.id.Spinner2_1);
		sp2_2 = (Spinner) findViewById(R.id.Spinner2_2);
		ed1 = (EditText) findViewById(R.id.EditText_v2_1);
		ed2 = (EditText) findViewById(R.id.EditText_v2_2);
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
		//监听
		ch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					if (ed1.getText().toString().trim().length() != 0) {
						yz1 = Float.valueOf(ed1.getText().toString().trim());
						switch (sp1_1.getSelectedItemPosition()) {
						case 0:
							cjz1 = wendu;
							break;
						case 1:
							cjz1 = guangzhao;
							break;

						default:
							break;
						}
						switch (sp1_2.getSelectedItemPosition()) {
						case 0:
							tj1 = cjz1 > yz1;
							break;
						case 1:
							tj1 = cjz1 <= yz1;
							break;

						default:
							break;
						}
					} else {
						Toast.makeText(getApplicationContext(), "风扇数值填写错误",
								1000).show();
						ch1.setChecked(false);
					}
				} else {
					tj1 = false;
				}
			}
		});
		ch2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					if (ed2.getText().toString().trim().length() != 0) {
						yz2 = Float.valueOf(ed2.getText().toString().trim());
						cjz2 = guangzhao;
						switch (sp1_1.getSelectedItemPosition()) {
						case 0:
							tj2 = cjz2 > yz2;
							break;
						case 1:
							tj2 = cjz2 <= yz2;
							break;

						default:
							break;
						}
					} else {
						Toast.makeText(getApplicationContext(), "窗帘数值填写错误",
								1000).show();
						ch2.setChecked(false);
					}
				} else {
					tj2 = false;
				}
			}
		});
		//联动线程
		new Thread(new Runnable() {
			
			@Override
			public void run() {
			while (aa) {
				js.receive();
				try {
					Thread.sleep(500);
					wendu = Float.valueOf(js.receive_data.get(Json_data.Temp).toString());
					guangzhao = Float.valueOf(js.receive_data.get(Json_data.Illumination).toString());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				hd2.sendEmptyMessage(0);
			}
			}
		}).start();
		//联动Handler
		hd2 = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (tj1) {
					js.control(Json_data.Fan, 0, 1);
				}
				if (tj2) {
					switch (sp2_2.getSelectedItemPosition()) {
					case 0:
						js.control(Json_data.Curtain, 0, 1);
						break;
					case 1:
						js.control(Json_data.Curtain, 0, 0);
						break;
					default:
						break;
					}
				}
			}
		};
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		aa=false;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.v2, menu);
		return true;
	}

}
