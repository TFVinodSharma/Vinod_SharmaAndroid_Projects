package shifoo.com.app.modules.login.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.goodiebag.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.AppDialog;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.modules.chooseheros.view.ChooseHerosActivity;
import shifoo.com.app.modules.utility.AppUtilsMethod;


public class OtpActivity extends BaseActivity implements View.OnClickListener {

    private String mPhoneNumber;
    private String mOtpStr;


    private AppPreferences mAppPrefrences;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //find view's id
        final Button mOtpProceed = (Button) findViewById(R.id.otpreceived);
        mOtpProceed.setOnClickListener(this);
        TextView mTextOtpHeader = (TextView) findViewById(R.id.otpheader);
        TextView mTextOtpHeader2 = (TextView) findViewById(R.id.otpheader2);
        //  TV3 = (TextView) findViewById(R.id.otpheader3);
        TextView mTextOtpHeader4 = (TextView) findViewById(R.id.otpheader4);
        final TextView mTextResend = (TextView) findViewById(R.id.txt_resend);
        mTextResend.setOnClickListener(this);


        mAppPrefrences = AppPreferences.INSTANCE;
        mAppPrefrences.initAppPreferences(getApplicationContext());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mPhoneNumber = extras.getString("phone_number");
        }
        Pinview pinview = findViewById(R.id.mypinview);
        pinview.requestFocus();
        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                AppUtilsMethod.hideKeyBoard(OtpActivity.this);
                pinview.setFocusable(false);
                mOtpProceed.setBackground(getDrawable(R.drawable.round));

                mOtpStr = pinview.getValue();
                System.out.println("Pin Data Entered   " + pinview.getValue());
                // Toast.makeText(OtpActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });


// type faces
        Typeface f = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        mTextOtpHeader.setTypeface(f);
        mTextOtpHeader2.setTypeface(f);
        //  TV3.setTypeface(f);
        mTextOtpHeader4.setTypeface(f);
        mOtpProceed.setTypeface(f);
        mTextResend.setTypeface(f);

        AppUtilsMethod.hideKeyBoard(this);

//        mTextResend.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View v) {
//
//                mTextResend.setTextColor(getResources().getColorStateList(R.color.black));
//            }
//        });


    }


    public void verifyCustomerOtp(String urlOne, JSONObject jsonObject) {

        // System.out.println("Auth key  " + auth_key);
        try {

            System.out.println(urlOne);

            System.out.println("json Verify Otp" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    urlOne, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Verify Customer Otp Response " + response);
                            showHideProgressDialog(false);

                            try {
//                                JSONObject jb1 = response.getJSONObject("meta");
                                //  int code = jb1.getInt("code");
                                // if (code == 200) {

                                if (response.getString("Status").equalsIgnoreCase("true")) {

                                    JSONObject dataObject = response.getJSONObject("Data");
                                    System.out.println("Data Object   " + dataObject);

                                    if (Integer.parseInt(dataObject.getString("is_register")) == 0) {
                                        // is_register == 0
                                        Intent i = new Intent(OtpActivity.this, RegisterNameActivity.class);
                                        startActivity(i);
                                        finish();


                                    } else {

                                        mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.user_name.toString(), dataObject.getString("user_name"));
                                        mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.user_avtaar.toString(), dataObject.getString("user_avatar"));
                                        mAppPrefrences.setInt(AppPreferences.SharedPreferncesKeys.is_logged_in.toString(), 1);


                                        Intent intent = new Intent(OtpActivity.this, ChooseHerosActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();


                                    }


                                }else{
                                    //Toast.makeText(OtpActivity.this, "Please enter correct OTP", Toast.LENGTH_SHORT).show();
                                     AppDialog.callDialog("Incorrect OTP ", "Please enter correct OTP", OtpActivity.this);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    showHideProgressDialog(false);
                    System.out.println("12Error" + error.toString());
                    try {
                        if (error instanceof NetworkError) {
                            // DialogNScableApp.callDialog("Error", MessagesAccess.noInternetConnection, PaymentHistoryActivity.this);
                        } else if (error instanceof NoConnectionError) {
                            // DialogNScableApp.callDialog("Error", MessagesAccess.noInternetConnection, PaymentHistoryActivity.this);
                        } else if (error.networkResponse.data != null) {
                            String body = new String(error.networkResponse.data, "UTF-8");
                            //////println("response from server ///" + body);


                            JSONObject obj = new JSONObject(body);
                            JSONObject meta = obj.getJSONObject("meta");
                          //  String messgae = meta.getString("message");


                            // Toast.makeText(CustomerListActivity.this, messgae, Toast.LENGTH_SHORT).show();
                            // DialogNScableApp.callDialog("Error", messgae, PaymentHistoryActivity.this);

                        } else {
                            //  DialogNScableApp.callDialog("Error", MessagesAccess.serverError, PaymentHistoryActivity.this);
                        }

                    } catch (Exception e) {
                        Toast.makeText(OtpActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                        //DialogNScableApp.callDialog("Error", MessagesAccess.serverError, PaymentHistoryActivity.this);
                        e.printStackTrace();
                    }
                }
            }) {

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    headers.put("Auth-Key", "");
                    return headers;
                }
            };

            // Wait 30 seconds and don't retry more than once
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(15000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsonObjReq.setShouldCache(true);
            RequestQueue queue = Volley.newRequestQueue(OtpActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.otpreceived:
                try {
                    showHideProgressDialog(true);
                    JSONObject loginJson = new JSONObject();
                    loginJson.put("PhoneNumber", mPhoneNumber);
                    loginJson.put("VerificationOtp", mOtpStr);

                    verifyCustomerOtp(HostUrl.BaseUrl + "account/VerifyOtp", loginJson);
                } catch (JSONException je) {

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case R.id.txt_resend:


                break;

        }
    }
}
