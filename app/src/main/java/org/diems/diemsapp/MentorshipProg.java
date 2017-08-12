package org.diems.diemsapp;


import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MentorshipProg extends Fragment {
    TextView mentorship;


    public MentorshipProg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mentorship_prog, container, false);
        mentorship = (TextView)view.findViewById(R.id.textView28);
        mentorship.setText(Html.fromHtml(getString(R.string.about_metoship3)));
        return view;
    }

}
