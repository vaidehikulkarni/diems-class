package org.diems.diemsapp;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AcdEtc extends Fragment {

TextView etc;

    public AcdEtc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_acd_etc, container, false);
        etc = (TextView) view.findViewById(R.id.etc);
        etc.setText(Html.fromHtml(getString(R.string.acd_etc)));

        return view;
    }

}
