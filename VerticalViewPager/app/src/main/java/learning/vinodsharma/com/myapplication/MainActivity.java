package learning.vinodsharma.com.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import learning.vinodsharma.com.myapplication.fragment.PagerFragment;
import learning.vinodsharma.com.myapplication.fragment.PagerFragment2;
import learning.vinodsharma.com.myapplication.fragment.PagerFragment3;
import learning.vinodsharma.com.myapplication.fragment.PagerFragment4;

public class MainActivity extends AppCompatActivity {
 VerticalViewPager verticalViewPager;
  PagerAdapter pagerAdapter;


    static final int NUMBER_OF_PAGES = 2;

    VPagerAdapter mAdapter;
    VerticalViewPager mPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* mAdapter = new VPagerAdapter(getSupportFragmentManager());
        mPager = findViewById(R.id.verticalviewpager);
        mPager.setAdapter(mAdapter);*/

      List<Fragment>  list=new ArrayList<>();
        list.add(new PagerFragment());
        list.add(new PagerFragment2());
        list.add(new PagerFragment3());
        list.add(new PagerFragment4());


        verticalViewPager=findViewById(R.id.verticalviewpager);
        pagerAdapter=new VPagerAdapter(getSupportFragmentManager(),list);
        verticalViewPager.setAdapter(pagerAdapter);

    }
}