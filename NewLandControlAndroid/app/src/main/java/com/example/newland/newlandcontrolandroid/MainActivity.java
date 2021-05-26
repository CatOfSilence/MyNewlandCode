package com.example.newland.newlandcontrolandroid;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    IndexActivity indexActivity;
    SetActivity setActivity;
    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.bottom_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, indexActivity).commitAllowingStateLoss();
                    return true;
                case R.id.bottom_set:
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frameLayout, setActivity).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        indexActivity = new IndexActivity();
        setActivity = new SetActivity();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frameLayout, indexActivity).commitAllowingStateLoss();

    }
}
