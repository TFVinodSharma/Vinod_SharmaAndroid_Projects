package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class startcallrecording extends Service
{
	public static boolean call_recording_started=false;
	private Timer timer = new Timer(); 
	private long period = 60000;
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}
	@Override
	public void onCreate() 
	{
		super.onCreate();
		call_recording_started=true;
	}
	@Override
	public void onStart(Intent intent, int startId) 
	{
		try
		{
			super.onStart(intent, startId);
			timer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Log.e(".............", "----------------------------------------- startcallrecording");
					getApplicationContext().startService(new Intent(getApplicationContext(),Startvoice1.class));

				}
			},0, period);
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(),"Exception:"+e);
		}
	}

	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		call_recording_started=false;
		try {
			if (timer != null)
				timer.cancel();
		} catch (Exception e) {}


	}

}
