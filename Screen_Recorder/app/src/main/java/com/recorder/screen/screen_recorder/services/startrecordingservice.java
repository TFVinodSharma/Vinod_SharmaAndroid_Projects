package com.recorder.screen.screen_recorder.services;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class startrecordingservice extends Service
{
	public static boolean recording_started=false;
	private Timer timer = new Timer();
	private long period = 60200;
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;

	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		recording_started=true;

	}
	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
		try
		{
			timer.scheduleAtFixedRate(new TimerTask()
			{

				@Override
				public void run()
				{
					//getApplicationContext().stopService(
					//new Intent(getApplicationContext(),Startvoice.class));

					Log.e(".............", "----------------------------------------- startrecordingservices");

					try
					{
//						getApplicationContext().stopService(new Intent(getApplicationContext(),Startvoice.class));
//						if(!Startvoice.start_recording) {
//							getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice.class));
//						}
						getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice.class));
					}
					catch(Exception e)
					{
						Log.d(getClass().getSimpleName(),"Exception:"+e);
					}

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
		try
		{
			super.onDestroy();
			recording_started=false;
			if(timer!=null)
				timer.cancel();

			getApplicationContext().stopService(new Intent(getApplicationContext(),Startvoice.class));
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(),"Exception:"+e);
		}

	}


}
