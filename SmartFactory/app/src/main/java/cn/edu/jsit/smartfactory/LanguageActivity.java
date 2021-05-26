package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import cn.edu.jsit.smartfactory.tools.LanguageAdapter;
import cn.edu.jsit.smartfactory.tools.SmartFactoryApplication;

public class LanguageActivity extends BaseActivity {

    private TextView tv_back, tv_title;
    private ListView lv_language;
    private SharedPreferences spPreferences;
    private LanguageAdapter languageAdapter;
    private String newLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        initView();
        initAdapter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                closeActivity();
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void closeActivity() {
        if (!newLanguage.equals(SmartFactoryApplication.language)) {
            SmartFactoryApplication.language = newLanguage;
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        finish();
    }

    private void initView() {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.language_title);
        lv_language = (ListView) findViewById(R.id.lv_language);

    }

    private void initAdapter() {
        String[] data = new String[]{getString(R.string.language_default), getString(R.string.language_zh), getString(R.string.language_zh_rTW)};
        languageAdapter = new LanguageAdapter(this, data);
        lv_language.setAdapter(languageAdapter);
        lv_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                languageAdapter.setCheck(position);
                switch (position) {
                    case 0:
                        setLanguage("default");
                        changeLanguage("default");
                        break;
                    case 1:
                        setLanguage("zh");
                        changeLanguage("zh");
                        break;
                    case 2:
                        setLanguage("zh_rTW");
                        changeLanguage("zh_rTW");
                        break;
                }
                LanguageActivity.this.finish();
                LanguageActivity.this.startActivity(new Intent(LanguageActivity.this,LanguageActivity.class));
            }
        });
        getLanguage();
    }


    public void getLanguage() {
        spPreferences = getSharedPreferences("loginSet", MODE_PRIVATE);
        newLanguage = spPreferences.getString("language", "default");
        switch (newLanguage) {
            case "zh":
                languageAdapter.setCheck(1);
                break;
            case "zh_rTW":
                languageAdapter.setCheck(2);
                break;
            default:
                languageAdapter.setCheck(0);

        }
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor = spPreferences.edit();
        editor.putString("language", language);
        editor.commit();
    }
}
