package com.nlecloud.demo1;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Http网络请求工具类
 *
 * Created by JH on 2015/10/31.
 */
public class HttpEngine {
	private final static String TAG = "HttpEngine";
	private final static String REQUEST_MOTHOD_POST = "POST";
	private final static String ENCODE_TYPE = "UTF-8";
	private final static int TIME_OUT = 20000;
	public static String SERVER_URL = "http://192.168.14.239:86";
	private static HttpEngine instance = null;

	private HttpEngine() {

	}

	/**
	 * 初始化 HttpEngine
	 */
	public static HttpEngine getInstance() {
		if (instance == null) {
			instance = new HttpEngine();
		}
		return instance;
	}

	/**
	 * POST请求
	 * 
	 * @param url
	 *            请求地址
	 * @param paramsMap
	 *            请求参数
	 * @return    返回数据
	 * @throws IOException
	 */
	public String postHandle(String url, Map<String, String> params) throws IOException {
		//Json打包请求参数数据
		String data = joinParamsJson(params);
		// 打印出请求
		Log.i(TAG, "request: " + data);
		HttpURLConnection connection = getConnection(new URL(SERVER_URL + url));
		connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
		connection.connect();
		OutputStream os = connection.getOutputStream();
		os.write(data.getBytes());
		os.flush();
		if (connection.getResponseCode() == 200) {
			// 获取响应的输入流对象
			InputStream is = connection.getInputStream();
			// 创建字节输出流对象
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// 定义读取的长度
			int len = 0;
			// 定义缓冲区
			byte buffer[] = new byte[1024];
			// 按照缓冲区的大小，循环读取
			while ((len = is.read(buffer)) != -1) {
				// 根据读取的长度写入到os对象中
				baos.write(buffer, 0, len);
			}
			// 释放资源
			is.close();
			baos.close();
			connection.disconnect();
			// 返回字符串
			final String result = new String(baos.toByteArray());
			// 打印出结果
			Log.i(TAG, "response: " + result);
			return result;
		} else {
			Log.i(TAG, Long.toString(connection.getResponseCode()));
			connection.disconnect();
			return null;
		}
	}

	// 获取connection
	private HttpURLConnection getConnection(URL url) {
		HttpURLConnection connection = null;
		// 初始化connection
		try {
			// 根据地址创建URL对象
			// URL url = new URL(SERVER_URL);
			// 根据URL对象打开链接
			connection = (HttpURLConnection) url.openConnection();
			// 设置请求的方式
			connection.setRequestMethod(REQUEST_MOTHOD_POST);
			// 发送POST请求必须设置允许输入，默认为true
			connection.setDoInput(true);
			// 发送POST请求必须设置允许输出
			connection.setDoOutput(true);
			// 设置不使用缓存
			connection.setUseCaches(false);
			// 设置请求的超时时间
			connection.setReadTimeout(TIME_OUT);
			connection.setConnectTimeout(TIME_OUT);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Connection", "keep-alive");
			connection.setRequestProperty("Response-Type", "json");
		
			connection.setChunkedStreamingMode(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}


	// 拼接参数列表
	private String joinParams(Map<String, String> params) {
		StringBuilder stringBuilder = new StringBuilder();
		for (String key : params.keySet()) {
			stringBuilder.append(key);
			stringBuilder.append("=");
			try {
				stringBuilder.append(URLEncoder.encode(params.get(key), ENCODE_TYPE));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			stringBuilder.append("&");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}

	// 拼接json参数
	private String joinParamsJson(Map<String, String> params) {
		JSONObject jsonObject = new JSONObject();
		for (String key : params.keySet()) {
			try {
				jsonObject.put(key, params.get(key));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonObject.toString();
	}
}
