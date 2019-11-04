package shifoo.com.app.modules.cardviewquiz.fragment;


import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import oku.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentVedioTextFragment extends Fragment {


    public ContentVedioTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_content_vedio_text, container, false);
      /*  VideoView Vedio=v.findViewById(R.id.videoView1);
        MediaController mc= new MediaController(getActivity());

        String path = "android.resource://" + getActivity().getPackageName() + "/" + R.raw.bhaloo;
        Vedio.setVideoURI(Uri.parse(path));
        Vedio.setMediaController(mc);
        Vedio.start();*/
        TextView mVedioTextHeading=v.findViewById(R.id.txtvediotextheading);
        TextView mTxtVedioTextContent=v.findViewById(R.id.txtvediotextcontent);

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/JosefinSans-Regular.ttf");
        mVedioTextHeading.setTypeface(font);
        mTxtVedioTextContent.setTypeface(font);

        return v ;
    }

}
