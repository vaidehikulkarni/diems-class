package com.example.diemsct;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AcdCse extends Fragment {

TextView cse;
    public AcdCse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_acd_cse, container, false);
        cse = (TextView) view.findViewById(R.id.cse);
        cse.setText(Html.fromHtml(getString(R.string.acd_cse)));

        return view;
    }

}
