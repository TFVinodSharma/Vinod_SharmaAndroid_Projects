package com.recorder.screen.screen_recorder.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Stack;

public class FacebookDemo extends Activity {

    Stack s ;
    WebView w1 = null;
    ByteArrayOutputStream mByteArrayOpStream = null;
    Bitmap bmpBuffer  = null;
    public static  String URL_ID1= "http://www.facebook.com";
    //public static  String URL_ID= " http://www.mobimonster2.com";
    /** Called when the activity is first created. */
    //private ActivityManager activityManager;

    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT < 18) {
            w1.clearView();
        } else {
            w1.loadUrl("about:blank");
        }
        w1.goBack();
    }




    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v("File", "in oncreate Facebook");

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        s= new Stack<String>();

        w1 = new WebView(this);
        setContentView( w1 );

        w1.loadUrl(URL_ID1);
	    /*  setContentView(R.layout.main);
	      w1 = (WebView) findViewById(R.id.webView1);

	     w1.loadUrl(URL_ID1);
		*/

        w1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);

                switch(action) {

                    case (MotionEvent.ACTION_DOWN) :
                        takepicture();
                        Log.d("File","on Touch ");
                        return false;

                    case (MotionEvent.ACTION_UP) :
                        takepicture();
                        return false;
                };


                return false;
            }
        });
        ///////////


        w1.setWebViewClient(new WebViewClient()
        {
            @SuppressWarnings("deprecation")
            public void onPageFinished(WebView view, String url)
            {

                //  WebView myWebView = (WebView) findViewById(R.id.webview);


                //Log.v("File","in email activity on page finished");

                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 416);

                WebSettings webSettings = w1.getSettings();
                webSettings.setSavePassword(false);
                webSettings.setSaveFormData(false);
                webSettings.setJavaScriptEnabled(true);

                // webSettings.setCacheMode(1);
                webSettings.setCacheMode(webSettings.LOAD_DEFAULT);

                //////////////////////////////////////////////////////////////////////////////////////
                webSettings.setAllowFileAccess(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
                //  webSettings.setNavDump(true);
                webSettings.setLightTouchEnabled(true);
                webSettings.setSupportMultipleWindows(true);
                webSettings.setLoadsImagesAutomatically(true);

                ///////////////////////////////////////////////////////
                //WebSettings webSettings = w.getSettings();
                webSettings.setJavaScriptEnabled(true);

                //w1.setWebViewClient(new WebViewClient());
                takepicture();


            }

            public boolean shouldOverrideUrlLoading(WebView view, String url){
                w1.loadUrl(url);
                //Log.v("File","shouldOverrideUrlLoading "+url);
                return super.shouldOverrideUrlLoading(view, url);
                // Here the String url hold 'Clicked URL'
                //return false;
            }
        });

        //  setContentView( w );
        //
        //  w.loadUrl( "http://www.yahoo.com");

    }


    public void takepicture()
    {
        //Log.d("File", "in takepicture()");
        String newFolder = "/FTest";


        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

        File myNewFolder = new File(extStorageDirectory + newFolder);

        if(!myNewFolder.exists()){
            myNewFolder.mkdir();
        }

        Picture picture = w1.capturePicture();

        int width =   picture.getWidth() ;
        int height  = picture.getHeight();

        Bitmap  b =null;
        try{
            b = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_4444);
            // Bitmap b = createBlackAndWhite(b);
            Canvas c = new Canvas( b );

            picture.draw( c );
            System.gc();

            FileOutputStream fos = null;
            fos = new FileOutputStream( "/sdcard/FTest/" +"facebook"+
                    System.currentTimeMillis() + ".jpeg" );
            if ( fos != null )
            {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos );

                fos.close();
                fos.flush();
                //b.recycle();
                System.gc();
            }

        } catch( Exception e )
        {
            // b.recycle();
            System.gc();
            Log.d("File","exception thrown in during capturing images "+e);
        }
        catch(OutOfMemoryError e)
        {
            Log.d("File", "out of memory "+e);
            try
            {
                // b.recycle();
                System.gc();

            }catch(Exception e1)
            {
                ;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT < 18) {
            w1.clearView();
            w1.destroy();
        }
        else {
            w1.loadUrl("about:blank");
            w1.clearCache(true);
            w1.clearHistory();
            w1.destroy();
        }

    }

}
