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
 * Http�������󹤾���
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
	 * ��ʼ�� HttpEngine
	 */
	public static HttpEngine getInstance() {
		if (instance == null) {
			instance = new HttpEngine();
		}
		return instance;
	}

	/**
	 * POST����
	 * 
	 * @param url
	 *            �����ַ
	 * @param paramsMap
	 *            �������
	 * @return    ��������
	 * @throws IOException
	 */
	public String postHandle(String url, Map<String, String> params) throws IOException {
		//Json��������������
		String data = joinParamsJson(params);
		// ��ӡ������
		Log.i(TAG, "request: " + data);
		HttpURLConnection connection = getConnection(new URL(SERVER_URL + url));
		connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
		connection.connect();
		OutputStream os = connection.getOutputStream();
		os.write(data.getBytes());
		os.flush();
		if (connection.getResponseCode() == 200) {
			// ��ȡ��Ӧ������������
			InputStream is = connection.getInputStream();
			// �����ֽ����������
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// �����ȡ�ĳ���
			int len = 0;
			// ���建����
			byte buffer[] = new byte[1024];
			// ���ջ������Ĵ�С��ѭ����ȡ
			while ((len = is.read(buffer)) != -1) {
				// ���ݶ�ȡ�ĳ���д�뵽os������
				baos.write(buffer, 0, len);
			}
			// �ͷ���Դ
			is.close();
			baos.close();
			connection.disconnect();
			// �����ַ���
			final String result = new String(baos.toByteArray());
			// ��ӡ�����
			Log.i(TAG, "response: " + result);
			return result;
		} else {
			Log.i(TAG, Long.toString(connection.getResponseCode()));
			connection.disconnect();
			return null;
		}
	}

	// ��ȡconnection
	private HttpURLConnection getConnection(URL url) {
		HttpURLConnection connection = null;
		// ��ʼ��connection
		try {
			// ���ݵ�ַ����URL����
			// URL url = new URL(SERVER_URL);
			// ����URL���������
			connection = (HttpURLConnection) url.openConnection();
			// ��������ķ�ʽ
			connection.setRequestMethod(REQUEST_MOTHOD_POST);
			// ����POST������������������룬Ĭ��Ϊtrue
			connection.setDoInput(true);
			// ����POST������������������
			connection.setDoOutput(true);
			// ���ò�ʹ�û���
			connection.setUseCaches(false);
			// ��������ĳ�ʱʱ��
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


	// ƴ�Ӳ����б�
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

	// ƴ��json����
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
