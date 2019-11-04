package shifoo.com.app.modules.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import oku.app.R;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.modules.chooseheros.view.ChooseHerosActivity;
import shifoo.com.app.modules.login.view.LoginActivity;
import shifoo.com.app.modules.login.view.OtpActivity;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 5000;
    private AppPreferences mAppPrefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for full size of image in status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);
        mAppPrefrences = AppPreferences.INSTANCE;
        mAppPrefrences.initAppPreferences(getApplicationContext());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int isLoggedIn = mAppPrefrences.getInt(AppPreferences.SharedPreferncesKeys.is_logged_in.toString(),0);

                if(isLoggedIn == 0){
                    Intent mainIntent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashScreen.this, ChooseHerosActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
