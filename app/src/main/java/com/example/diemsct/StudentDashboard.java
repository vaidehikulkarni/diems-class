package com.example.diemsct;


import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class StudentDashboard extends Fragment {

    FragmentManager manager;
    CardView ct;
    public StudentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stud_dash, container, false);
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
