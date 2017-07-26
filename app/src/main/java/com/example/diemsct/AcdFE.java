package com.example.diemsct;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AcdFE extends Fragment {
    TextView acdfe;


    public AcdFE() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_acd_fe, container, false);
        acdfe = (TextView) view.findViewById(R.id.textView31);
        acdfe.setText(Html.fromHtml(getString(R.string.about_fe3)));
        return  view;
    }

}
