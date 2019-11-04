package vinod.learning.com.orientationchange;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.File;
public class ChangeOrientation extends Service {
    private File storeorientation;
    private SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //onConfigurationChanged(1,newC);

        // onConfigurationChanged();
        return null;
    }


    public void onConfigurationChanged(Configuration newConfig) {
        int orientation = newConfig.orientation;

        switch (orientation) {

            case Configuration.ORIENTATION_LANDSCAPE:
                /*preferences = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("value", 1);
                preferences.getInt("value", 1);
*/


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                //editor.putString("name", "Elena");
                editor.putInt("ORIENTATION_LANDSCAPE", 12);
                editor.apply();


                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
               // String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                int count = prefs.getInt("ORIENTATION_LANDSCAPE", 1);
                count++;

//to do something
                break;

            case Configuration.ORIENTATION_PORTRAIT:

                SharedPreferences.Editor editor1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                //editor.putString("name", "Elena");
                editor1.putInt("ORIENTATION_PORTRAIT", 12);
                editor1.apply();


                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                // String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                int count1 = prefs1.getInt("ORIENTATION_PORTRAIT", 1);
                count1++;

//to do something
                break;
        }


    }
}