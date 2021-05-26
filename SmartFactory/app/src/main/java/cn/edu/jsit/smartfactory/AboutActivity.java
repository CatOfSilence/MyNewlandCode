package cn.edu.jsit.smartfactory;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
    private TextView version_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
    }

    private void initView(){
        version_info = findViewById(R.id.version_id);
        version_info.setText(R.string.version);
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.about);
        ((TextView)findViewById(R.id.tv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.finishAfterTransition(AboutActivity.this);
            }
        });
    }
}
