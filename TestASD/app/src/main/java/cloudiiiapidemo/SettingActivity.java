package cloudiiiapidemo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.newland.cloudiiiapidemo.util.Constants;
import cn.com.newland.cloudiiiapidemo.util.DataCache;
import cn.com.newland.cloudiiiapidemo.util.SPHelper;

public class SettingActivity extends BaseActivity {
    private TextView tvGateWayTag;
    private TextView tvPlatformAddress;
    private TextView tvPort;

    private SPHelper spHelper;

    @Override
    protected void onFirst(Bundle saveInstanceState) {
        super.onFirst(saveInstanceState);
        spHelper = SPHelper.getInstant(getApplicationContext());
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_setting;
    }

    @Override
    protected String setTitle() {
        return "设置";
    }

    @Override
    protected void instantiateView() {
        super.instantiateView();
        tvGateWayTag = findViewById(R.id.Tag);
        tvPlatformAddress = findViewById(R.id.platformAddress);
        tvPort = findViewById(R.id.port);
    }

    @Override
    protected void initViewData() {
        super.initViewData();
        tvGateWayTag.setText(spHelper.getStringFromSP(getApplicationContext(), Constants.SETTING_GATEWAY_TAG));
        tvPlatformAddress.setText(spHelper.getStringFromSP(getApplicationContext(), Constants.SETTING_PLATFORM_ADDRESS));
        tvPort.setText(spHelper.getStringFromSP(getApplicationContext(), Constants.SETTING_PORT));
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSetting();
            }
        });
    }

    private void saveSetting() {
        String platformAddress = tvPlatformAddress.getText().toString();
        String port = tvPort.getText().toString();
        String gateWayTag = tvGateWayTag.getText().toString();
        if (!TextUtils.isEmpty(platformAddress) && !TextUtils.isEmpty(port)) {
            DataCache.updateBaseUrl(getApplicationContext(), "http://" + platformAddress + ":" + port + "/");
        }
        DataCache.updateGateWayTag(getApplicationContext(), gateWayTag);

        spHelper.putData2SP(getApplicationContext(), Constants.SETTING_PLATFORM_ADDRESS, platformAddress);
        spHelper.putData2SP(getApplicationContext(), Constants.SETTING_PORT, port);
        Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
        this.setResult(2);
        finish();
    }
}
