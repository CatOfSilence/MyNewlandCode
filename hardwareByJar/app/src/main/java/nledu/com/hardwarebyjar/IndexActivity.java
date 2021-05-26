package nledu.com.hardwarebyjar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class IndexActivity extends AppCompatActivity {
    //    public static final String MODE = "socket";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        ListView listView = findViewById(R.id.listView);

        List<ActivityItem> data = new ArrayList<>();
        data.add(new ActivityItem("led ", ActivityLed.class));

        data.add(new ActivityItem("4150", Activity4150.class));

        data.add(new ActivityItem("RFID", ActivityRFID.class));

        data.add(new ActivityItem("ZIGBEE", ActivityZigbee.class));

        UniversalListAdapter<ActivityItem> adapter = new UniversalListAdapter<ActivityItem>(getApplicationContext(), data, R.layout.item_ommon) {
            @Override
            protected void doSomeThing(ViewHolder holder, int position, Collection<ActivityItem> mData, ActivityItem data) {
                TextView textView = holder.get(R.id.tv);
                textView.setText(data.activityName);
                textView.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), data.activityClass)));
            }
        };
        listView.setAdapter(adapter);


        RadioGroup radioGroup = findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSocket) {
                Util.MODE = "socket";
            } else if (checkedId == R.id.rbSerial) {
                Util.MODE = "serial";
            }
        });
    }

    class ActivityItem {
        private String activityName;
        private Class<? extends Activity> activityClass;

        ActivityItem(String activityName, Class<? extends Activity> activityClass) {
            this.activityName = activityName;
            this.activityClass = activityClass;
        }
    }
}
