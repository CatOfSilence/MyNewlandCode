package cn.edu.jsit.smartfactory.tools;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WebServiceHelper {

    public static void SaveInfo(final String msg) {
        new Thread() {
            public void run() {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject rpc = new SoapObject("http://tempuri.org/", "SaveInfo");
                rpc.addProperty("message", msg);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transportSE = new HttpTransportSE("http://10.34.1.167:62518/WarningMessage.asmx?WSDL");
                try {
                    transportSE.call("http://tempuri.org/SaveInfo", envelope);
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String result = soapObject.getProperty(0).toString();
                    Log.i("result", result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }


    public interface Callback {
        void call(String s);
    }

    public static void GetInfo(final Callback callback) {
        new Thread() {
            public void run() {
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject rpc = new SoapObject("http://tempuri.org/", "GetInfo");
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transportSE = new HttpTransportSE("http://10.34.1.167:62518/WarningMessage.asmx?WSDL");
                try {
                    transportSE.call("http://tempuri.org/GetInfo", envelope);
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String result = soapObject.getProperty(0).toString();
                    callback.call(result);
                    Log.i("result", result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static void GetLogin(final Callback callback,final String user, final String psw){
        new Thread(){
            public void run(){
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject rpc = new SoapObject("http://tempuri.org/", "Login");
                rpc.addProperty("name",user);
                rpc.addProperty("password",psw);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transportSE = new HttpTransportSE("http://10.34.1.167:62518/User.asmx?WSDL");
                try {
                    transportSE.call("http://tempuri.org/Login", envelope);
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String result = soapObject.getProperty(0).toString();
                    callback.call(result);
                    Log.i("result", result);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static void SetReg(final  Callback callback,final String user, final String psw, final String sex,
                              final  String phone,final  String email){
        new Thread(){
            public void run(){
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject rpc = new SoapObject("http://tempuri.org/", "Register");
                rpc.addProperty("name",user);
                rpc.addProperty("password",psw);
                rpc.addProperty("sex",sex);
                rpc.addProperty("phone",phone);
                rpc.addProperty("email",email);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transportSE = new HttpTransportSE("http://10.34.1.167:62518/User.asmx?WSDL");
                try {
                    transportSE.call("http://tempuri.org/Register", envelope);
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String result = soapObject.getProperty(0).toString();
                    callback.call(result);
                    Log.i("result", result);
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    public static void GetById(final Callback callback, final String id){
        new Thread(){
            public void run(){
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                SoapObject rpc = new SoapObject("http://tempuri.org/", "GetAll");
                rpc.addProperty("name",id);
                envelope.bodyOut = rpc;
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE transportSE = new HttpTransportSE("http://10.34.1.167:62518/User.asmx?WSDL");
                try {
                    transportSE.call("http://tempuri.org/GetAll", envelope);
                    SoapObject soapObject = (SoapObject) envelope.bodyIn;
                    String result = soapObject.getProperty(0).toString();
                    callback.call(result);
                    Log.i("result", result);
                }catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
