package cn.com.newland.cloudiiiapidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by marco on 2017/8/21.
 * 基类activity
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onFirst(savedInstanceState);
        setContentView(setLayoutRes());
        setTitle("NLECloud SDK DEMO - " + setTitle());
        instantiateView();
        initViewData();
        registerListener();

    }


    protected void onFirst(Bundle saveInstanceState) {

    }

    protected abstract int setLayoutRes();

    protected abstract String setTitle();

    protected void instantiateView() {
    }

    protected void initViewData() {

    }

    protected void registerListener() {
    }



}
