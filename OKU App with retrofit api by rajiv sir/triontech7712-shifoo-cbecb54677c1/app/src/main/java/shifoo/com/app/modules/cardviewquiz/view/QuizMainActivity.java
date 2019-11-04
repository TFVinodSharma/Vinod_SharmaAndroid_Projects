package shifoo.com.app.modules.cardviewquiz.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import oku.app.R;

import com.prabhat1707.verticalpager.VerticalViewPager;

import shifoo.com.app.modules.cardviewquiz.adapter.VPagerAdapter;
import shifoo.com.app.modules.cardviewquiz.fragment.CongratulationFragment;
import shifoo.com.app.modules.cardviewquiz.fragment.ContentImageTextFragment;
import shifoo.com.app.modules.cardviewquiz.fragment.ContentTextTypeFragment;
import shifoo.com.app.modules.cardviewquiz.fragment.ContentVedioTextFragment;
import shifoo.com.app.modules.cardviewquiz.fragment.RateToipcFRagment;
import shifoo.com.app.modules.cardviewquiz.fragment.TextContentFragment;
import shifoo.com.app.modules.chooseheros.view.AboutCelebrityActivity;

public class QuizMainActivity extends AppCompatActivity {
   private  VerticalViewPager verticalViewPager;
    PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_quiz_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarquiz);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setElevation((float) 10.0);
        ImageView Imageviewback = findViewById(R.id.imageViewback);
        Imageviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizMainActivity.this, AboutCelebrityActivity.class);
                startActivity(i);
            }
        });

        List<Fragment> list = new ArrayList<>();
        list.add(new ContentImageTextFragment());
        list.add(new TextContentFragment());
        list.add(new ContentVedioTextFragment());
        list.add(new ContentTextTypeFragment());
        list.add(new RateToipcFRagment());
        list.add(new CongratulationFragment());
       /* list.add(new MCQFirstFragment());
        list.add(new StaggeredDataFragment());
        list.add(new QuizTrueFalseFragment());
*/

        verticalViewPager = findViewById(R.id.viewPager);
        pagerAdapter = new VPagerAdapter(getSupportFragmentManager(), list);
        verticalViewPager.setAdapter(pagerAdapter);

    }
}
