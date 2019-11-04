package shifoo.com.app.modules.cardviewquiz.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import oku.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CongratulationFragment extends Fragment {


    public CongratulationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_congratulation, container, false);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(rootView.getContext());
        LayoutInflater factory = LayoutInflater.from(rootView.getContext());
        final View view = factory.inflate(R.layout.dailogbox, null);
        alertDialog.setView(view);
       // ImageView CloseImage=rootView.findViewById(R.id.closeimage);
        alertDialog.setNegativeButton("close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        return rootView;
    }
}

