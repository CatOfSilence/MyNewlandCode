package cloudiiiapidemo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import cn.com.newland.cloudiiiapidemo.util.DataCache;
import cn.com.newland.nle_sdk.postDataSet.PostEnum;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.Tools;

public class MainActivity extends BaseActivity {
    private BaseResponseEntity userBaseResponseEntity;
    private PostEnum[] postEnums;
    private ListView listView;
    private ScrollView scrollView;
    @Override
    protected void onFirst(Bundle saveInstanceState) {
        super.onFirst(saveInstanceState);
        userBaseResponseEntity = (BaseResponseEntity) getIntent().getSerializableExtra("userBaseResponseEntity");
        postEnums = PostEnum.values();
    }

    @Override
    protected int setLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected String setTitle() {
        return "首页";
    }

    @Override
    protected void instantiateView() {
        super.instantiateView();
        listView = findViewById(R.id.apiList);
        scrollView = findViewById(R.id.scrollView);
    }

    @Override
    protected void initViewData() {
        super.initViewData();

        TextView tvPostResult = findViewById(R.id.postResult);
        TextView tvLoginMsg = findViewById(R.id.loginMsg);

        if (userBaseResponseEntity!=null&&userBaseResponseEntity.getStatus() == 0) {
            tvPostResult.setTextColor(Color.parseColor("#FF39C42F"));
            tvPostResult.setText("登陆成功，您的登陆信息为 : ");
        } else {
            tvPostResult.setTextColor(Color.parseColor("#FFCE5858"));
            findViewById(R.id.contentLayout).setVisibility(View.GONE);
            tvPostResult.setText("登陆失败，您的登陆信息为 : ");
            return;
        }
        Gson gson = new Gson();
        Tools.printJson(tvPostResult, gson.toJson(userBaseResponseEntity),false);

        listView.setAdapter(new MyAdapter(postEnums));
        scrollView.smoothScrollTo(0,0);
    }

    @Override
    protected void registerListener() {
        super.registerListener();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("apiItem", postEnums[i]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        PostEnum[] itemEntities;
        String baseUrl = DataCache.getBaseUrl(getApplicationContext());


        MyAdapter(PostEnum[] values) {
            this.itemEntities = values;
        }

        @Override
        public int getCount() {
            return itemEntities.length;
        }

        @Override
        public Object getItem(int i) {
            return itemEntities[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, viewGroup, false);
            }
            TextView textView = view.findViewById(R.id.itemName);
            PostEnum postEnum = itemEntities[i];
            textView.setText(String.format("%s%s\n\n%s", baseUrl, postEnum.getUrl(), postEnum.getTitle()));
            return view;
        }
    }
}
