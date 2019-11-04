package vinod.learning.com.orientationchange;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String STATE_COUNTER = "counter";
    private int mCounter;
    TextView mCounterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCounter = savedInstanceState.getInt(STATE_COUNTER, 0);
        }

        // Display the value of the counter
        mCounterTextView = (TextView) findViewById(R.id.textview);
        mCounterTextView.setText(Integer.toString(mCounter));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Make sure to call the super method so that the states of our views are saved
        super.onSaveInstanceState(outState);
        // Save our own state now
        outState.putInt(STATE_COUNTER, mCounter);
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


/*
                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                //editor.putString("name", "Elena");
                editor.putInt("ORIENTATION_LANDSCAPE", 12);
                editor.apply();


                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                // String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                int count = prefs.getInt("ORIENTATION_LANDSCAPE", 1);
                count++;

*/
//to do something
                break;

            case Configuration.ORIENTATION_PORTRAIT:

                /*SharedPreferences.Editor editor1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                //editor.putString("name", "Elena");
                editor1.putInt("ORIENTATION_PORTRAIT", 12);
                editor1.apply();


                SharedPreferences prefs1 = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                // String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                int count1 = prefs1.getInt("ORIENTATION_PORTRAIT", 1);
                count1++;*/

//to do something
                break;
        }


    }

}
