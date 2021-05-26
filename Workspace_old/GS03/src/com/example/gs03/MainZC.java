package com.example.gs03;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainZC extends Activity {
	//注册界面
	EditText ed_zh, ed_mm, ed_xmm;
	Button bt1, bt2;
	SQLiteDatabase db;
	String user, pass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_zc);
		//绑定控件
		ed_zh = (EditText) findViewById(R.id.EditText_zczh);
		ed_mm = (EditText) findViewById(R.id.EditText_zcmm);
		ed_xmm = (EditText) findViewById(R.id.EditText_qrmm);
		bt1 = (Button) findViewById(R.id.Button_qd);
		bt2 = (Button) findViewById(R.id.Button_gb);
		//注册按钮监听
		bt1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				user = ed_zh.getText().toString().trim();
				pass = ed_mm.getText().toString().trim();
				db = openOrCreateDatabase("smarthome", MODE_PRIVATE, null);
				db.execSQL("create table if not exists UserPass(id integer primary key autoincrement,User text,Pass text)");
				if (user.length() != 0) {
					if (pass.length() >= 6) {
						if (CheckString(pass)) {
							if (pass.equals(ed_xmm.getText().toString().trim())) {
								Cursor cursor = db
										.rawQuery(
												"select * from UserPass where User = ?",
												new String[] { user });
								if (cursor.moveToNext()) {
									Toast.makeText(getApplicationContext(),
											"用户已经存在", 1000).show();
								} else {
									ContentValues cValues = new ContentValues();
									cValues.put("User", user);
									cValues.put("Pass", pass);
									db.insert("UserPass", null, cValues);
									Toast.makeText(getApplicationContext(),
											"用户注册成功", 1000).show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"验证密码不一致", 1000).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "密码格式有误",
									1000).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "密码长度不足6位",
								1000).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "用户名不能为空", 1000)
							.show();
				}
			}
		});
		//取消按钮监听
		bt2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
	//密码格式
	private boolean CheckString(String text) {
		Pattern pattern = Pattern
				.compile(".*[a-zA-Z].*[0-9]||.*[0-9].*[a-zA-Z]");
		Matcher matcher = pattern.matcher(text);
		if (matcher.matches())
			return true;
		else
			return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_zc, menu);
		return true;
	}

}
