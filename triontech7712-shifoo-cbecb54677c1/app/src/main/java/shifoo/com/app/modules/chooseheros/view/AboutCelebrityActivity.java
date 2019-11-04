package shifoo.com.app.modules.chooseheros.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.getbase.floatingactionbutton.FloatingActionsMenu;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.modules.chooseheros.fragment.FavoriteFragment;
import shifoo.com.app.modules.chooseheros.fragment.FiveFragment;
import shifoo.com.app.modules.chooseheros.fragment.FourFragment;
import shifoo.com.app.modules.chooseheros.fragment.NewsFragment;
import shifoo.com.app.modules.chooseheros.fragment.PopularFragment;
import shifoo.com.app.modules.chooseheros.fragment.SixFragment;
import shifoo.com.app.modules.home.view.FAQActivity;
import shifoo.com.app.modules.home.view.HomeActivity;
import shifoo.com.app.modules.login.view.LoginActivity;
import shifoo.com.app.modules.login.view.OtpActivity;

public class AboutCelebrityActivity extends BaseActivity implements View.OnClickListener {
    // FloatingActionButton fab;
    //FloatingActionsMenu menuMultipleActions;

    CollapsingToolbarLayout collapsingToolbarLayout;
    private String mHeroId;
    ViewPager mViewPager;
    TabLayout mTabLay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for full size of image in status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_back_image);
//        getSupportActionBar().setTitle("");
        //toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24dp);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        try {
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.grey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorPrimaryDark)); // transperent color = #00000000
        // collapsingToolbarLayout.setCollapsedTitleTextColor(Color.rgb(255, 255, 255)); //Color of your title

        // appbar = (AppBarLayout) findViewById(R.id.appbar);
        TextView TxtNameOfCelebrity = findViewById(R.id.txtNameOfCelebrity);
        TextView TxtDesgCelebrity = findViewById(R.id.txtDesgCelebrity);
        Typeface About = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        TxtNameOfCelebrity.setTypeface(About);
        TxtDesgCelebrity.setTypeface(About);

        ImageView AboutBackButton = findViewById(R.id.aboutbackbutton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mHeroId = extras.getString("hero_id");
            String hero_name = extras.getString("hero_name");
            String hero_profession = extras.getString("hero_profession");
            String hero_short_desc = extras.getString("hero_short_desc");

            TxtNameOfCelebrity.setText(hero_name);
            TxtDesgCelebrity.setText(hero_profession);
           // TxtNameOfCelebrity.setText(hero_name);

        }


        // fab = (FloatingActionButton) findViewById(R.id.multiple_actions);
//        com.getbase.floatingactionbutton.FloatingActionButton fab1=findViewById(R.id.action_a);
//        com.getbase.floatingactionbutton.FloatingActionButton fab2=findViewById(R.id.action_b);
//        fab1.setOnClickListener(this);
//        fab2.setOnClickListener(this);
//        menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);


        AboutBackButton.setOnClickListener(this);

        LinearLayout Btn_MoreInfo = findViewById(R.id.btn_moreinfo);
        Btn_MoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(AboutCelebrityActivity.this, "hello", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AboutCelebrityActivity.this, CelebrityMoreInfoActivity.class);
                i.putExtra("hero_id",mHeroId);
                startActivity(i);
            }
        });
        mViewPager = findViewById(R.id.viewPager);
       // setupViewPager(viewPager);

        mTabLay = findViewById(R.id.tablayout);
       // mTabLay.setupWithViewPager(viewPager);


        try {
            showHideProgressDialog(true);
            JSONObject loginJson = new JSONObject();
            loginJson.put("catId", "1");
            loginJson.put("heroId", mHeroId);
            getCategoryByHeroId(HostUrl.BaseUrl + "hero/getTopics", loginJson);
        } catch (Exception je) {

        }
    }


    private void setData(){



    }


    public void getCategoryByHeroId(String urlOne, JSONObject jsonObject) {

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
                        Toast.makeText(AboutCelebrityActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
            RequestQueue queue = Volley.newRequestQueue(AboutCelebrityActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    private void setupViewPager(ViewPager viewPager) {
        AboutCelebrityActivity.ViewPagerAdapter adapter = new AboutCelebrityActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NewsFragment(), "Popular");
        adapter.addFrag(new PopularFragment(), "MS Dhoni");
        adapter.addFrag(new FavoriteFragment(), "Learn Cricket");
        adapter.addFrag(new FourFragment(), "FOUR");
        adapter.addFrag(new FiveFragment(), "FIVE");
        adapter.addFrag(new SixFragment(), "SIX");
       /* adapter.addFrag(new SevenFragment(), "SEVEN");
        adapter.addFrag(new EightFragment(), "EIGHT");
        adapter.addFrag(new NineFragment(), "NINE");
        adapter.addFrag(new TenFragment(), "TEN");
*/
        viewPager.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onClick(View v) {

      /*  if (v.getId()==R.id.multiple_actions)
        {
            fab1.setVisibility(View.VISIBLE);
            fab2.setVisibility(View.VISIBLE);
        }
*/
//        if (v.getId()==R.id.action_a)
//        {
//            Intent i= new Intent(AboutCelebrityActivity.this,FAQActivity.class);
//            startActivity(i);
//        }
        // else
//            if (v.getId()==R.id.action_b)
//        {
//            Intent callIntent = new Intent(Intent.ACTION_DIAL);
//            callIntent.setData(Uri.parse("tel: +98990*******"));
//            startActivity(callIntent);
//
//        }
        // else
        if (v.getId() == R.id.aboutbackbutton) {
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
        }

    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
