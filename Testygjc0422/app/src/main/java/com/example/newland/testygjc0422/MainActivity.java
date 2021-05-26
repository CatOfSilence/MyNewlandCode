package com.example.newland.testygjc0422;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nle.mylibrary.forUse.mdbus4150.Modbus4150;
import com.nle.mylibrary.forUse.rfid.RFID;
import com.nle.mylibrary.forUse.rfid.SingleEpcListener;
import com.nle.mylibrary.transfer.DataBusFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_card,tv_name,tv_money;

    RFID rfid;
    String str = "0";
    String card1 = "E2 00 00 19 51 01 00 86 23 20 2A 03";
    String card2 = "E2 00 00 19 51 01 01 12 23 20 2A 34";
    String card3 = "E2 00 00 19 51 01 01 01 23 20 2A 1B";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    TextToSpeech tts;
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_card = findViewById(R.id.tv_card);
        tv_name = findViewById(R.id.tv_name);
        tv_money = findViewById(R.id.tv_money);
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.22.16",2003),null);

        tts = new TextToSpeech(MainActivity.this,null);
        sp = getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sp.edit();
        init();

        String type = sp.getString("type","0");
        type_(type);

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                type_(str);
            }
        };
        Handler ttshan = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

            }
        };
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);

                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                if(s.equals(card1)){
                                    if(str.equals("2")||str.equals("3")||str.equals("0")){
                                        str = "1";
                                        handler.sendEmptyMessage(0);
                                    }
                                }else if(s.equals(card2)){
                                    if(str.equals("1")||str.equals("3")||str.equals("0")){
                                        str = "2";
                                        handler.sendEmptyMessage(0);
                                    }
                                }else if(s.equals(card3)){
                                    if(str.equals("1")||str.equals("2")||str.equals("0")){
                                        str = "3";
                                        handler.sendEmptyMessage(0);
                                    }
                                }
                            }

                            @Override
                            public void onFail(Exception e) {

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(rfid!=null)
            rfid.stopConnect();
    }

    public void init(){
        editor.putString("card1",card1);
        editor.putString("card2",card2);
        editor.putString("card3",card3);
        editor.putString("name1","客人A");
        editor.putString("name2","客人B");
        editor.putString("name3","客人C");
        editor.putString("money1","24");
        editor.putString("money2","30");
        editor.putString("money3","27");
        editor.commit();
    }

    public void type_(String type){
        if(type.equals("1")){
            tv_card.setText(sp.getString("card1",""));
            tv_name.setText(sp.getString("name1",""));
            tv_money.setText(sp.getString("money1",""));
            tts.speak("客人A的消费额为：24元", TextToSpeech.QUEUE_FLUSH,null);
        }else if(type.equals("2")){
            tv_card.setText(sp.getString("card2",""));
            tv_name.setText(sp.getString("name2",""));
            tv_money.setText(sp.getString("money2",""));
            tts.speak("客人B的消费额为：30元", TextToSpeech.QUEUE_FLUSH,null);
        }else if(type.equals("3")){
            tv_card.setText(sp.getString("card3",""));
            tv_name.setText(sp.getString("name3",""));
            tv_money.setText(sp.getString("money3",""));
            tts.speak("客人C的消费额为：27元", TextToSpeech.QUEUE_FLUSH,null);
        }
        editor.putString("type",type);
        editor.commit();
    }

}
