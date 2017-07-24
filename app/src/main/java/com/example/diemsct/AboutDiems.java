package com.example.diemsct;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutDiems extends Fragment {

    TextView aboutDiems;

    public AboutDiems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_about_diems, container, false);
        aboutDiems = (TextView) view.findViewById(R.id.aboutDiems);
        aboutDiems.setText(Html.fromHtml(getString(R.string.about_diems1)));
        return view;
    }

}
