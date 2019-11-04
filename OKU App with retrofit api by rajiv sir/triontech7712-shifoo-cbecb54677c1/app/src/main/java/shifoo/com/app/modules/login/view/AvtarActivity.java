package shifoo.com.app.modules.login.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.Interface.AvtaarSelectInterface;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.modules.chooseheros.view.ChooseHerosActivity;
import shifoo.com.app.modules.login.adapter.SecondAdapter;
import shifoo.com.app.modules.login.model.AvatarModel;

public class AvtarActivity extends BaseActivity implements SecondAdapter.OnItemClicked, View.OnClickListener, AvtaarSelectInterface {

    TextView TxtHi, TxtUser;
    Button mSelectAvtar;
    int ClickedCount;
    private String mUserId;
    private String mUserName;

    private String mAvtaarSelectionStr;
    private AppPreferences mAppPrefrences;

    int logos[] = {R.mipmap.avtra1, R.mipmap.avtar2, R.mipmap.avtar3, R.mipmap.avtar5,
            R.mipmap.avtar6, R.mipmap.avtra1};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avtar);
        TxtHi = (TextView) findViewById(R.id.txthi);
        TxtUser = (TextView) findViewById(R.id.txt_avtarheading);
        mSelectAvtar = findViewById(R.id.selectavtar);
        mSelectAvtar.setOnClickListener(this);
        List<AvatarModel> avatarModels = getAvatarModels(logos);
        mAppPrefrences = AppPreferences.INSTANCE;
        mAppPrefrences.initAppPreferences(getApplicationContext());

        mUserId = mAppPrefrences.getString(AppPreferences.SharedPreferncesKeys.user_id.toString(), "");

        System.out.println("User id   " + mUserId);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mUserName = extras.getString("user_name");
        }


        SecondAdapter customAdapter = new SecondAdapter(getApplicationContext(), avatarModels,this);
        customAdapter.setOnItemClicked(this);
        ((GridView) findViewById(R.id.gridvew)).setAdapter(customAdapter);

    }

    @Override
    public void onSelectAvtaar(int index) {
        mAvtaarSelectionStr = String.valueOf(index);
    }

    private List<AvatarModel> getAvatarModels(int[] logos) {

        List<AvatarModel> avatarModelList = new ArrayList<>();

        for (int drawable : logos) {
            AvatarModel avtarModel = new AvatarModel();
            avtarModel.setSelected(false);
            avtarModel.setDrawableId(drawable);
            avatarModelList.add(avtarModel);
        }

        return avatarModelList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectavtar:

                try {
                    showHideProgressDialog(true);
                    JSONObject loginJson = new JSONObject();
                    loginJson.put("UserId", mUserId);
                    loginJson.put("UserName", mUserName);
                    loginJson.put("UserAvatar", mAvtaarSelectionStr);
                    loginJson.put("UserIp", "");

                    updateUserProfile(HostUrl.BaseUrl + "account/UpdateUserProfile", loginJson);


                } catch (JSONException je) {
                    je.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;


        }
    }


    public void updateUserProfile(String urlOne, JSONObject jsonObject) {

        // System.out.println("Auth key  " + auth_key);
        try {

            System.out.println(urlOne);

            System.out.println("json Verify Otp" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    urlOne, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("user Update Profile  Response " + response);
                            showHideProgressDialog(false);

                            try {
//                                JSONObject jb1 = response.getJSONObject("meta");
                                //  int code = jb1.getInt("code");
                                // if (code == 200) {

                                if (response.getString("Status").equalsIgnoreCase("true")) {

                                    JSONObject dataObject = response.getJSONObject("Data");
                                    System.out.println("Data Object   " + dataObject);
                                    mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.user_name.toString(),dataObject.getString("user_name"));
                                    mAppPrefrences.setString(AppPreferences.SharedPreferncesKeys.user_avtaar.toString(),response.getString("user_avatar"));

                                    Intent intent = new Intent(AvtarActivity.this, ChooseHerosActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                    finish();




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
                        Toast.makeText(AvtarActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
            RequestQueue queue = Volley.newRequestQueue(AvtarActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }

    public void newActivity(View view) {


    }


    @Override
    public void onItemCLicked() {
        mSelectAvtar.setEnabled(true);
        mSelectAvtar.setBackgroundResource(R.drawable.round);
    }
}

