package nledu.com.hardwarebyjar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.zigbee.FourChannelValConvert;
import com.nle.mylibrary.forUse.zigbee.Zigbee;
import com.nle.mylibrary.transfer.DataBusFactory;


public class ActivityZigbee extends AppCompatActivity {
    private TextView tv4Tmp;
    private TextView tv4Hum;
    private TextView tv4Noise;
    private TextView tv4CO2;
    private TextView tvTmp;
    private TextView tvHum;
    private TextView tvFire;
    private TextView tvPerson;
    private TextView tvLight;
    private Zigbee zigbee;
    private EditText etSingleSerial;
    private EditText etDoubleSerial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zigbee);
        tv4Tmp = findViewById(R.id.tv4Tmp);
        tv4Hum = findViewById(R.id.tv4Hum);
        tv4Noise = findViewById(R.id.tv4Noise);
        tv4CO2 = findViewById(R.id.tv4CO2);
        tvTmp = findViewById(R.id.tvTmp);
        tvHum = findViewById(R.id.tvHum);
        tvFire = findViewById(R.id.tvFire);
        tvPerson = findViewById(R.id.tvPerson);
        tvLight = findViewById(R.id.tvLight);
        etSingleSerial = findViewById(R.id.etSingleSerial);
        etDoubleSerial = findViewById(R.id.etDoubleSerial);
        if ("socket".equals(Util.MODE)) {
            zigbee = new Zigbee(DataBusFactory.newSocketDataBus("172.19.20.16", 951));
        } else {
            zigbee = new Zigbee(DataBusFactory.newSerialDataBus(2, 38400));
        }
    }

    @SuppressLint("SetTextI18n")
    public void getVal(View view) {
        clearTextView();
        try {
            double[] vals = zigbee.getFourEnter();
            if (vals != null) {
                tv4Tmp.setText(FourChannelValConvert.getTemperature(vals[1]) + "");//请根据实际通道号转换值
                System.out.println(FourChannelValConvert.getTemperature(vals[1]) + "");
                tv4Hum.setText(FourChannelValConvert.getHumidity(vals[3]) + "");//请根据实际通道号转换值
                System.out.println(FourChannelValConvert.getHumidity(vals[3]) + "");
                tv4Noise.setText(FourChannelValConvert.getNoice(vals[2]) + "");//请根据实际通道号转换值
                System.out.println(FourChannelValConvert.getNoice(vals[2]) + "");
                tv4CO2.setText(FourChannelValConvert.getCO2(vals[0]) + "");//请根据实际通道号转换值
                System.out.println(FourChannelValConvert.getCO2(vals[0]) + "");
            }
            if (zigbee.getTmpHum() != null) {
                tvTmp.setText(zigbee.getTmpHum()[0] + "");
                tvHum.setText(zigbee.getTmpHum()[1] + "");
            }

            tvFire.setText(zigbee.getFire() + "");
            tvPerson.setText(zigbee.getPerson() + "");

            tvLight.setText(zigbee.getLight() + "");
            System.out.println(zigbee.getLight() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearTextView() {
        tv4Tmp.setText("");
        tv4Hum.setText("");
        tv4Noise.setText("");
        tv4CO2.setText("");
        tvTmp.setText("");
        tvHum.setText("");
        tvFire.setText("");
        tvPerson.setText("");
        tvLight.setText("");
    }


    public void closeRelay(View view) {
        try {
            int num = Integer.parseInt(etDoubleSerial.getText().toString());
            zigbee.ctrlDoubleRelay(num, 1, false, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRelay(View view) {
        try {
            int num = Integer.parseInt(etDoubleSerial.getText().toString());
            zigbee.ctrlDoubleRelay(num, 1, true, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeRelay2(View view) {
        try {
            int num = Integer.parseInt(etDoubleSerial.getText().toString());
            zigbee.ctrlDoubleRelay(num, 2, false, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRelay2(View view) {
        try {
            int num = Integer.parseInt(etDoubleSerial.getText().toString());
            zigbee.ctrlDoubleRelay(num, 2, true, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (zigbee != null) {
            zigbee.stopConnect();
        }
    }

    public void openSingle(View view) {
        try {
            int num = Integer.parseInt(etSingleSerial.getText().toString());
            zigbee.ctrlSingleRelay(num, true, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSingle(View view) {
        try {
            int num = Integer.parseInt(etSingleSerial.getText().toString());
            zigbee.ctrlSingleRelay(num, false, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
