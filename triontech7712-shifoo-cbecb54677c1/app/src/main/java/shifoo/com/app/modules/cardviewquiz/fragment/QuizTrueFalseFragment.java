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
public class QuizTrueFalseFragment extends Fragment {


    public QuizTrueFalseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_quiz_true_false, container, false);


        TextView mTrueFalse=v.findViewById(R.id.truefalse);
        TextView mTrueFalseHeading=v.findViewById(R.id.truefalseheading);


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mTrueFalse.setTypeface(font);
        mTrueFalseHeading.setTypeface(font);
        return v;
    }

}
