package learning.vinodsharma.com.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

public class VerticalViewPager extends ViewPager {


    public VerticalViewPager(@NonNull Context context) {
        this(context,null);
        init();
    }

    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    @Override
    public boolean canScrollHorizontally(int direction) {
        return false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollHorizontally(direction);
    }


    private  void init()
    {
       setPageTransformer(true,new VerticalPager());
       setOverScrollMode((View.OVER_SCROLL_NEVER));

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept=super.onInterceptTouchEvent(event);
        SwapXY(event);
        return intercept ;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(SwapXY(event));
    }

    private MotionEvent SwapXY(MotionEvent event)
    {
        float X=getWidth();
        float y=getHeight();

        float newx=(event.getY()/y)*X;
        float newy= (event.getX()/X)*y;
        event.setLocation(newx,newy);
        return event;

    }


    private   static  final class VerticalPager implements ViewPager.PageTransformer
    {

        @Override
        public void transformPage(@NonNull View view, float position) {
            final int pageWidth = view.getWidth();
            final int pageHeight = view.getHeight();
            if (position<-1)
            {
                view.setAlpha(0);
            }
            else if (position<=1)
            {
                view.setAlpha(1);
                view.setTranslationX(pageWidth* -position);
                float yposition=position*pageHeight;
                view.setTranslationY(yposition);
            }
            else
                {
                view.setAlpha(0);
            }

        }
    }
}
