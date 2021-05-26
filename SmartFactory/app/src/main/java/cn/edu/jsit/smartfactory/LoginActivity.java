package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Message;

import cn.edu.jsit.smartfactory.tools.WebServiceHelper;

public class LoginActivity extends BaseActivity {
    private EditText ed_id,ed_pwd;
    private TextView tv_register;
    private Button btn_login;
    private SharedPreferences spPreference;
    private String result;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                SharedPreferences.Editor editor = spPreference.edit();
                editor.putString("pwd",ed_pwd.getText().toString().trim());
                editor.putString("user",ed_id.getText().toString().trim());
                editor.commit();
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(LoginActivity.this,R.string.account_toast_text2,Toast.LENGTH_SHORT).show();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ed_id = findViewById(R.id.account_id);
        ed_pwd =findViewById(R.id.account_psw);
        tv_register = findViewById(R.id.tv_registered);
        btn_login = findViewById(R.id.btn_loginUp);
        spPreference = getSharedPreferences("loginSet",MODE_PRIVATE);
        ed_id.setText(spPreference.getString("user",""));
        ed_pwd.setText(spPreference.getString("pwd",""));
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUp(ed_id.getEditableText().toString(),ed_pwd.getEditableText().toString());
            }
        });
    }

    private void loginUp(String userName, String password){
        if(TextUtils.isEmpty(userName)||TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this,R.string.account_toast_text1,Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

//        WebServiceHelper.GetLogin(new WebServiceHelper.Callback() {
//            @Override
//            public void call(String s) {
//                result=s;
//                if(result.equals("true")){
//                    handler.sendEmptyMessage(1);
//                }
//                else {
//                    handler.sendEmptyMessage(0);
//                }
//            }
//        },userName,password);
    }
}
