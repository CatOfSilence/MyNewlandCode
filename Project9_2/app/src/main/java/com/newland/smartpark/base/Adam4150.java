package com.newland.smartpark.base;

import com.nle.mylibrary.forUse.mdbus4150.MdBus4150SensorListener;
import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;


public class Adam4150 {
    private Modbus4150 modbus4150;
    private int hasPerson=1;//默认无人
    private int switchState;
    private int hasSmoke;

    public void openPort(int com, int baudRate) {
        modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(com, baudRate));
    }

    //获得烟雾的数据
    public String getSmoke(int port) {
        try {
            modbus4150.getVal(port, new MdBus4150SensorListener() {
                @Override
                public void onVal(int i) {
                    hasSmoke = i;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasSmoke == 1) {
            return "有烟";
        }
        return "无烟";
    }
    //获得红外对射的数据
    public String getInfrared(int port) {
        try {
            modbus4150.getVal(port, new MdBus4150SensorListener() {
                @Override
                public void onVal(int i) {
                        hasPerson = i;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasPerson == 0) {
            return "有人";
        }
        return "无人";
    }
    //获得微动开关状态
    public boolean getMicroSwitch(int port) {
        try {
            modbus4150.getVal(port, new MdBus4150SensorListener() {
                @Override
                public void onVal(int i) {
                        switchState=i;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (switchState == 0) {
            return false;
        }
        return true;
    }

    //开继电器
    public void openRelay(final int port) {
        try {
            modbus4150.openRelay(port, isSuccess -> System.out.println("开DO" + port + "对应的继电器"+isSuccess));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //关继电器
    public void closeRelay(final int port) {
        try {
            modbus4150.closeRelay(port, isSuccess -> System.out.println("关DO" + port + "对应的继电器"+isSuccess));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭串口，释放资源
    public void closePort() {
        modbus4150.stopConnect();
    }
}
