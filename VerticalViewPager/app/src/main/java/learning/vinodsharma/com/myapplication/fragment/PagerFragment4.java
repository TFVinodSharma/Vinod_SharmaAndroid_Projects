package learning.vinodsharma.com.myapplication.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import learning.vinodsharma.com.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PagerFragment4 extends Fragment {


    public PagerFragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager_fragment4, container, false);
    }

}
