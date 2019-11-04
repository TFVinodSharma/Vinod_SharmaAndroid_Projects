package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Startvoice1 extends Service 
{
	/*public Startvoice1() 
	{
		super("Startvoice1");
		// TODO Auto-generated constructor stub
	}*/
	private final String myprefs3 = "mySharedPreferences3";
	private boolean msgsent = false;
	public static boolean running = false;
	String name;
	String asw;
	StringBuffer stringBuffer = new StringBuffer().append("A-P");
	byte decode[];
	private static String time_stamp = "";
	MediaRecorder mRecorder=new MediaRecorder();
	static final int ERROR = -1;
	private File mSampleFile = null;
	private File prevFile = null;
	// private File mSampleFileE=null;
	// private File mSampleFile1=null;

	static final String SAMPLE_PREFIX = "C_R";
	static final String SAMPLE_PREFIX1 = "C_R1";
	static final String SAMPLE_EXTENSION = ".mp3";
	static final String SAMPLE_EXTENSION1 = ".mp3";
	boolean mExternalStorageAvailable = false;
	private File sampleDir2 = null;
	boolean mExternalStorageWriteable = false;
	StringBuilder contents = new StringBuilder();
	/*
	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}*/

	static public long getAvailableInternalMemorySize() 
	{
		try
		{
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return availableBlocks * blockSize;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	@Override
	public void onCreate() 
	{
		Log.d(getClass().getSimpleName(),"Voice Record Create");
		super.onCreate();
		running = true;
	}
	/*@Override
	protected void onHandleIntent(Intent intent) 
	{

	}*/
	@Override
	public void onStart(Intent intent, int startId) 
	{
		// mSampleFileE=null;
		running = true;
		Log.d(getClass().getSimpleName(),"Voice call started");
		try
		{
			prevFile = mSampleFile;
			
		}
		catch(Exception e)
		{
			
		}
		try 
		{
			mRecorder.stop();
			//prevFile = mSampleFile;
		} catch (Exception e) {
			Log.d(getClass().getSimpleName(), "exception in startvoice during stop " + e);
		}
		try 
		{
			mRecorder.reset();
		} 
		catch (Exception e) {
			Log.d(getClass().getSimpleName(), "exception during reset  " + e);
		}
		try 
		{
			mRecorder.release();
		}
		catch (Exception e) 
		{
			Log.d(getClass().getSimpleName(), "exception during release  " + e);
		}
		long as = getAvailableInternalMemorySize();
		if (as > 5000)
		{
			mSampleFile = null;
			if (mSampleFile == null) 
			{
				sampleDir2 = getApplicationContext().getFilesDir();
				try 
				{
					if (sampleDir2 != null) 
					{
						mSampleFile = File.createTempFile("TEMP",SAMPLE_EXTENSION, sampleDir2);
					}

				} 
				catch (IOException e) 
				{
					Log.d(getClass().getSimpleName(),"exception thrown during prepare mSampleFile " + e);
				}
			}

			if (mSampleFile != null) 
			{
				try 
				{
					mRecorder = new MediaRecorder();
					/*if(Build.MANUFACTURER.contains("HTC"))
						mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
					else
						mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);*/
					String model = android.os.Build.MODEL;
					System.out.println("Model = " +model);
					String manufacture = android.os.Build.MANUFACTURER;
					System.out.println("Model = " +manufacture);


					if(android.os.Build.MODEL.equals("NEXUS")){
						mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
					}
					else{
					mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setOutputFile(mSampleFile.getAbsolutePath());
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				}

				/*	mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
					mRecorder.setAudioSamplingRate(48000);
					mRecorder.setAudioEncodingBitRate(12200);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setOutputFile(mSampleFile.getAbsolutePath());
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);*/
				} 
				catch (Exception e) 
				{
					Log.d(getClass().getSimpleName(),"Exception:"+e);
				}

				try 
				{
					Log.d(getClass().getSimpleName(),"*******sample file path:"+mSampleFile.getAbsolutePath());

				}
				catch (IllegalStateException e2) 
				{
					Log.d(getClass().getSimpleName(),"ADVANCEDSNF IllegalStateException in startvoice "+ e2);
				}
				mRecorder.setMaxDuration(125000);
				try
				{
					mRecorder.prepare();
				} 
				catch (IllegalStateException e1) 
				{
				
					Log.d(getClass().getSimpleName(),"ADVANCEDSNF exception thrown during prepare " + e1);
				}
				catch (IOException e1) 
				{

					Log.d(getClass().getSimpleName(),"ADVANCEDSNF exception thrown during prepare " + e1);
				}
				//mRecorder.start();
				try
				{
					mRecorder.start();
					Log.d("Startvoice1","Call recording started");
					//	mRecorder.start();
				}
				catch(Exception e)
				{
					Log.d("Startvoice1","Exception" +e);
				}
				new Thread(new Runnable() {
					public void run() {
						try {
							Thread.sleep(500);
							}
						catch (Exception e) {
							Log.d(getClass().getSimpleName(), "thread interrupted " + e);
						}

						if (prevFile != null) {
							prevFile.renameTo(new File(sampleDir2, SAMPLE_PREFIX + time_stamp + SAMPLE_EXTENSION));
						}
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
						time_stamp = "_" + strhr + strmin + "_"
								+ Integer.toString(dt.getMonth() + 1) + "_"
								+ Integer.toString(dt.getDate()) + "_"
								+ Integer.toString(dt.getYear() + 1900);

					}

				}).start();

			}//end of if(mSimpleFile != null)
			Log.d("startvoice1","FileName = "+mSampleFile+"FileDirc = "+sampleDir2+"FilePrfx = "+SAMPLE_PREFIX+"FileTime = "+time_stamp+"FileExtn = "+SAMPLE_EXTENSION);
		}

	}

	@Override
	public void onDestroy() 
	{

		running = false;
		try 
		{

			if (mRecorder != null)
				mRecorder.stop();

		}
		catch(Exception e)
		{
			System.out.println("Exception in Call Record Stop");
		}
		try 
		{
			if (mRecorder != null)
				mRecorder.reset();
		}
		catch(Exception e)
		{

		}
		try 
		{
			if (mRecorder != null)
				mRecorder.release();
		}
		catch(Exception e)
		{
			System.out.println("Exception in call Record Release = " +e);
		}
		try
		{
			Log.d("startvoice1","FileName = "+mSampleFile+" FileDir = "+sampleDir2+"FilePrfx = "+SAMPLE_PREFIX+"FileTime = "+time_stamp+"FileExtn = "+SAMPLE_EXTENSION);
			mSampleFile.renameTo(new File(sampleDir2, SAMPLE_PREFIX + time_stamp+ SAMPLE_EXTENSION));
		}
		catch(Exception e)
		{
			System.out.println("Exception in create File = " +e);
		}
		super.onDestroy();

	}

	@Override
	public IBinder onBind(Intent intent) 
	{
		return null;
	}

}