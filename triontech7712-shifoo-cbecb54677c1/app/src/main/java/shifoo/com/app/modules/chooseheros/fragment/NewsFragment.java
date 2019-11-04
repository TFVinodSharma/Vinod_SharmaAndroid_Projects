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
public class NewsFragment extends Fragment  {


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View rootview=  inflater.inflate(R.layout.fragment_news, container, false);
        RecyclerView recyclerView = (RecyclerView)rootview.findViewById(R.id.simpleGridView);


          String[] Name={"MS Dhoni hello","MS Dhoni good ","MS Dhoni captain","MS DHONI"};

        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        recyclerView.setAdapter(new DataFragmentAdapter(Name));

        SpacesItemDecoration decoration = new SpacesItemDecoration(12);
        recyclerView.addItemDecoration(decoration);
       // RecyclerView.ItemDecoration.class

        // Inflate the layout for this fragment
        return rootview;
    }

   }

