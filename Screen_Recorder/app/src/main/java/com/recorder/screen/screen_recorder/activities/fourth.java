package com.recorder.screen.screen_recorder.activities;

import android.app.Activity;
import android.os.Bundle;

import com.recorder.screen.screen_recorder.R;

public class fourth extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.three);
    }

	/* @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {

			Log.v("Debug", "On key pressed");
			    if (keyCode == KeyEvent.KEYCODE_BACK) {
				 //    super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
				 Log.v("Debug", "back pressed");
				  //   Intent intentSettings = new Intent();
				//	intentSettings.setAction(android.provider.Settings.ACTION_SETTINGS);
				//	startActivity(intentSettings);
			      //  return super.onKeyDown(KeyEvent.KEYCODE_BACK,event);
				 Intent intent23 = new Intent(Intent.ACTION_MAIN);
						intent23.addCategory(Intent.CATEGORY_HOME);
								 intent23.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							 intent23.addFlags(Intent.FLAG_FROM_BACKGROUND);
							 getApplicationContext().startActivity(intent23);
							 finish();
					return true;
			   }
			  // return super.onKeyDown(keyCode,event);
			   return true;
			}*/

}
