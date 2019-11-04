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
public class MCQFirstFragment extends Fragment {


    public MCQFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mcqfirst, container, false);

        TextView mTextFirstMcq=v.findViewById(R.id.txtfirsttmcq);
        TextView mMcqHeading=v.findViewById(R.id.mcqheading);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mTextFirstMcq.setTypeface(font);
        mMcqHeading.setTypeface(font);
        return v;
    }

}
