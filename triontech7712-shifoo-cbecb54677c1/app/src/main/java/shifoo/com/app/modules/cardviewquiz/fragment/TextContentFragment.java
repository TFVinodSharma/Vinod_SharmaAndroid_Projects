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
public class TextContentFragment extends Fragment {


    public TextContentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_text_content, container, false);
        TextView mTxtDummy=v.findViewById(R.id.txtdummy);
        TextView mTxtHeadingQuiz=v.findViewById(R.id.txtheadingquiz);
        TextView mTxtQuizContent=v.findViewById(R.id.txtquizcontent);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mTxtDummy.setTypeface(font);
        mTxtHeadingQuiz.setTypeface(font);
        mTxtQuizContent.setTypeface(font);

        return v;
    }

}
