package shifoo.com.app.modules.profile.view;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import oku.app.R;
import shifoo.com.app.modules.chooseheros.adapter.ChooseHerosAdapter;
import shifoo.com.app.modules.profile.adapter.BadgesAdapter;
import shifoo.com.app.modules.profile.adapter.LeaderShipAdapter;
import shifoo.com.app.viewutils.SpacesItemDecoration;

public class ProfileActivity extends AppCompatActivity {

  int logos[] = {
         // R.drawable.badges, R.drawable. badges, R.drawable.badges, R.drawable.badges,

//            R.drawable.badges, R.drawable.badges
            };
    int photos[] = {R.mipmap.avtar2, R.mipmap. avtar3, R.mipmap.avtar5};
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        android.support.v7.widget.Toolbar ProfileTool = (android.support.v7.widget.Toolbar) findViewById(R.id.profiletool);
        setSupportActionBar(ProfileTool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TextView User_Profile_Name=findViewById(R.id.profile_name);
        TextView Profile_Number=findViewById(R.id.profile_number);
       TextView profile_Complete=findViewById(R.id.profile_complete);
        TextView Badges=findViewById(R.id.badges);
        TextView Leaderboard=findViewById(R.id.leaderboard);
        final ImageButton User_Profile_Compelete=findViewById(R.id.user_profile_compelete);
        final RelativeLayout ProfileCompleteUser=findViewById(R.id.profilecompleteuser);
        ProgressBar progressBar=findViewById(R.id.progressBarProfile);
        TextView ProfileDetails=findViewById(R.id.profiledetails);
        TextView Date=findViewById(R.id.date);
        EditText ProfileClss=findViewById(R.id.clss);
        EditText ProfileSchool=findViewById(R.id.school);
        EditText ProfileCity=findViewById(R.id.city);
        Button Btn_SaveProfile=findViewById(R.id.btn_saveprofile);

// Badeges Recyclerview
        RecyclerView BadgesRecyclerView=findViewById(R.id.badgesRecyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);

        BadgesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
       BadgesRecyclerView.setAdapter(new BadgesAdapter(logos));
       /* SpacesItemDecoration decoration = new SpacesItemDecoration(12);
        BadgesRecyclerView.addItemDecoration(decoration);
*/
       // LeaderShip RecyclerView
       RecyclerView LeadershipRecyclerview=findViewById(R.id.leadershipRecyclerview);
        LeadershipRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LeadershipRecyclerview.setAdapter(new LeaderShipAdapter(photos));

        RecyclerView LearderShpiNearRecyclerview=findViewById(R.id.leadershipNearRecyclerview);
        LearderShpiNearRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        LearderShpiNearRecyclerview.setAdapter(new LeaderShipAdapter(photos));


        final Typeface Profile = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        //User_Profile_Name.setTypeface(Profile);
        Profile_Number.setTypeface(Profile);
        profile_Complete.setTypeface(Profile);
        Badges.setTypeface(Profile);
        User_Profile_Name.setTypeface(Profile);
        Leaderboard.setTypeface(Profile);
        ProfileDetails.setTypeface(Profile);
        Date.setTypeface(Profile);
        ProfileClss.setTypeface(Profile);
        ProfileSchool.setTypeface(Profile);
        ProfileCity.setTypeface(Profile);
        Btn_SaveProfile.setTypeface(Profile);




        progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));


/*
        progressBar.getProgressDrawable().setColorFilter(
                Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);


*/

        User_Profile_Compelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCompleteUser.setVisibility(View.VISIBLE);
                final RelativeLayout Profile_Payout1=findViewById(R.id.layout);
              //  Profile_Payout1.setVisibility(View.GONE);
                User_Profile_Compelete.setVisibility(View.GONE);
                Profile_Payout1.setVisibility(View.GONE);



            }
        });
        Btn_SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileCompleteUser.setVisibility(View.GONE);
                User_Profile_Compelete.setVisibility(View.VISIBLE);
                final RelativeLayout Profile_Payout1=findViewById(R.id.layout);
                Profile_Payout1.setVisibility(View.VISIBLE);
            }
        });
    }
}
