package org.diems.diemsapp;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class acadMBA extends Fragment {

    TextView mba;
    public acadMBA() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_acad_mb, container, false);
        mba = (TextView) view.findViewById(R.id.missionmba);
        mba.setText(Html.fromHtml(getString(R.string.missionmba)));
        return view;
    }

}
