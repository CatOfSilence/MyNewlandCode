package com.example.newland.testcloud0408;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.com.newland.nle_sdk.requestEntity.SignIn;
import cn.com.newland.nle_sdk.responseEntity.DeviceInfo;
import cn.com.newland.nle_sdk.responseEntity.SensorDataRecord;
import cn.com.newland.nle_sdk.responseEntity.SensorInfo;
import cn.com.newland.nle_sdk.responseEntity.User;
import cn.com.newland.nle_sdk.responseEntity.base.BaseResponseEntity;
import cn.com.newland.nle_sdk.util.NCallBack;
import cn.com.newland.nle_sdk.util.NetWorkBusiness;

public class MainActivity extends AppCompatActivity {
    NetWorkBusiness nb;
    String Token = "07D8C6C1B91E83D83F59DD9E2567984562EA7D00D8A8AC3EBCE58A0CF96CD350FA5FD9660D375147BAD21D2BFAC8290AA5AB369BD0168475B49FB56B462BB8C139571931C92755E1D17FF0FDC16A6D8984C9A666899546237D94ACCC2E13FB974A3239DD7ECC767B9FBA6169FC98EB5C94CB0051D144AFC956AE58D4A3E82F3CFA6E4D41B202EB6333AFA6E8B52E1C76A73CB39F5AAB1CA28E4F276DF414E74128515AB23F11AB452E0C15E16354BA3971F254BB4D83B3C7BFBD8814E31100363927728A3C28AC0477836B8DCB2BA2863C0D5356FA32E6FDE63B90D2358430A8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nb = new NetWorkBusiness(Token, "http://api.nlecloud.com");
        nb.signIn(new SignIn("18316461896", "asdffdsa"), new NCallBack<BaseResponseEntity<User>>(MainActivity.this) {
            @Override
            protected void onResponse(BaseResponseEntity<User> respone) {
                if (respone.getStatus() == 0) {
                    System.out.println(respone.getResultObj().getAccessToken());
                }
            }
        });

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    try {
                        Thread.sleep(1000);
//                        nb.getSensor("228862", "m_fan", new NCallBack<BaseResponseEntity<SensorInfo>>(MainActivity.this) {
//                            @Override
//                            protected void onResponse(BaseResponseEntity<SensorInfo> ss) {
//                                if(ss.getStatus()==0){
////                                    System.out.println("温度"+ss.getResultObj().getValue());
//                                }
//                            }
//                        });
                        try {
                            URL url = new URL("http://api.nlecloud.com/devices/{deviceId}/Sensors/{apiTag}?deviceId=228862&apiTag=m_fan&AccessToken="+Token);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                            InputStream in = conn.getInputStream();
                            int len = 0;
                            byte b[] = new byte[1024];
                            String result = null;
                            while ((len=in.read(b))!=-1){
                                result = new String(b,0,len);
                            }
                            JSONObject js = new JSONObject(result);
                            System.out.println("风扇"+js.getJSONObject("ResultObj").getBoolean("Value"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        nb.control("228862", "m_fan", 0, new NCallBack<BaseResponseEntity>(MainActivity.this) {
                            @Override
                            protected void onResponse(BaseResponseEntity baseResponseEntity) {
//                                System.out.println(baseResponseEntity.getMsg());
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }
}
