package shifoo.com.app.modules.chooseheros.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import shifoo.com.app.modules.chooseheros.fragment.FavoriteFragment;
import shifoo.com.app.modules.chooseheros.fragment.NewsFragment;
import shifoo.com.app.modules.chooseheros.fragment.PopularFragment;

public class AboutCelebrityAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> list = new ArrayList<>();

    public AboutCelebrityAdapter(FragmentManager fm) {
        super(fm);
        list.add(new NewsFragment());
        list.add(new PopularFragment());
        list.add(new FavoriteFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
       return   list.size();
    }
}
