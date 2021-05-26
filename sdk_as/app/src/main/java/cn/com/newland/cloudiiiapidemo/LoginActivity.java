package cn.com.newland.cloudiiiapidemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.newland.cloudiiiapidemo.util.Constants;
import cn.com.newland.cloudiiiapidemo.util.DataCache;
import cn.com.newland.cloudiiiapidemo.util.SPHelper;
import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class LoginActivity extends BaseActivity {
    private EditText etUserName;
    private EditText etPwd;
    private TextView tvTip;

    private SPHelper spHelper;

    @Override
    protected void onFirst(Bundle saveInstanceState) {
        super.onFirst(saveInstanceState);
        spHelper = SPHelper.getInstant(getApplicationContext());
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    protected String setTitle() {
        return "登陆";
    }

    @Override
    protected void instantiateView() {
        super.instantiateView();
        etUserName = findViewById(R.id.userName);
        etPwd = findViewById(R.id.pwd);
        tvTip = findViewById(R.id.tip);
    }

    @Override
    protected void initViewData() {
        super.initViewData();
        setTipInfo();
    }

    @Override
    protected void registerListener() {
        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), SettingActivity.class), 1);

            }
        });
        findViewById(R.id.signIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setTipInfo() {
        String baseUrl = DataCache.getBaseUrl(getApplicationContext());
        if (!TextUtils.isEmpty(baseUrl)) tvTip.setText("您的登陆请求地址为:\n" + baseUrl + "Users/Login");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            setTipInfo();
        }
    }

    private void signIn() {
        String platformAddress = spHelper.getStringFromSP(getApplicationContext(), Constants.SETTING_PLATFORM_ADDRESS);
        String port = spHelper.getStringFromSP(getApplicationContext(), Constants.SETTING_PORT);

        final String userName = etUserName.getText().toString();
        final String pwd = etPwd.getText().toString();
        if (TextUtils.isEmpty(platformAddress) || TextUtils.isEmpty(port)) {
            Toast.makeText(getApplicationContext(), "请设置云平台信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        final NetWorkBusiness netWorkBusiness = new NetWorkBusiness("", DataCache.getBaseUrl(getApplicationContext()));
        netWorkBusiness.signIn(new SignIn(userName, pwd), new NCallBack<BaseResponseEntity<User>>(getApplicationContext()) {
            @Override
            protected void onResponse(BaseResponseEntity<User> response) {
                DataCache.updateUserName(getApplicationContext(), userName);
                DataCache.updatePwd(getApplicationContext(), pwd);
                String accessToken = response.getResultObj().getAccessToken();
                DataCache.updateAccessToken(getApplicationContext(), accessToken);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("userBaseResponseEntity", response);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }
}

