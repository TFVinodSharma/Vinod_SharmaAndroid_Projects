package shifoo.com.app.modules.chooseheros.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oku.app.R;
import shifoo.com.app.modules.chooseheros.adapter.DataFragmentAdapter;
import shifoo.com.app.viewutils.SpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class FourFragment extends Fragment {


    public FourFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=  inflater.inflate(R.layout.fragment_four, container, false);


        return rootview;

    }
}
