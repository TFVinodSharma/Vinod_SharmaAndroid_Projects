package com.recorder.screen.screen_recorder.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.services.myappmanager2;

public class TabActivity extends android.app.TabActivity {
    /** Called when the activity is first created. */
    public static boolean running =false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        running =true;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TabHost host = getTabHost();
        Resources res = getResources();
        IntentFilter filter = new IntentFilter(myappmanager2.recentappfired);
        filter.setPriority(900);
        registerReceiver(broadcastReceiverRecent, filter);


        //((LinearLayout.LayoutParams)host.getTabWidget().getChildAt(i).getLayoutParams()).weight = 0;

        //host.getTabWidget().getChildAt(0).getLayoutParams().width=10;

        host.addTab(host.newTabSpec("one").setIndicator(getString(R.string.Download)).setContent(new Intent(this, first.class)));

        host.addTab(host.newTabSpec("two").setIndicator(getString(R.string.SDcard)).setContent(new Intent(this, fourth.class)));

        host.addTab(host.newTabSpec("three").setIndicator(getString(R.string.Running)).setContent(new Intent(this, ListImage.class)));

        host.addTab(host.newTabSpec("four").setIndicator(getString(R.string.All)).setContent(new Intent(this, ListImage.class)));

        host.getTabWidget().getChildAt(0).getLayoutParams().width = 110;
        //host.getTabWidget().getChildAt(1).getLayoutParams().width = 5;
        host.getTabWidget().getChildAt(2).getLayoutParams().width = 45;
        //host.getTabWidget().getChildAt(3).getLayoutParams().width = 5;
        //host.getTabWidget().getChildAt(3).getLayoutParams().width = 30;

    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        running =false;
        try{
            unregisterReceiver(broadcastReceiverRecent);}catch(Exception e){;}
        super.onDestroy();
    }

/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		  Log.v("Debug", "On key pressed");
		   if (keyCode == KeyEvent.KEYCODE_BACK) {
			// super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
			//Log.v("Debug", "back pressed");
			// Intent intentSettings = new Intent();
			// intentSettings.setAction(android.provider.Settings.ACTION_SETTINGS);
			// startActivity(intentSettings);
			// return super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
			Intent intent23 = new Intent(Intent.ACTION_MAIN);
			intent23.addCategory(Intent.CATEGORY_HOME);
			intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
			getApplicationContext().startActivity(intent23);
			finish();
			return true;
		}
		// return super.onKeyDown(keyCode,event);
		return true;
	}*/

/*
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub

		if(myappmanager2.class_name.contains("RecentsActivity"))
		{
			Log.d("tab", "on pause");
			Intent intent23 = new Intent(
				Intent.ACTION_MAIN);
		intent23.addCategory(Intent.CATEGORY_HOME);
		intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);

		getApplicationContext()

				.startActivity(
						intent23);



		finish();
		}

		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		if(myappmanager2.class_name.contains("RecentsActivity"))
		{
			Log.d("tab", "on stop");
		Intent intent23 = new Intent(
				Intent.ACTION_MAIN);
		intent23.addCategory(Intent.CATEGORY_HOME);
		intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);

		getApplicationContext()

				.startActivity(
						intent23);



		finish();
		}

		super.onStop();
	}*/

    private BroadcastReceiver broadcastReceiverRecent = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
			/*  Intent intent23 = new Intent(
						Intent.ACTION_MAIN);
				intent23.addCategory(Intent.CATEGORY_HOME);
				intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);

				getApplicationContext()

						.startActivity(
								intent23);
             TabActivity.this.finish();*/
            Log.d("tab", "receiver fired Tab");
            abortBroadcast();
        }
    };

}
