package org.diems.diemsapp;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AboutDiems extends Fragment {

    TextView aboutDiems, abtDiems;

    public AboutDiems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        MainActivity.actionBar.setTitle("About");
        MainActivity.navigationBarMenu.findItem(R.id.nav_about).setChecked(true);

        View view = inflater.inflate(R.layout.fragment_about_diems, container, false);
       aboutDiems = (TextView) view.findViewById(R.id.aboutDiems);
        aboutDiems.setText(Html.fromHtml(getString(R.string.about_diems1)));
        abtDiems = (TextView) view.findViewById(R.id.objdiems);
        abtDiems.setText(Html.fromHtml(getString(R.string.objdiems)));
        return view;
    }

}
