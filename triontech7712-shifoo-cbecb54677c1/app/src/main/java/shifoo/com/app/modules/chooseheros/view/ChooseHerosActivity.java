package shifoo.com.app.modules.chooseheros.view;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import oku.app.R;
import shifoo.com.app.Interface.HeroSelectionInterface;
import shifoo.com.app.apppreferences.AppPreferences;
import shifoo.com.app.base.activity.BaseActivity;
import shifoo.com.app.library.HostUrl;
import shifoo.com.app.Models.chooseHeroesModel;
import shifoo.com.app.library.LibFunction;
import shifoo.com.app.modules.chooseheros.adapter.ChooseHerosAdapter;

public class ChooseHerosActivity extends BaseActivity implements HeroSelectionInterface, View.OnClickListener {

    public String[] Name = {"Ms Dhoni", "Ms Dhoni", "Ms Dhoni"};
    public String[] Rank = {"Crickter ", "Crickter", "Crickter"};
    Button mGetStarted;

    private int mHeroesCount = 0;

    private ArrayList<chooseHeroesModel> mHeroesList;
    private ChooseHerosAdapter chooseHerosAdapter;
    private RecyclerView ChooseHeroRecycler;

    private String user_Name;
    private AppPreferences mAppPrefrences;

    private TextView mUserName, mUnlockedHeros;
    private int mClickPos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_heros);

        mAppPrefrences = AppPreferences.INSTANCE;
        mAppPrefrences.initAppPreferences(getApplicationContext());

// find view's id
        ChooseHeroRecycler = findViewById(R.id.choose_heros);
        mUserName = findViewById(R.id.username);
        mUnlockedHeros = findViewById(R.id.txt_unlockedheros);
        TextView mChooseDreamHero = findViewById(R.id.txt_dreamhero);
        mGetStarted = findViewById(R.id.btn_started);
        mGetStarted.setOnClickListener(this);

        LinearLayoutManager layoutManager
               = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        ChooseHeroRecycler.setLayoutManager(layoutManager);
        mHeroesList = new ArrayList<chooseHeroesModel>();
        chooseHerosAdapter = new ChooseHerosAdapter(this, mHeroesList, this);
        ChooseHeroRecycler.setAdapter(chooseHerosAdapter);


        user_Name = mAppPrefrences.getString(AppPreferences.SharedPreferncesKeys.user_name.toString(),"");


        // Set adapter on Recyclerview
//       ChooseHeros.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//       ChooseHeros.setAdapter(new ChooseHerosAdapter(Name,Rank,this));


        // typeface fonts
        Typeface HerosTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        // mTxt_heros.setTypeface(HerosTypeFace);
        mUserName.setTypeface(HerosTypeFace);
        mUnlockedHeros.setTypeface(HerosTypeFace);
        mChooseDreamHero.setTypeface(HerosTypeFace);
        //mGetStarted.setTypeface(HerosTypeFace);
        try {
            showHideProgressDialog(true);
            JSONObject loginJson = new JSONObject();
            // loginJson.put("PhoneNumber", mPhoneNumber);
            getAllHeroes(HostUrl.BaseUrl + "hero/getHeros", loginJson);
        } catch (Exception je) {

        }


    }


    private void setHeroesData(JSONArray heroarray){

        try {
            for (int i = 0; i < heroarray.length(); i++) {
                JSONObject jsonObject = heroarray.getJSONObject(i);
                chooseHeroesModel details = new chooseHeroesModel();

                details.setHero_id(jsonObject.getString("hero_id"));
                details.setHero_name(jsonObject.getString("hero_name"));
                details.setHero_profession(jsonObject.getString("hero_profession"));
                details.setHero_short_description(jsonObject.getString("hero_short_description"));
                details.setFans_count(jsonObject.getString("fans_count"));
                details.setCover_photos(jsonObject.getString("hero_coverphoto"));
                details.setSort_order(jsonObject.getString("sort_order"));
                details.setHero_thumbnail(jsonObject.getString("hero_thumbnail"));
                details.setSetSelected(0);
                mHeroesList.add(details);

            }


            String user_fname  = LibFunction.capitalize(user_Name.substring(0,user_Name.indexOf(" ")));
            mUserName.setText("Congratulations " + user_fname + "!");
            mUnlockedHeros.setText("You have unlocked " + mHeroesCount + " heros." );

        }catch (JSONException je){
            je.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }



        chooseHerosAdapter = new ChooseHerosAdapter(ChooseHerosActivity.this, mHeroesList, ChooseHerosActivity.this);
        ChooseHeroRecycler.setAdapter(chooseHerosAdapter);



    }

    public void getAllHeroes(String urlOne, JSONObject jsonObject) {

        // System.out.println("Auth key  " + auth_key);
        try {

            System.out.println(urlOne);

            System.out.println("json All Heroes" + jsonObject);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    urlOne, jsonObject,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Get All Heroes Response " + response);
                            showHideProgressDialog(false);
// showProgress(false);
                            try {
//                                JSONObject jb1 = response.getJSONObject("meta");
                                //  int code = jb1.getInt("code");
                                // if (code == 200) {

                                if (response.getString("Status").equalsIgnoreCase("true")) {
                                    try {

                                        JSONArray dataArray = response.getJSONArray("Data");
                                        mHeroesCount = dataArray.length();

                                        setHeroesData(dataArray);






                                    } catch (JSONException je) {
                                        je.printStackTrace();
                                    }


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
                        Toast.makeText(ChooseHerosActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

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
            RequestQueue queue = Volley.newRequestQueue(ChooseHerosActivity.this);
            queue.add(jsonObjReq);

        } catch (Exception e) {

            e.printStackTrace();
        }


    }


    @Override
    public void onItemClick(int flag, int pos) {

        mGetStarted.setBackgroundResource(R.drawable.round);
        mClickPos = pos;


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_started:
                final chooseHeroesModel subsItem = mHeroesList.get(mClickPos);

                Intent intent = new Intent(this, AboutCelebrityActivity.class);
                intent.putExtra("hero_id",subsItem.getHero_id());
                intent.putExtra("hero_name",subsItem.getHero_name());
                intent.putExtra("hero_profession",subsItem.getHero_profession());
                intent.putExtra("hero_short_desc",subsItem.getHero_short_description());
                startActivity(intent);

                break;
        }
    }
}
