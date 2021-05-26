package nledu.com.hardwarebyjar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.nle.mylibrary.enums.led.PlayType;
import com.nle.mylibrary.enums.led.ShowSpeed;
import com.nle.mylibrary.forUse.led.LedScreen;
import com.nle.mylibrary.transfer.DataBusFactory;


public class ActivityLed extends AppCompatActivity {
    private EditText etTxt;
    private LedScreen ledScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        etTxt = findViewById(R.id.etTxt);
        if ("socket".equals(Util.MODE)) {
            ledScreen = new LedScreen(DataBusFactory.newSocketDataBus("192.168.0.200", 953));
        } else {
            ledScreen = new LedScreen(DataBusFactory.newSerialDataBus(2, 9600));
        }
    }

    public void send(View view) {
        String txt = etTxt.getText().toString();
        try {
            ledScreen.sendTxt(txt, PlayType.LEFT, ShowSpeed.SPEED3, 3, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeLed(View view) {
        try {
            ledScreen.switchLed(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openLed(View view) {
        try {
            ledScreen.switchLed(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ledScreen != null) {
            ledScreen.stopConnect();
        }
    }
}
