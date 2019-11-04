package com.recorder.screen.screen_recorder.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Android on 7/29/2017.
 */

public class Values {

    public static final String SR_TAG = "SR_TAG";

    public static void setSharedPrefrence(String key, String value, Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SR_TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getSharedPrefrence(String key, Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences(SR_TAG, Context.MODE_PRIVATE);
        String value =  prefs.getString(key, "NA");
        return value;
    }

    public static String Date(Long millis) {
        Date date = new Date(millis);
        return date.toString() + "  :  " + TimeZone.getDefault().getDisplayName() + "  :  " + TimeZone.getDefault().getID();
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
