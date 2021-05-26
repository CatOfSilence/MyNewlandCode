package com.example.gs03;

import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;

import lib.SocketThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainDL extends Activity {
	// 登錄界面
	Button bt1, bt2;
	EditText ed_zh, ed_mm, ed_ip, ed_port;
	SQLiteDatabase db;
	TextView tvtime, tv1;
	Handler hd1;
	String user, pass;
	SharedPreferences sdf;
	boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_dl);
		//绑定控件
		bt1 = (Button) findViewById(R.id.Button_dl);
		bt2 = (Button) findViewById(R.id.Button_zc);
		ed_zh = (EditText) findViewById(R.id.EditText_zh);
		ed_mm = (EditText) findViewById(R.id.EditText_mm);
		ed_ip = (EditText) findViewById(R.id.EditText_ip);
		ed_port = (EditText) findViewById(R.id.EditText_port);
		tvtime = (TextView) findViewById(R.id.TextView_dl_time);
		tv1 = (TextView) findViewById(R.id.TextView_dl);
		sdf = this.getSharedPreferences("smarthome", MODE_PRIVATE);
		
		//注册按钮监听
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(MainDL.this, MainZC.class));
			}
		});
		//登录按钮监听
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				user = ed_zh.getText().toString().trim();
				pass = ed_mm.getText().toString().trim();
				db = openOrCreateDatabase("smarthome", MODE_PRIVATE, null);
				db.execSQL("create table if not exists UserPass(id integer primary key autoincrement,User text,Pass text)");
				if (user.length() != 0 && pass.length() != 0) {

					Cursor cursor = db
							.rawQuery(
									"select * from UserPass where User = ? and Pass = ?",
									new String[] { user, pass });

					if (cursor.moveToNext()) {
						startActivity(new Intent(MainDL.this, Vmain.class));
						try {
							SocketThread.Port = Integer.valueOf(ed_port
									.getText().toString().trim());
							SocketThread.SocketIp = ed_ip.getText().toString()
									.trim();
						} catch (Exception e) {
							// TODO: handle exception
						}
						sdf.edit().putString("name", user).commit();
						sdf.edit().putString("pass", pass).commit();
						sdf.edit()
								.putString("ip",
										ed_ip.getText().toString().trim())
								.commit();
						sdf.edit()
								.putString("port",
										ed_port.getText().toString().trim())
								.commit();
					} else {
						Toast.makeText(getApplicationContext(), "用户名或密码有误",
								1000).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "用户名或密码有误", 1000)
							.show();
				}
			}
		});
		//时间设置
		hd1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Date d = new Date(System.currentTimeMillis());
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy年MM月dd日 hh:mm:ss");
				String s = sdf.format(d);
				tvtime.setText(s);
				if (!flag) {
					tv1.setText("");
					flag = true;
				} else {
					tv1.setText("加载完毕，请登录...");
					flag = false;
				}
			}
		};
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					hd1.sendEmptyMessage(0);
				}
			}
		}).start();
	}
	//数据存储,默认输入值
	@Override
	protected void onResume() {
		ed_zh.setText(sdf.getString("name", ""));
		ed_mm.setText(sdf.getString("pass", ""));
		ed_ip.setText(sdf.getString("ip", ""));
		ed_port.setText(sdf.getString("port", ""));
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_dl, menu);
		return true;
	}

}
