package shifoo.com.app.modules.login.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.EditText;
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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
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
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.modules.home.view.HomeActivity;
import shifoo.com.app.modules.login.model.SignupRequest;
import shifoo.com.app.modules.login.viewmodel.SignupViewModel;
import shifoo.com.app.modules.utility.AppUtilsMethod;


public class LoginActivity extends BaseActivity {

    SignupViewModel signupViewModel;

    private String mPhoneNumber;
    private ProgressDialog dialog;


   private AppPreferences mAppPrefrences;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signupViewModel = ViewModelProviders.of(LoginActivity.this).get(SignupViewModel.class);
        registerLoginObserver();

        mAppPrefrences = AppPreferences.INSTANCE;
        mAppPrefrences.initAppPreferences(getApplicationContext());


// view's id find here
        TextView mTextView_Header = (TextView) findViewById(R.id.tv_header);
        TextView mTextView_Header1 = (TextView) findViewById(R.id.tv_header1);
        TextView textView_header2 = (TextView) findViewById(R.id.tv_header2);
        final CheckBox ck = (CheckBox) findViewById(R.id.checkbox);
        TextView mPrivacy = (TextView) findViewById(R.id.tv_header3);
        final Button mAccepttermAndCondition = findViewById(R.id.accepttermandcondition);
        final EditText phoneNumberEdt = (EditText) findViewById(R.id.phone_number_edt);
        phoneNumberEdt.requestFocus();

        String privacyPolicyUrl = "";
        String termsofUse = "";

        String value = " <font color='#393F5F'> <a href=\"" + privacyPolicyUrl + "\"  >Privacy Policy</a></font> and <font color='#393F5F'> <a href=\"" + termsofUse + "\" >Terms of use</a></font>.";


        // fonts use here
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        Typeface faceregular = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans-Regular.ttf");
        mTextView_Header.setTypeface(face);
        mTextView_Header1.setTypeface(faceregular);
        textView_header2.setTypeface(faceregular);
        ck.setTypeface(face);
        mPrivacy.setText(Html.fromHtml(value));
        mPrivacy.setTypeface(face);
        mAccepttermAndCondition.setTypeface(face);

        phoneNumberEdt.setTypeface(face);

        phoneNumberEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 13 && ck.isChecked()) {
                    mAccepttermAndCondition.setBackground(getDrawable(R.drawable.round));
                    mAccepttermAndCondition.setEnabled(true);
                } else {
                    mAccepttermAndCondition.setBackground(getDrawable(R.drawable.roundgrey));
                    mAccepttermAndCondition.setEnabled(false);
                }
            }
        });

        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && phoneNumberEdt.getText().toString().length() == 10) {
                    mAccepttermAndCondition.setBackground(getDrawable(R.drawable.round));
                    mAccepttermAndCondition.setEnabled(true);
                    AppUtilsMethod.hideKeyBoard(LoginActivity.this);

                    mAccepttermAndCondition.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            SignupRequest signupRequest = new SignupRequest();
                            mPhoneNumber = phoneNumberEdt.getText().toString();
                            try {
                                showHideProgressDialog(true);
                                JSONObject loginJson = new JSONObject();
                                loginJson.put("PhoneNumber", mPhoneNumber);

                                GetCustomerOtp(HostUrl.BaseUrl + "account/getOtp", loginJson);
                            } catch (JSONException je) {

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

//                            signupRequest.setPhoneNumber(phoneNumberEdt.getText().toString());
//
//                            showProgressDialogue(getString(R.string.txt_processing), getString(R.string.please_wait_progress));
//
                           //signupViewModel.doRegisteration(signupRequest);
                        }
                    });

                } else {
                    mAccepttermAndCondition.setBackground(getDrawable(R.drawable.roundgrey));
                    mAccepttermAndCondition.setEnabled(false);

                }
            }
        });


    }

    public void GetCustomerOtp(String urlOne, JSONObject jsonObject) {

       // System.out.println("Auth key  " + auth_key);
        try {

            System.out.println(urlOne);

            System.out.println("json customer Otp" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    urlOne, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Get Customer Otp Response " + response);
                            showHideProgressDialog(false);
// showProgress(false);
                            try {
//                                JSONObject jb1 = response.getJSONObject("meta");
                              //  int code = jb1.getInt("code");
                               // if (code == 200) {

                                if(response.getString("Status").equalsIgnoreCase("true")){

                                    startSmsListener();

                                    JSONObject dataObject = response.getJSONObject("Data");
                                    mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.user_id.toString(),dataObject.getString("user_id"));
                                    mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.auth_key.toString(),response.getString("token"));


                                    Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                    intent.putExtra("phone_number",mPhoneNumber);
                                    startActivity(intent);


                                    System.out.println("Data Object   " + jsonObject);


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
                            String messgae = meta.getString("message");



                            // Toast.makeText(CustomerListActivity.this, messgae, Toast.LENGTH_SHORT).show();
                            // DialogNScableApp.callDialog("Error", messgae, PaymentHistoryActivity.this);

                        } else {
                            //  DialogNScableApp.callDialog("Error", MessagesAccess.serverError, PaymentHistoryActivity.this);
                        }

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


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

    private void startSmsListener(){
        SmsRetrieverClient client = SmsRetriever.getClient(this /* context */);
        Task<Void> task = client.startSmsRetriever();
        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                System.out.println("On Success  SmS Listener rrrr");


            }


        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                System.out.println("On Failure  SmS Listener rrrr");

            }
        });
    }
    private void registerLoginObserver() {

        signupViewModel.registerUser().observe(LoginActivity.this, signupResponse -> {
            hideProgressDialgogue();
            if (signupResponse == null) {
                showErrorMessage(getString(R.string.not_valid_response));
            } else if (signupResponse.getStatus()) {
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                intent.putExtra("phone_number",mPhoneNumber);
                startActivity(intent);
            } else {
                showErrorMessage(signupResponse.getMessage());
            }
        });
    }

}
