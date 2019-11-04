package com.recorder.screen.screen_recorder.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;
import android.widget.Toast;

import com.recorder.screen.screen_recorder.R;
import com.recorder.screen.screen_recorder.helper.Values;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import in.omerjerk.libscreenshotter.ScreenshotCallback;
import in.omerjerk.libscreenshotter.Screenshotter;

public class VideoScreenshot extends Activity {

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    public static Activity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_screenshot);
        activity = this;

        //super.moveTaskToBack(true);
        try {
            takeScreenshot();
        }
        catch (Exception e) {}

    }

    public void takeScreenshot() {

        //startService(new Intent(getApplicationContext(), OvrService.class));

        try {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                    getSystemService(Context.MEDIA_PROJECTION_SERVICE);

            startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
        catch (Exception e) {}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Screenshotter.getInstance()
                        .setSize(720, 1280)
                        .takeScreenshot(this, resultCode, data, new ScreenshotCallback() {
                            @Override
                            public void onScreenshot(Bitmap bitmap) {

                                OutputStream outStream = null;

                                File file = new File(Environment.getExternalStorageDirectory().toString(), "ss.png");
                                if (file.exists()) {
                                    file.delete();
                                }
                                try {
                                    bitmap = Values.getResizedBitmap(bitmap, 400);
                                    outStream = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                    outStream.flush();
                                    outStream.close();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("---------------------Screenshot Captured---------------------");
                                VideoScreenshot.activity.finish();
                            }
                        });
            }
            catch (Exception e) {}
        } else {
            Toast.makeText(this, "You denied the permission.", Toast.LENGTH_SHORT).show();
        }
    }

}