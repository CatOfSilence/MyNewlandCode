package com.newland.smartpark.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.newland.smartpark.utils.SharePreferenceUtil;

/**
 * 实现基础接口,实现全屏
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseInterface {
  public static SharePreferenceUtil sharePreferenceUtil;
  private Toast toast=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Full Screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获得Sharepreferences实例
        sharePreferenceUtil=SharePreferenceUtil.getInstant(BaseActivity.this);
    }
    protected void ShowMessage(String message) {
        toast = toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void addListener() {

    }
}
