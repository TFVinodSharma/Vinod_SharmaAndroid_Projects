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
public class ContentTextTypeFragment extends Fragment {


    public ContentTextTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_text_type, container, false);

        TextView mTxtTypeDummy=v.findViewById(R.id.txttypedummy);
        TextView mTxtTypeHeading=v.findViewById(R.id.txtTypeHeading);
        TextView mTxtTypeContent=v.findViewById(R.id.txtTypeContent);

        TextView mTextFeel=v.findViewById(R.id.txtfeel);


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mTxtTypeDummy.setTypeface(font);
        mTxtTypeHeading.setTypeface(font);
        mTxtTypeContent.setTypeface(font);
        mTextFeel.setTypeface(font);
        return v;
    }

}
