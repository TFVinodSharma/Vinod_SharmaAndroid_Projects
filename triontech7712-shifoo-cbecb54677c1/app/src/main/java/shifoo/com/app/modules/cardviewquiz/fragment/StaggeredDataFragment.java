package shifoo.com.app.modules.cardviewquiz.fragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import oku.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaggeredDataFragment extends Fragment {


    public StaggeredDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_staggered_data, container, false);

        TextView mStaggeredTitle=v.findViewById(R.id.staggeredtitle);
        TextView mStaggeredQuestion=v.findViewById(R.id.staggeredQuestion);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mStaggeredTitle.setTypeface(font);
        mStaggeredQuestion.setTypeface(font);
        return v;
    }

}
