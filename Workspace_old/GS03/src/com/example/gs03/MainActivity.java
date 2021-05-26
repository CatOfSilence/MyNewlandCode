package com.example.gs03;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	// 加载界面
	Handler hd1;
	int i;
	TextView tv1, tv2, tv3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 绑定控件
		tv1 = (TextView) findViewById(R.id.TextView_1);
		tv2 = (TextView) findViewById(R.id.TextView_2);
		tv3 = (TextView) findViewById(R.id.TextView_3);
		// Handler加载进度
		hd1 = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (i == 99) {
					startActivity(new Intent(MainActivity.this, MainDL.class));
				}
				tv3.setText("Loading.." + i + "%");
				if (msg.what == 1) {
					tv2.setText("正在加载，请稍后.");
				} else if (msg.what == 2) {
					tv2.setText("正在加载，请稍后..");
				} else if (msg.what == 3) {
					tv2.setText("正在加载，请稍后...");
				}
			}
		};
		// 线程
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (i = 0; i < 99; i++) {
					Message msg = new Message();
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i / 10 % 3 == 1) {
						msg.what = 1;
					} else if (i / 10 % 3 == 2) {
						msg.what = 2;
					} else if (i / 10 % 3 == 0) {
						msg.what = 3;
					}
					hd1.sendMessage(msg);
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
