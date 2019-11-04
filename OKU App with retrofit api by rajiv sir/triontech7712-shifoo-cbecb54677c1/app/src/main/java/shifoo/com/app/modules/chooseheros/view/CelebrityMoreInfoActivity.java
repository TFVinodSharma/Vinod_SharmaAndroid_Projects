package shifoo.com.app.modules.chooseheros.view;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.modules.home.view.HomeActivity;

public class CelebrityMoreInfoActivity extends BaseActivity {

    private ExpandableListView expandableListView;

    private ExpandableListViewAdapter expandableListViewAdapter;

    private List<String> listDataGroup;
    private HashMap<String, List<PersonalityItemDetail>> listDataChild;

    private String mHeroId;

    CollapsingToolbarLayout collapsingToolbarLayout;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_celebrity_more_info);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back_image);
//        getSupportActionBar().setTitle("");
        //toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        try {
            collapsingToolbarLayout.setTitle("");
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.transperent));
        } catch (Exception e) {
            e.printStackTrace();
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark)); // transperent color = #00000000

        // finds id's
        TextView mUserProfilename = findViewById(R.id.user_profile_name);
        TextView mUserProfileShortBio = findViewById(R.id.user_profile_short_bio);
        TextView mTxtFans = findViewById(R.id.txt_fans);
        TextView mTxtwords = findViewById(R.id.txt_words);
        //  final TextView mInfo=findViewById(R.id.info);


        // fonts use here
        Typeface Celebrity = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        /*Typeface CelebrityMore = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans-Regular.ttf");*/
        // mUserProfilename.setTypeface(CelebrityMore);
        mUserProfileShortBio.setTypeface(Celebrity);
        mTxtFans.setTypeface(Celebrity);
        mTxtwords.setTypeface(Celebrity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeroId = extras.getString("hero_id");


        }


        //  mInfo.setTypeface(Celebrity);

        // initializing the views
        initViews();

        // initializing the objects
        initObjects();

        // preparing list data
        initListData();



        try {
            showHideProgressDialog(true);
            JSONObject loginJson = new JSONObject();
           // loginJson.put("catId", "1");
            loginJson.put("heroProfileId", mHeroId);
            getHeroesProfile(HostUrl.BaseUrl + "hero/getHeroProfile", loginJson);
        } catch (Exception je) {

        }



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        expandableListView.setIndicatorBounds(expandableListView.getRight() - 40, expandableListView.getWidth());
    }

    private void initObjects() {

        // initializing the list of groups
        listDataGroup = new ArrayList<>();

        // initializing the list of child
        listDataChild = new HashMap<>();

        // initializing the adapter object
        expandableListViewAdapter = new ExpandableListViewAdapter(this, listDataGroup, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(expandableListViewAdapter);

    }

    public void getHeroesProfile(String urlOne, JSONObject jsonObject) {

        // System.out.println("Auth key  " + auth_key);
        try {

            System.out.println(urlOne);

            System.out.println("json Category of  Heroes" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    urlOne, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Get Category By Id Response " + response);
                            showHideProgressDialog(false);

                            try {
//                                JSONObject jb1 = response.getJSONObject("meta");
                                //  int code = jb1.getInt("code");
                                // if (code == 200) {

                                if (response.getString("Status").equalsIgnoreCase("true")) {
//                                    try {
//
//                                    } catch (JSONException je) {
//                                        je.printStackTrace();
//                                    }


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
                        Toast.makeText(CelebrityMoreInfoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
            RequestQueue queue = Volley.newRequestQueue(CelebrityMoreInfoActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    private void initListData() {


        // Adding group data
        listDataGroup.add(getString(R.string.text_intro));
        listDataGroup.add(getString(R.string.text_bio));
        listDataGroup.add(getString(R.string.text_batting_career));
        listDataGroup.add(getString(R.string.text_major_awards));
        listDataGroup.add(getString(R.string.text_definning_movement));
        listDataGroup.add(getString(R.string.text_becomingdhoni));


        // array of strings
        String[] array;
        String[] values;


        List<PersonalityItemDetail> introList = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_intro);
        for (String item : array) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setValue(item);
            introList.add(personalityItemDetail);
        }

        array = getResources().getStringArray(R.array.string_array_bio);
        values = getResources().getStringArray(R.array.string_array_bio_second);
        List<PersonalityItemDetail> bioList = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setHeader(array[i]);
            personalityItemDetail.setValue(values[i]);
            bioList.add(personalityItemDetail);
        }

        List<PersonalityItemDetail> BattingCareer = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_array_Batting_Career);
        values = getResources().getStringArray(R.array.string_array_Batting_Career2);

        for (int i = 0; i < array.length; i++) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setHeader(array[i]);
            personalityItemDetail.setValue(values[i]);
            BattingCareer.add(personalityItemDetail);
        }

        List<PersonalityItemDetail> MajorAwrdsandPrizes = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_major_awards_and_prizes);
        values = getResources().getStringArray(R.array.string_array_Batting_Career2);

        for (int i = 0; i < array.length; i++) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setHeader(array[i]);
            personalityItemDetail.setValue(values[i]);
            MajorAwrdsandPrizes.add(personalityItemDetail);
        }

        List<PersonalityItemDetail> definningmovement = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_definnig_movement);

        for (int i = 0; i < array.length; i++) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setValue(values[i]);
            definningmovement.add(personalityItemDetail);
        }
        List<PersonalityItemDetail> becomingDhoni = new ArrayList<>();
        array = getResources().getStringArray(R.array.string_major_awards_and_prizes);
        values = getResources().getStringArray(R.array.string_array_Batting_Career2);

        for (int i = 0; i < array.length; i++) {
            PersonalityItemDetail personalityItemDetail = new PersonalityItemDetail();
            personalityItemDetail.setValue(values[i]);
            personalityItemDetail.setHeader(array[i]);
            becomingDhoni.add(personalityItemDetail);
        }


        // Adding child data
        listDataChild.put(listDataGroup.get(0), introList);
        listDataChild.put(listDataGroup.get(1), bioList);
        listDataChild.put(listDataGroup.get(2), BattingCareer);
        listDataChild.put(listDataGroup.get(3), MajorAwrdsandPrizes);
        listDataChild.put(listDataGroup.get(4), definningmovement);
        listDataChild.put(listDataGroup.get(5), becomingDhoni);


        // notify the adapter
        expandableListViewAdapter.notifyDataSetChanged();
    }

    /**
     * method to initialize the views
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initViews() {
        expandableListView = findViewById(R.id.expandableListView);


    }


    public void onBackPressed() {

        // do something on back.
        Intent i = new Intent(this, AboutCelebrityActivity.class);
        startActivity(i);


        return;
    }
}
