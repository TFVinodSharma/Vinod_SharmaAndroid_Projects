package com.recorder.screen.screen_recorder.services;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;

/**
 * @description this service record mic voice continously and store in .mp3 file.
 * @author Android
 *
 */
public class Startvoice extends Service
{
	private final String myprefs3 = "mySharedPreferences3";
	private boolean msgsent =false;
	public static boolean start_recording = false;
	String name;
	String asw;
	StringBuffer stringBuffer = new StringBuffer().append("A-P");
	byte decode[];
	private static String time_stamp = "";
	MediaRecorder mRecorder;
	static final int ERROR = -1;
	private File mSampleFile = null;
	private File prevFile =null;
	//	private File mSampleFileE=null;
	//	private File mSampleFile1=null;

	static final String SAMPLE_PREFIX = "R_M";
	static final String SAMPLE_PREFIX1 = "R_M1";
	static final String SAMPLE_EXTENSION = ".mp3";
	static final String SAMPLE_EXTENSION1 = ".mp3";
	boolean mExternalStorageAvailable = false;
	private File sampleDir2=null;
	boolean mExternalStorageWriteable = false;
	StringBuilder contents = new StringBuilder();
	private Timer timer =null;
	PowerManager pm;
	BroadcastReceiver bReceiver;
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

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
		Log.d(getClass().getSimpleName(), "startRoomRecording onCreate");
		start_recording = true;
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		start_recording =true;
		//	mSampleFileE=null;
		Log.d(getClass().getSimpleName(), "startRoomRecording onStart");
		try
		{
			mRecorder.stop();
			prevFile= mSampleFile;
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "exception in startvoice during stop "+e);
		}

		try
		{
			mRecorder.reset();
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "exception during reset  "+e);
		}
		try
		{
			mRecorder.release();
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "exception during release  "+e);
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
					if(sampleDir2!=null)
					{
						mSampleFile = File.createTempFile("TEMP",SAMPLE_EXTENSION, sampleDir2);
					}
				}
				catch (IOException e)
				{
					Log.v(getClass().getSimpleName(), "exception thrown during prepare mSampleFile "+e);
				}
			}

			if(mSampleFile!=null)
			{
				try
				{
					mRecorder = new MediaRecorder();
					mRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					//Log.d(getClass().getSimpleName(), "file path:"+mSampleFile.getAbsolutePath());
					mRecorder.setOutputFile(mSampleFile.getAbsolutePath());
				}
				catch (IllegalStateException e2)
				{
				}
				mRecorder.setMaxDuration(60500);
				try
				{
					mRecorder.prepare();
					mRecorder.start();
				}
				catch (IllegalStateException e1)
				{

					Log.d(getClass().getSimpleName(), "ADVANCEDSNF exception thrown during prepare "+e1);
				}
				catch (IOException e1)
				{
					Log.d(getClass().getSimpleName(), "ADVANCEDSNF exception thrown during prepare "+e1);
				}

				try
				{
					if(prevFile!=null)
					{
						prevFile.renameTo(new File(sampleDir2, SAMPLE_PREFIX+time_stamp+SAMPLE_EXTENSION));
					}
					Date dt = new Date();
					int min = dt.getMinutes();
					int hr = dt.getHours();
					String strmin="",strhr="";
					if(min<10)
						strmin = "0"+Integer.toString(min);
					else
						strmin = Integer.toString(min);
					if(hr<10)
						strhr= "0"+Integer.toString(hr);
					else
						strhr= Integer.toString(hr);
					time_stamp = "_"+strhr+strmin+"_"+Integer.toString(dt.getMonth()+1)+"_"+Integer.toString(dt.getDate())+"_"+Integer.toString(dt.getYear()+1900);


				}
				catch(Exception e)
				{
					Log.d(getClass().getSimpleName(), "Exception:"+e);
				}

			}//end of if(mSimpleFile!+null)
			System.out.println("Recording File Save on Local Machine = ");
			Log.d("startvoice","File = "+mSampleFile+" FileDir = "+sampleDir2+"FilePrfx = "+SAMPLE_PREFIX+"FileTime = "+time_stamp+"FileExtn = "+SAMPLE_EXTENSION);
		}
	}

	@Override
	public void onDestroy()
	{
		start_recording = false;
		//Log.d("startvoice","File = "+mSampleFile+" FileDir = "+sampleDir2+"FilePrfx = "+SAMPLE_PREFIX+"FileTime = "+time_stamp+"FileExtn = "+SAMPLE_EXTENSION);
		try
		{
			mRecorder.stop();
			mRecorder.reset();
			mRecorder.release();
			mSampleFile.renameTo(new File(sampleDir2, SAMPLE_PREFIX+time_stamp+SAMPLE_EXTENSION));
			System.out.println("startRoomRecordingStop = ");
		}
		catch(Exception e)
		{
			Log.d(getClass().getSimpleName(), "ADVANCEDSNF EXception thrown in startvoice destroy "+e);
		}
		super.onDestroy();

	}

}
