package nledu.com.hardwarebyjar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.transfer.DataBusFactory;


public class ActivityRFID extends AppCompatActivity {
    private EditText etEpc;
    private TextView tvReadData;
    private TextView tvSingleEpc;

    private RFID rfid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfid);
        etEpc = findViewById(R.id.etEpc);
        tvSingleEpc = findViewById(R.id.tvSingleEpc);
        tvReadData = findViewById(R.id.tvReadData);
        if ("socket".equals(Util.MODE)) {
            rfid = new RFID(DataBusFactory.newSocketDataBus("192.168.0.200", 951));
        } else {
            rfid = new RFID(DataBusFactory.newSerialDataBus(2, 115200));
        }
    }


    public void readSingleEpc(View view) {
        tvSingleEpc.setText("");
        try {
            rfid.readSingleEpc(val -> tvSingleEpc.setText(val));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeData(View view) {
        String data = etEpc.getText().toString();
        try {
            rfid.writeData(data, isSuccess -> Toast.makeText(getApplicationContext(), isSuccess + "", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData(View view) {
        tvReadData.setText("");
        try {
            rfid.readData(str -> tvReadData.setText(str));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (rfid != null) {
            rfid.stopConnect();
        }
    }


}
