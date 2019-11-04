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


package shifoo.com.app.modules.utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;



import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oku.app.R;


public class AppUtilsMethod {
    public static int sSpinnerPosion = 0;
    public static String sMeeting_Address = "";
    public static String sCountry_Code = "";

    public static List<Long> allContactList = new ArrayList<Long>();

    /**
     * This method is used to hide the keyboard
     *
     * @param activity
     * @return void
     */
    public static void hideKeyBoard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dialPhoneNumber(String PhoneNumner,Activity activity) {
        try {
//                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + dt.getMobileNo()));
//                    context.startActivity(intent);

            Intent intent = new Intent(Intent.ACTION_DIAL);
//                    intent.setPackage("com.android.server.telecom");
            intent.setData(Uri.parse("tel:" + PhoneNumner));
           activity. startActivity(intent);
        } catch (SecurityException ee) {
            ee.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static  Location getLastKnownLocation(LocationManager locationManager) {

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        try {
            for (String provider : providers) {
                Location l = locationManager.getLastKnownLocation(provider);
                System.out.println("Providerssss    " + provider);
                System.out.println("location on last known Location  " + l);
                bestLocation = l;

                if (l == null) {
                    continue;
                }
                if (bestLocation == null
                        || l.getAccuracy() < bestLocation.getAccuracy()) {

                    System.out.println("found best last known location   " + l);
                    bestLocation = l;
                    System.out.println("Best Location   " + bestLocation);
                }
            }
        }catch (SecurityException se){
            se.printStackTrace();
        }

        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }


//    public static  boolean checkLocationPermission(Activity context,int PERMISSIONS_REQUEST_LOCATION) {
//        if (ContextCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(context)
//                        .setTitle(R.string.title_location_permission)
//                        .setMessage(R.string.text_location_permission)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(context,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        PERMISSIONS_REQUEST_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(context,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }


    public static String getPackageManager(Activity activity) {
        String  versionName= "";
        try {
            PackageManager manager = activity.getPackageManager();
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            versionName = info.versionName;
           return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }
        return versionName;
    }


//    public static LatLng getLocationFromAddress(String strAddress, Activity activity) {
//
//        Geocoder coder = new Geocoder(activity);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try {
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//            Address location = address.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new LatLng(location.getLatitude(), location.getLongitude());
//
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//        }
//
//        return p1;
//    }


    public static void getAddressFromLocation(final double latitude, final double longitude, final Context context) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }

                        Log.e("locality,country,admi", "" + address.getCountryName() + " " + address.getCountryName());

                        sMeeting_Address = address.getLocality();

                        sCountry_Code = address.getCountryCode();

                        Log.e("country  code", "" + sCountry_Code);
                        //AppPreferences.INSTANCE.setCountryName(address.getCountryName());
                        Log.e("===address===>", "" + sMeeting_Address);
                        //Log.e("===country con===>", "" + AppPreferences.INSTANCE.getCountryName());
                    }
                } catch (IOException e) {
                    Log.e(getClass().getName(), "Unable connect to Geocoder", e);
                }
            }
        };
        thread.start();
    }

    public static String getAppVersion(Context context) {
        String appVersion = "";
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            appVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    /**
     * This method is used to show the keyboard
     *
     * @return void
     */
    public static void showKeyBoard(Activity activity, EditText editText) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * This method is used to check if the email address entered is valid or not
     *
     * @param email
     * @return boolean
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    /**
     * This method is used to check if the phone address entered is valid or not
     *
     * @param phone
     * @return boolean
     */
    public static boolean isPhoneValid(String phone) {
        String expression = "/^(\\+\\d{1,3}[- ]?)?\\d{10}$/";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }


    /**
     * used to convert dp value into pixel value
     *
     * @param context
     * @param dp
     * @return pixel value (int)
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    /**
     * This method is used to get Formatted date which will be updated to the server
     *
     * @param number
     * @return date as an String
     */
    public static String getMonthName(int number) {
        String month = null;
        switch (number) {
            case 1:
                month = "JAN";
                break;
            case 2:
                month = "FEB";
                break;

            case 3:
                month = "MAR";
                break;

            case 4:
                month = "APR";
                break;

            case 5:
                month = "MAY";
                break;

            case 6:
                month = "JUN";
                break;

            case 7:
                month = "JUL";
                break;

            case 8:
                month = "AUG";
                break;

            case 9:
                month = "SEP";
                break;

            case 10:
                month = "OCT";
                break;

            case 11:
                month = "NOV";
                break;

            case 12:
                month = "DEC";
                break;
        }
        return month;
    }

    /**
     * used to format date into specific format
     *
     * @param date
     * @return String date
     */
    public static String formateDateForServer(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String finalDate = timeFormat.format(myDate);
        return finalDate;
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * used to format date into specific format
     *
     * @param date
     * @return String date
     */
    public static String formateDateForApp(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String finalDate = timeFormat.format(myDate);
        return finalDate;
    }


    /**
     * used to convert radius into km
     *
     * @param radius
     */
    public static String convertIntoKM(int radius) {
        StringBuilder builder = new StringBuilder();
        if (radius < 1000) {
            return builder.append(radius).append(" m").toString();
        } else {
            return builder.append(radius / 1000).append(" Km").toString();
        }
    }

    /**
     * This method is used to convert the milliseconds to UTC time
     *
     * @param time
     * @return String
     */
    public static String getUTCTimeFromMilliseconds(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String UTCTime = simpleDateFormat.format(calendar.getTime());
        return UTCTime;
    }

    /**
     * This method is used to convert the UTC time to milliseconds
     *
     * @param time
     * @return long
     */
    public static long getMillisecondsFromUTCTime(String time) {
        if (time == null) {
            return -1;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date;
        try {
            date = simpleDateFormat.parse(time);
            long milliseconds = date.getTime();
            return milliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * This methos will return formated string into 12 hour format
     *
     * @param messageHour
     * @param messageMinute
     */
    public static String getFormatedString(int messageHour, int messageMinute, Context context) {
        StringBuilder builder = new StringBuilder();
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            builder.append(messageHour < 10 ? "0" + messageHour : messageHour);
            builder.append(":");
            builder.append(messageMinute < 10 ? "0" + messageMinute : messageMinute);
        } else {
            String amPmString = messageHour >= 12 ? " PM" : " AM";
            if (messageHour > 12)
                messageHour = messageHour - 12;
            builder.append(messageHour < 10 ? "0" + messageHour : messageHour);
            builder.append(":");
            builder.append(messageMinute < 10 ? "0" + messageMinute : messageMinute);
            builder.append(amPmString);
        }
        return builder.toString();
    }

    public static long convertDateTimeinMillies(String dateString) {
        long timeInMilliseconds;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        Date mDate = new Date();
        try {
            mDate = dateFormat.parse(dateString);
            timeInMilliseconds = mDate.getTime();
            Log.e("DATE AND MILLIES " + mDate.getTime(), " date " + dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mDate.getTime();
    }

    public static void callHelpLine(String mobNo, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mobNo));
        activity.startActivity(intent);

    }

	/*
	private void showMultipleMeetingPlace(List<AllMeetingCreatedChildBean> allMeetingCreatedChildBeans) {

		for (int i = 0; i < allMeetingCreatedChildBeans.size(); i++)
		{

			AllMeetingCreatedChildBean bean = allMeetingCreatedChildBeans.get(i);
			if(bean.getmMeetingLat()!=0.0 || bean.getmMeetingLong()!=0.0){
				Marker marker= googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.pin)).position(new LatLng(bean.getmMeetingLat(),bean.getmMeetingLong())).title(bean.getmMeetingAddress()));
				createCircle(bean.getmMeetingLat(), bean.getmMeetingLong(), bean.getmMeetingThreasHold());
			}

		}
		if(allMeetingCreatedChildBeans.size()>0)
		{
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allMeetingCreatedChildBeans.get(0).getmMeetingLat(),allMeetingCreatedChildBeans.get(0).getmMeetingLong()), 13));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(13), 5000, null);
		}

	}
	*/


}