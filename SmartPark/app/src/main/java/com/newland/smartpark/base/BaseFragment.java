package com.newland.smartpark.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.newland.smartpark.golbal.Constants;
import com.newland.smartpark.utils.SharePreferenceUtil;

/**
 * 基础的Fragment 实现接受数据的广播
 */

public abstract class BaseFragment extends Fragment {
    private MyDataReceiver receiver;
    public static SharePreferenceUtil sharePreferenceUtil;
    public Adam4150 adam4150;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (adam4150 == null) {
            adam4150 = new Adam4150();
            adam4150.openPort(Constants.COMS[0], 9600);
        }
        if (sharePreferenceUtil == null) {
            sharePreferenceUtil = SharePreferenceUtil.getInstant(getActivity());
        }



        //注册接收数据的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("senddata");
        receiver = new MyDataReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    /**
     * 获取广播发送过来的数据
     *
     * @param intent
     */
    public abstract void getData(Intent intent);


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    //广播接收者
    private class MyDataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData(intent);
        }
    }
}
