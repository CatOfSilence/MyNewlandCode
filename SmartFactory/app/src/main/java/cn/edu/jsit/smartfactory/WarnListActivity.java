package cn.edu.jsit.smartfactory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jsit.smartfactory.tools.WarnAdapter;
import cn.edu.jsit.smartfactory.tools.WebServiceHelper;

public class WarnListActivity extends BaseActivity {

    private ListView listView;
    private List<String> msgs = new ArrayList<String>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg){
            if(msg.what==0){
                WarnAdapter adapter = new WarnAdapter(WarnListActivity.this,msgs);
                listView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warnlist);
        listView = findViewById(R.id.list_warn);
        WebServiceHelper.GetInfo(new WebServiceHelper.Callback() {
            @Override
            public void call(String s) {
                initWeb(s);
            }
        });
    }

    protected void initWeb(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = (JSONObject) array.get(i);
                String msg = object.getString("Message");
                msgs.add(msg);
            }
            handler.sendEmptyMessage(0);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
