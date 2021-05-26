package com.nlecloud.demo1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import com.example.qq.R;

public class MainActivity extends Activity {

	private EditText etName, etAge, etBirthday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		etName = (EditText) findViewById(R.id.etName);
		etAge = (EditText) findViewById(R.id.etAge);
		etBirthday = (EditText) findViewById(R.id.etBirthday);
		// 设置服务器地址,部署 Demo1_Server 的IP地址和端口
		HttpEngine.SERVER_URL = "http://192.168.14.239:86";
	}
    /**
     * 点击事件
     * @param v
     */
	public void myclick(View v) {
		new Thread(runnable).start();
	}
	
    /**
     * 发送网络请求Runnable
     */
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Message msg = new Message();
			Bundle data = new Bundle();
			// 请求参数存入Map中
			Map<String, String> params = new HashMap<String, String>();
			params.put("Status", "1");
			params.put("AppendData", "");
			String result = null;
			try {
				result = HttpEngine.getInstance().postHandle("/Index.ashx", params);
			} catch (IOException e) {
				e.printStackTrace();
			}
			data.putString("value", result);
			msg.setData(data);
			handler.sendMessage(msg);
		}
	};

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String value = data.getString("value");
			// 解析JSON数据.
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(value);
				System.out.println("result=============" + value);
				JSONObject user = jsonObject.getJSONObject("AppendData");
				String name = user.getString("UserName");
				String age = user.getString("Age");
				String day = user.getString("BirthDay");
				// 返回的数据写入相对应的EditText中
				etName.setText(name);
				etAge.setText(age);
				etBirthday.setText(day);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};
}