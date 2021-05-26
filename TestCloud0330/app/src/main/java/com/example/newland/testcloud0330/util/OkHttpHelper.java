package com.example.newland.testcloud0330.util;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request.Builder;
import okio.BufferedSink;

public class OkHttpHelper {
	//声明媒体类型为JSON
	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private static final String TOKEN_KEY = "AccessToken";
	//使用okhttp
	public static OkHttpClient client = new OkHttpClient();
	//保存token值(登录一次后，获取的token值可以重复多次使用)
	public static String accessToken;

	public static String get(String url) throws IOException {
		Request.Builder builder = new Request.Builder();
		if (accessToken != null)
			builder.addHeader(TOKEN_KEY, accessToken);

		Request request = builder.url(url).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}

	public static String post(String url, String json) throws IOException {
		RequestBody requestBody = RequestBody.create(JSON, json);
		Builder builder = new Request.Builder();
		if (accessToken != null)
			builder.addHeader(TOKEN_KEY, accessToken);
		Request request = builder.url(url).post(requestBody).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}


	public static String sendCmd() throws IOException {
		RequestBody requestBody = new RequestBody() {
			@Nullable
			@Override
			public MediaType contentType() {
				return MediaType.parse("*/*; charset=utf-8");
			}

			@Override
			public void writeTo(BufferedSink bufferedSink) throws IOException {
				bufferedSink.writeUtf8("0");
			}
		};
		Builder builder = new Request.Builder();
		if (accessToken != null)
			builder.addHeader(TOKEN_KEY, accessToken);
		Request request = builder.url("http://api.nlecloud.com/Cmds?deviceId=225515&apiTag=z_fan").post(requestBody).build();
		Response response = client.newCall(request).execute();
		return response.body().string();
	}
}
