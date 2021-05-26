package com.example.gs03;

import java.sql.Date;
import java.text.SimpleDateFormat;

import lib.SocketThread;
import lib.json_dispose;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Vmain extends Activity {
	//选择界面
	TextView tvtime, tv1, tv2, tv3, tv4;
	ImageView iv1, iv2, iv3, iv4;
	Handler hd1;
	json_dispose js = new json_dispose();
	int count = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vmain);
		//控件绑定
		tvtime = (TextView) findViewById(R.id.TextView_vm_time);
		tv1 = (TextView) findViewById(R.id.TextView_vm1);
		tv2 = (TextView) findViewById(R.id.TextView_vm2);
		tv3 = (TextView) findViewById(R.id.TextView_vm3);
		tv4 = (TextView) findViewById(R.id.TextView_vm4);
		iv1 = (ImageView) findViewById(R.id.ImageView_vm1);
		iv2 = (ImageView) findViewById(R.id.ImageView_vm2);
		iv3 = (ImageView) findViewById(R.id.ImageView_vm3);
		iv4 = (ImageView) findViewById(R.id.ImageView_vm4);
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
		//网络
		SocketThread.mHandlerSocketState = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Bundle bundle = msg.getData();
				if (count == 1) {
					if (bundle.getString("SocketThread_State") == "error") {
						Toast.makeText(getApplicationContext(), "网络连接失败", 1000)
								.show();
					} else {
						Toast.makeText(getApplicationContext(), "网络连接成功", 1000)
								.show();
						count = 0;
					}
				}
			}
		};
		//文字单击监听事件
		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Vmain.this, V1.class));
				iv1.setVisibility(View.VISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			}
		});
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Vmain.this, V2.class));
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.VISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			}
		});
		tv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Vmain.this, V3.class));
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.VISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			}
		});
		tv4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(Vmain.this, V4.class));
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vmain, menu);
		return true;
	}

}
