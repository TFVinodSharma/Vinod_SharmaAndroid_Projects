/*
 * Copyright (C) 2017 Orange Tree technology Private Limited

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Orange Tree Technology private Limited
 * @developer  Rajiv Ranjan Singh
 *  Email : rajiv.ar73@gmail.com
 *
 * used to keep all utility methods which we used in the app
 */

package shifoo.com.app.library;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;




import oku.app.R;

import java.io.IOException;

import static android.Manifest.permission.READ_CONTACTS;

public class LibFunction {


    private static final int REQUEST_READ_CONTACTS = 0;


    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public static int getBuildVersion() {
        return Build.VERSION.SDK_INT;
    }


    public void hideSoftKeyboard(Activity context) {
        if (context.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        }
    }


    public static String capitalize(String text) {

        StringBuilder str = new StringBuilder();

        String[] tokens = text.split("\\s");// Can be space,comma or hyphen

        for (String token : tokens) {
            str.append(Character.toUpperCase(token.charAt(0))).append(token.substring(1)).append("");
        }
        str.toString().trim(); // Trim trailing space
        System.out.println(str);
        return  str.toString();


    }



    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

//    public String getGcmId(final Context activity) {
//        String regId = "";
//        new AsyncTask<Void, Void, String>() {
//
//            @Override
//            protected String doInBackground(Void... params) {
//                String msg = "";
//                try {
//                    GoogleCloudMessaging gcmObj = GoogleCloudMessaging
//                            .getInstance(activity);
//                    String regId = gcmObj
//                            .register(HostUrl.Push_Notification);
//                    msg = "Registration ID :" + regId;
//                    //System.out.println(msg);
//
//                } catch (IOException ex) {
//                    msg = "Error :" + ex.getMessage();
//                }
//                return msg;
//            }
//
//            @Override
//            protected void onPostExecute(String msg) {
////
//            }
//        }.execute(null, null, null);
//
//
//        return regId;
//    }


    private boolean mayRequestContacts(Activity activity, EditText mEmailView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (activity.checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (activity.shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            activity.requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });


        } else {
            activity.requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


}
