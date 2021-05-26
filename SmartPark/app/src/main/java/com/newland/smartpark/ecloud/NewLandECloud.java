package com.newland.smartpark.ecloud;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class NewLandECloud {
	private static Logger logger = Logger.getLogger("NewLandECloud");

	private final String baseURL;
	private static final String PROTOCOL = "http://";
    private static final String LOGIN = "/Users/Login";
    private static final String DEVICES = "/Devices";
    private static final String DATAS = "/Datas";

	public NewLandECloud(){
    	this(null, null);
    }
    public NewLandECloud(String ip, String port){
        if(ip != null && port != null) {
        	baseURL = PROTOCOL + ip + ":" + port;
        }else {
        	baseURL = "http://api.nlecloud.com";
        }
    }

	/**
	 * 登录物联网云服务平台
	 * @param loginname
	 * @param password
	 * @return
	 * @throws RuntimeException
	 * @throws IOException
	 */
	public boolean login(String loginname, String password) throws RuntimeException, IOException {
		String url = baseURL + LOGIN;
		String json = "{'Account':'"+loginname+"','Password':'"+password+"','IsRememberMe':'true'}";

		String response = OkHttpHelper.post(url, json);
		logger.info(response);

		JSONObject jsonObj = JSONObject.parseObject(response);
		Integer status = jsonObj.getInteger("Status");
		if(status.equals(0)) {
	    	OkHttpHelper.accessToken = jsonObj.getJSONObject("ResultObj").getString("AccessToken");
	    	logger.info("accessToken = " + OkHttpHelper.accessToken);
	    	return true;
		}else {
			throw new RuntimeException("连接云平台失败！");
		}
	}

	/**
	 * 批量查询设备最新数据
	 * @param deviceId 多个设备编号
	 * @return 多个传感器值
	 * @throws IOException
	 */
	public Map<String, String> getDevicesData(String ... deviceId) throws IOException {
		if(deviceId == null || deviceId.length == 0) {
			return null;
		}

		StringBuilder url = new StringBuilder(baseURL).append(DEVICES).append(DATAS);
		url.append("?devIds=").append(deviceId[0]);
		System.out.println(url);
		for (int i = 1; i < deviceId.length; i++) {
			url.append(",").append(deviceId[i]);
		}

		String response = OkHttpHelper.get(url.toString());
		logger.info(response);
        System.out.println("传感器数据："+response);
		JSONObject jsonObj = JSONObject.parseObject(response);
		Integer status = jsonObj.getInteger("Status");
		if(!status.equals(0)) {
			throw new RuntimeException("您的请求操作失败，请检查您传入的参数");
		}
		//保存采集到的数据
		Map<String, String> data = new HashMap<>();
		//解析云平台返回的JSON数据
		JSONArray deviceAry = jsonObj.getJSONArray("ResultObj");
		int deviceSize = deviceAry.size();
		//遍历多个设备：Lora网关、NB-Iot(一氧化碳)、NB-Iot(可燃气)
		for (int i = 0; i < deviceSize; i++) {
			JSONObject deviceObj = deviceAry.getJSONObject(i);
			JSONArray sensorAry = deviceObj.getJSONArray("Datas");
			int sensorSize = sensorAry.size();
			//遍历获取每个设备中多个传感器的值，并保存的Map类型的对象中
			for (int j = 0; j < sensorSize; j++) {
				JSONObject sensorObj = sensorAry.getJSONObject(j);

				String apiTag = sensorObj.getString("ApiTag");
				String value = sensorObj.getString("Value");
				data.put(apiTag, value);
			}
		}

		return data;
	}
}