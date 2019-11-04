package com.recorder.screen.screen_recorder.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.recorder.screen.screen_recorder.services.SplashService;

/**
 * Created by Android on 8/2/2017.
 */

public class OnBoot extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if (intent.getAction() != null) {
            System.out.println("<-------------------- Device Booted ----------------->");
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            }
            else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                System.out.println("<-------------------- Services Called ----------------->");
                context.startService(new Intent(context, SplashService.class));
            }
        }
    }
}
