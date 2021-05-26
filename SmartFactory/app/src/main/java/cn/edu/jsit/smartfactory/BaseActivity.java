package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void changeLanguage(String language) {
        Locale myLocale;
        switch (language) {
            case "zh":
                myLocale = Locale.SIMPLIFIED_CHINESE;
                break;
            case "zh_rTW":
                myLocale = Locale.TRADITIONAL_CHINESE;
                break;
            default:
                myLocale = Locale.getDefault();
                break;
        }
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf,dm);
    }
}
