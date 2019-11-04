package shifoo.com.app.modules.home.view;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

import oku.app.R;
import shifoo.com.app.modules.home.adapter.ChooseHerosAdaptertwo;
import shifoo.com.app.modules.profile.view.ProfileActivity;
import shifoo.com.app.viewutils.SpacesItemDecoration;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar Toolbar;
    int logos[] = {R.mipmap.dhoni, R.mipmap.dhoni, R.mipmap.dhoni, R.mipmap.dhoni,
            R.mipmap.dhoni, R.mipmap.dhoni};

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//  find views id's here
        TextView Txt_Celebrity_About = findViewById(R.id.txt_celebrity_about);
        TextView Txt_Continue_Reading = findViewById(R.id.txt_continue_reading);
        TextView Txt_Welcome = findViewById(R.id.txt_welcome);
        CircleImageView AvtarProfile = findViewById(R.id.avtarprofile);


        RecyclerView ChooseHerosRecycler = findViewById(R.id.choose_herosrecycler);
        ChooseHerosRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ChooseHerosRecycler.setAdapter(new ChooseHerosAdaptertwo(logos));
        SpacesItemDecoration decoration = new SpacesItemDecoration(15);
        ChooseHerosRecycler.addItemDecoration(decoration);


        AvtarProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });


//  Typeface here
        Typeface HerosTypeFace = Typeface.createFromAsset(getAssets(),
                "fonts/JosefinSans_Bold.ttf");
        Txt_Celebrity_About.setTypeface(HerosTypeFace);
        Txt_Continue_Reading.setTypeface(HerosTypeFace);
        Txt_Welcome.setTypeface(HerosTypeFace);


        //PHome.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.getHeaderView(0).findViewById(R.id.viewprofile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        navigationView.setItemIconTintList(null);
        // navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Handle the camera action
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_notification) {

        } else if (id == R.id.nav_invite) {

        } else if (id == R.id.nav_aboutus) {
            Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(HomeActivity.this, AboutUsActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_policy) {

        } else if (id == R.id.nav_termandcondition) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // change the navigation drawer icon here .

       /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawer, Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

        toggle.setHomeAsUpIndicator(R.drawable.menuicon);
       */
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
