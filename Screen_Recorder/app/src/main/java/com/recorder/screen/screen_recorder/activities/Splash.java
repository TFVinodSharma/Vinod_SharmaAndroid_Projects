package com.recorder.screen.screen_recorder.activities;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.services.IconData;
import com.recorder.screen.screen_recorder.services.SplashService;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //moveTaskToBack(true);

        try {
            Process sh = Runtime.getRuntime().exec("su", null,null);
        }
        catch (Exception e){
            Log.d("SuperUser Exception","onOffchk"+e.getMessage());
        }

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }


        final Timer mytimer = new Timer();
        mytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Check");
                if(isAccessGranted()){
                    if(!SplashService.running) {
                        startService(new Intent(getApplicationContext(), IconData.class));
                        startService(new Intent(Splash.this, SplashService.class));
                        mytimer.cancel();
                        finish();
                    }
                    else {
                        mytimer.cancel();
                    }
                }
            }
        }, 0, 300);
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
