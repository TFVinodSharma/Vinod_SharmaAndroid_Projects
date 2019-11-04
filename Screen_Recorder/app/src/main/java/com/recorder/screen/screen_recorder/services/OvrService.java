package com.recorder.screen.screen_recorder.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.recorder.screen.screen_recorder.R;

public class OvrService extends Service {

    private WindowManager windowManager;
    private RelativeLayout chatheadView;

    public OvrService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                //WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 100;
        chatheadView = (RelativeLayout) inflater.inflate(R.layout.ss_notification_overlay, null);
        windowManager.addView(chatheadView, params);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
//                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//                WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.WRAP_CONTENT,
//                        WindowManager.LayoutParams.TYPE_PHONE,
//                        //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                        PixelFormat.TRANSLUCENT);
//
//                params.gravity = Gravity.CENTER;
//                params.x = 0;
//                params.y = 100;
//                chatheadView = (RelativeLayout) inflater.inflate(R.layout.ss_notification_overlay, null);
//                windowManager.addView(chatheadView, params);
//            }
//        }, 0);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(chatheadView);
    }
}
