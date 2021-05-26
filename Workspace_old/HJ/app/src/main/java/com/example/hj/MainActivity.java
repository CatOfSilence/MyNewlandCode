package com.example.hj;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
EditText ed_ip,ed_user,ed_pass;
Button bt_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		intiFuck();
	}

	private void intiFuck() {
		ed_ip = (EditText) findViewById(R.id.EditText_dl_ip);
		ed_user = (EditText) findViewById(R.id.EditText_dl_yhm);
		ed_pass = (EditText) findViewById(R.id.EditText_dl_mm);
		bt_login = (Button) findViewById(R.id.Button_dl);
		
		SocketThread.Port = 6006;
		
		bt_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (ed_user.getText().toString().trim().length()!=0&&ed_user.getText().toString().trim().length()!=0) {
					if (ed_user.getText().toString().trim().equals("bizideal")&&ed_pass.getText().toString().trim().equals("123456")) {
						try {
							SocketThread.SocketIp = ed_ip.getText().toString()
									.trim();
						} catch (Exception e) {
						}
						startActivity(new Intent(MainActivity.this,Vmain.class));
					}
					else {
						Toast.makeText(getApplicationContext(), "’À∫≈ªÚ√‹¬Î¥ÌŒÛ", 1000).show();
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "’À∫≈ªÚ√‹¬Î≤ªƒ‹Œ™ø’", 1000).show();
				}
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
