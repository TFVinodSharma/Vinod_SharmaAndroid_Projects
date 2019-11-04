package shifoo.com.app.modules.chooseheros.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oku.app.R;
import shifoo.com.app.modules.chooseheros.adapter.DataFragmentAdapter;
import shifoo.com.app.viewutils.SpacesItemDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiveFragment extends Fragment {


    public FiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=  inflater.inflate(R.layout.fragment_five, container, false);

        // RecyclerView.ItemDecoration.class

        // Inflate the layout for this fragment
        return rootview;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_five, container, false);
    }

}
