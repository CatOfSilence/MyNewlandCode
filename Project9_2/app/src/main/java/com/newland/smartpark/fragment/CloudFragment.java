package com.newland.smartpark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.newland.smartpark.R;
import com.newland.smartpark.base.BaseFragment;


/**
 * 园区环境采集界面
 */

public class CloudFragment extends BaseFragment {
    private  View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fragment只加载一次
        if (null != rootView) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (null != parent) {
                parent.removeView(rootView);
            }
        } else {
            rootView =  inflater.inflate(R.layout.fragment_cloud, null);
        }
        return rootView;
    }


    @Override
    public void getData(Intent intent) {

    }
}
