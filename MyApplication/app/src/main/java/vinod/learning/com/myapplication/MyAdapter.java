package vinod.learning.com.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends FragmentPagerAdapter {
    private Context context;
    private String desc, title;
    private List<String> stringList = new ArrayList<>();

    public MyAdapter(FragmentManager fm, Context context, List<String> stringList) {
        super(fm);
        this.context = context;
        this.stringList = stringList;

    }


    @Override
    public Fragment getItem(int position) {


        if (position <= getCount()) {

            return MyFragment.newInstance(stringList.get(position));
        }

        return null;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }
}