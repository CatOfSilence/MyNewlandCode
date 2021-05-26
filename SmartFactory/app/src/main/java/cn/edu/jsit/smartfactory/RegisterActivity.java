package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Message;

import cn.edu.jsit.smartfactory.tools.WebServiceHelper;

public class RegisterActivity extends BaseActivity {
    private Button registered_button;
    private EditText id_edit, psw_edit, phone_edit, email_edit;
    private String id, psw, phone,sex, email, result;
    private RadioGroup radioGroup;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==0){
                Toast.makeText(RegisterActivity.this,result,Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registered_button = findViewById(R.id.btn_registered);
        id_edit = findViewById(R.id.registered_id);
        psw_edit = findViewById(R.id.registered_psw);
        phone_edit = findViewById(R.id.registered_phone);
        email_edit = findViewById(R.id.registered_phone);
        radioGroup = findViewById(R.id.rg_sex);
        ((TextView) findViewById(R.id.tv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(RegisterActivity.this);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rg_male:
                        sex = "男";
                        break;
                    case R.id.rg_female:
                        sex = "女";
                        break;
                }
            }
        });
        registered_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = id_edit.getText().toString().trim();
                psw = psw_edit.getText().toString().trim();
                phone=phone_edit.getText().toString().trim();
                email = email_edit.getText().toString().trim();
                if(id.equals("")&&psw.equals("")){
                    Toast.makeText(RegisterActivity.this, R.string.account_toast_text1,Toast.LENGTH_SHORT).show();
                }
                else {
                    WebServiceHelper.SetReg(new WebServiceHelper.Callback() {
                        @Override
                        public void call(String s) {
                            result = s;
                            handler.sendEmptyMessage(0);
                        }
                    },id,psw,sex,phone,email);
                }
            }
        });
    }
}
