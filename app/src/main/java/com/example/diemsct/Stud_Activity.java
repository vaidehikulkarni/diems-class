package com.example.diemsct;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Stud_Activity extends Fragment {

    FragmentManager manager;
    CardView ct;
    public Stud_Activity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stud_act, container, false);
        ct = (CardView)view.findViewById(R.id.cv1);
        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent redirect=new Intent(getActivity(),ClasstestView.class);
                //getActivity().startActivity(redirect);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new ClasstestView())
                        .commit();
            }
        });

        return view;
    }

}
