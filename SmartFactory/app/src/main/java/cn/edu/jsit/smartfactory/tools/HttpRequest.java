package cn.edu.jsit.smartfactory.tools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {
    public synchronized static void send(final String s) {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(s);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int size = 0;
                    if (size == 0) {
                        size = connection.getInputStream().available();
                    }
                    byte[] msg = new byte[size];
                    connection.getInputStream().read(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
