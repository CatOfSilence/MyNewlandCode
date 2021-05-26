package nledu.com.hardwarebyjar;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.transfer.DataBusFactory;


public class Activity4150 extends AppCompatActivity {
    private Modbus4150 modbus4150;
    Handler handler = new Handler();

    private TextView tvVal;
    private EditText etSwitchPort;
    private EditText etDataPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4150);
        tvVal = findViewById(R.id.tvVal);
        etDataPort = findViewById(R.id.etDataPort);
        etSwitchPort = findViewById(R.id.etSwitchPort);
        if ("socket".equals(Util.MODE)) {
            modbus4150 = new Modbus4150(DataBusFactory.newSocketDataBus("192.168.1.200", 950));
        } else {
            modbus4150 = new Modbus4150(DataBusFactory.newSerialDataBus(2, 9600));
        }

    }

    public void open(View view) {
        try {
            int portNum = Integer.parseInt(etSwitchPort.getText().toString());
            modbus4150.openRelay(portNum, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(View view) {
        try {
            int portNum = Integer.parseInt(etSwitchPort.getText().toString());
            modbus4150.closeRelay(portNum, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("SetTextI18n")
    public void getVal(View view) {
        try {
            int dataPort = Integer.parseInt(etDataPort.getText().toString());
            modbus4150.getVal(dataPort, val -> tvVal.setText(val + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (modbus4150 != null) {
            modbus4150.stopConnect();
        }
    }


}
