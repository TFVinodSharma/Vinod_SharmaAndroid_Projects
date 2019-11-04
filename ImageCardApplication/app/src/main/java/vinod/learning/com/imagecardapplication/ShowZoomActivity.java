package vinod.learning.com.imagecardapplication;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import miaoyongjun.pagetransformer.MagicTransformer;
import miaoyongjun.pagetransformer.TransitionEffect;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

public class ShowZoomActivity extends AppCompatActivity {

    public static TransitionEffect transitionEffect;

    String[] imageUrls = new String[]{
            "https://demo.trionprojects.com/oku/dhoni1.png",
            "https://demo.trionprojects.com/oku/dhoni1.png",
            "https://demo.trionprojects.com/oku/dhoni1.png",
            "https://demo.trionprojects.com/oku/dhoni1.png",
           };
    List<View> mData = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_zoom);

        ImageView bg = (ImageView) findViewById(R.id.bg);
        Picasso.with(this).load("https://demo.trionprojects.com/oku/dhoni1.png")
                .into(bg);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        getLayoutData(mData);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int) (getResources().getDisplayMetrics().widthPixels * 0.8f),
                (int) (getResources().getDisplayMetrics().widthPixels * 0.8f));
        layoutParams.addRule(CENTER_IN_PARENT);
        viewPager.setLayoutParams(layoutParams);
        viewPager.setAdapter(new MyAdapter(mData));
        viewPager.setPageTransformer(true, MagicTransformer.getPageTransformer(transitionEffect));
        viewPager.setOffscreenPageLimit(imageUrls.length);
    }

    private void getLayoutData(List<View> data) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_item, null);
        for (String id : imageUrls) {
            data.add(view);
        }
    }

    public class MyAdapter extends PagerAdapter {

        List<View> mList = null;

        MyAdapter(List<View> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ShowZoomActivity.this).inflate(R.layout.layout_item_zoom, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Picasso.with(ShowZoomActivity.this).load(imageUrls[position]).into(imageView);
            container.addView(view);
            return view;
        }

    }
}