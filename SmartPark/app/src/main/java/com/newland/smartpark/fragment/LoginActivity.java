package com.newland.smartpark.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.newland.smartpark.R;
import com.newland.smartpark.activity.HomeActivity;
import com.newland.smartpark.activity.RegisteActivity;
import com.newland.smartpark.base.BaseActivity;
import com.newland.smartpark.service.MyService;
import com.newland.smartpark.utils.MySQLiteOpenHelper;

public class LoginActivity extends BaseActivity {

    private EditText etUserName;
    private EditText etPwd;
    private CheckBox cbSaveLogin;
    private String userName;
    private String pwd;
    private Button btnLogin;
    private Button btnRegiste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loadData();
        addListener();
    }

    @Override
    public void initView() {
        etUserName = (EditText) findViewById(R.id.et_username);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        cbSaveLogin = (CheckBox) findViewById(R.id.cb_rem_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegiste = (Button) findViewById(R.id.btn_registe);
    }

    @Override
    public void loadData() {
        initViewData();
    }

    @Override
    public void addListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        btnRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisteActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * ???????????????
     */
    private void checkLogin() {
        userName = etUserName.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        //????????????????????????
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        int result = MySQLiteOpenHelper.getInstance(LoginActivity.this).findUserByNameAndPwd(userName, pwd);
        if (cbSaveLogin.isChecked()) {
            sharePreferenceUtil.setIsKeepPWD(true);
            //????????????????????????
            sharePreferenceUtil.setUserName(userName);
            sharePreferenceUtil.setPWD(pwd);
        } else {
            sharePreferenceUtil.setIsKeepPWD(false);
            //????????????????????????
            sharePreferenceUtil.setUserName("");
            sharePreferenceUtil.setPWD("");
        }
        if (result == 1) {                                             //??????1?????????????????????????????????
            //????????????
            startService(new Intent(LoginActivity.this, MyService.class));
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);//?????????HomeActivity
            finish();
        } else if (result == 0) {
            Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();  //??????????????????
        }
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    private boolean isSaveLoginInfo() {
        return sharePreferenceUtil.getIsKeepPWD();
    }


    /**
     * ???????????????EditText Checkbok??????
     */
    private void initViewData() {
        //???????????????????????????????????????????????????????????????CheckBox??????true??????????????????
        cbSaveLogin.setChecked(isSaveLoginInfo());
        //??????CheckBox?????????????????????????????????EcitText???????????????SP?????????
        if (isSaveLoginInfo()) {
            etUserName.setText(sharePreferenceUtil.getUserName());
            etPwd.setText(sharePreferenceUtil.getPWD());
            checkLogin();
        }
    }
}
