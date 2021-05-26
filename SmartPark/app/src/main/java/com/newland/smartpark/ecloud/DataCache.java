package com.newland.smartpark.ecloud;
import android.content.Context;
import com.newland.smartpark.utils.SharePreferenceUtil;
import java.util.Map;
public class DataCache {
    private Context context;
    public Map<String, String> map;
    public SharePreferenceUtil sputil;
    public DataCache(Context context) {
        this.context = context;
        sputil = SharePreferenceUtil.getInstant(context);
        readECloudConnParam();
    }

    //项目标题
    public final String TITLE = "智慧园区";

    public static String address;//="api.nlecloud.com"物联网云服务平台网址
    public static String port;//="80"物联网云服务平台端口号

    public static String gatewayDeviceIdLoRa;//="39518"Lora网关ID
    public static String carbonMonoxideDeviceIdNB;//="39519"一氧化碳设备ID(NB-Iot设备)
    public static String flammableGasDeviceIdNB;//="39520"可燃气设备ID(NB-Iot设备)

    public static String loginname;//="此处换成自己的帐号" 云平台帐号
    public static String password;//="此处换成自己的密码"云平台密码

    public Map<String, String> getMap() {
        return map;
    }

    /**
     * 保存物联网云服务平台地址和端口号
     *
     * @param address 物联网云服务平台地址
     * @param port    物联网云服务平台端口号
     * @return
     */
    public boolean saveECloudConnParam(String address, String port,
                                       String loginname, String password,
                                       String gatewayDeviceIdLoRa,
                                       String carbonMonoxideDeviceIdNB,
                                       String flammableGasDeviceIdNB) {
        this.address = address;
        this.port = port;
        this.loginname = loginname;
        this.password = password;
        this.gatewayDeviceIdLoRa = gatewayDeviceIdLoRa;
        this.carbonMonoxideDeviceIdNB = carbonMonoxideDeviceIdNB;
        this.flammableGasDeviceIdNB = flammableGasDeviceIdNB;
        sputil.setAddressYun(address);
        sputil.setPortYun(port);
        sputil.setUsernameYun(loginname);
        sputil.setPasswordYun(password);
        sputil.setLoRaIDYun(gatewayDeviceIdLoRa);
        sputil.setNB1IDYun(carbonMonoxideDeviceIdNB);
        sputil.setNB2IDYun(flammableGasDeviceIdNB);
        return true;
    }

    /**
     * 读取配置文件参数，并初始化类变量(缓存变量)
     *
     * @return 传感器标识名
     */

    public void readECloudConnParam() {
        address = sputil.getAddressYun();
        port = sputil.getPortYun();
        loginname = sputil.getUsernameYun();
        password = sputil.getPasswordYun();
        gatewayDeviceIdLoRa = sputil.getLoRaIDYun();
        carbonMonoxideDeviceIdNB = sputil.getNB1IDYun();
        flammableGasDeviceIdNB = sputil.getNB2IDYun();
    }

    //为其他类变量添加get方法
    public String getTitle() {
        return TITLE;
    }

    public static String getAddress() {
        return address;
    }

    public static String getPort() {
        return port;
    }

    public static String getGatewayDeviceIdLoRa() {
        return gatewayDeviceIdLoRa;
    }

    public static String getCarbonMonoxideDeviceIdNB() {
        return carbonMonoxideDeviceIdNB;
    }

    public static String getFlammableGasDeviceIdNB() {
        return flammableGasDeviceIdNB;
    }

    public static String getLoginname() {
        return loginname;
    }

    public static String getPassword() {
        return password;
    }
}
