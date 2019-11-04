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
public class ContentImageTextFragment extends Fragment {


    public ContentImageTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_content_image_text, container, false);

        TextView mtxtContentHeading=v.findViewById(R.id.txtcontentquizheading);
        TextView mtextContentQuiz=v.findViewById(R.id.txtcontentquiz);
        TextView mtextContentsource=v.findViewById(R.id.contentsource);
        TextView mtextContentsourceinfo=v.findViewById(R.id.contentsourceinfo);
        TextView mtextContenttime=v.findViewById(R.id.contenttime);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mtxtContentHeading.setTypeface(font);
        mtextContentQuiz.setTypeface(font);
        mtextContentsource.setTypeface(font);
        mtextContentsourceinfo.setTypeface(font);
        mtextContenttime.setTypeface(font);

        return v;
    }

}
