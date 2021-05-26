package com.newland.smartpark.base;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;

import java.text.DecimalFormat;


public class ZigbeeOperator {
    private Zigbee zigbee;
    private double[] zigbeeVals;
    private DecimalFormat df;

    //打开串口并获得四输入数据
    public void openPort(int com, int baudRate) {
        zigbee = new Zigbee(DataBusFactory.newSerialDataBus(com, baudRate));
        df = new DecimalFormat("0.00");
    }
    public Double getTemp() {
        try {
            zigbeeVals = zigbee.getFourEnter();
            if(zigbeeVals==null){
                //如果获取到四输入的数组为空，返回温度值为0.00
                return 0.00;
            }
            //温度接in2
            Double temp = FourChannelValConvert.getTemperature(zigbeeVals[1]);
            //保留两位小数
            Double tempValue = Double.valueOf(df.format(temp));
            return tempValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    public double getHumi() {
        try {
            zigbeeVals = zigbee.getFourEnter();
            if(zigbeeVals==null){
                //如果获取到四输入的数组为空，返回湿度值为0.00
                return 0.00;
            }
            //湿度接in3
            Double humi = FourChannelValConvert.getHumidity(zigbeeVals[2]);
            //保留两位小数
            Double humiValue = Double.valueOf(df.format(humi));
            return humiValue;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.00;
    }

    public double getLight() {
        try {
            zigbeeVals = zigbee.getFourEnter();
            if(zigbeeVals==null){
                //如果获取到四输入的数组为空，返回光照值为0.00
                return 0.00;
            }
            //光照接in1
            double light = FourChannelValConvert.getLight(zigbeeVals[0]);
            //保留两位小数
            Double lightValue = Double.valueOf(df.format(light));
            return lightValue;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.00;
    }

    //关闭串口，释放资源
    public void closePort() {
        if (zigbee != null) {
            zigbee.stopConnect();
        }
    }

}
