package shifoo.com.app.base.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import shifoo.com.app.apppreferences.AppPreferences;


public class BaseActivity extends AppCompatActivity {

    private MaterialDialog materialDialog;
    public AppPreferences mAppPrefrences;
    public ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //localizationDelegate.addOnLocaleChangedListener(this);
        // localizationDelegate.onCreate(savedInstanceState);
        final AppPreferences mAppPrefrences = AppPreferences.INSTANCE;
         mAppPrefrences.initAppPreferences(getApplicationContext());


    }

    public void showHideProgressDialog(boolean isShow) {

        if (isShow) {
            dialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
            dialog.setMessage("Processing");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void showErrorMessage(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }


    public void showProgressDialogue(String title, String message) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .progress(true, 0).show();
    }

    public void showProgressDialogue() {
        materialDialog = new MaterialDialog.Builder(this)
                .progress(true, 0).show();
    }

    public void hideProgressDialgogue() {
        materialDialog.hide();
    }

}
