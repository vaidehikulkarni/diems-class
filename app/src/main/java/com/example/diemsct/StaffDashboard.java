package com.example.diemsct;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StaffDashboard extends Fragment {

    CardView ct,ct2;
    public StaffDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_staff_dash, container, false);
        ct = (CardView)view.findViewById(R.id.cv1);
        ct2 = (CardView)view.findViewById(R.id.cv2);
        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent redirect=new Intent(getActivity(),ClasstestView.class);
                //getActivity().startActivity(redirect);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fade_in,R.animator.fade_out)
                        .replace(R.id.login, new Student_list())
                        .commit();
            }
        });
        ct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent redirect=new Intent(getActivity(),ClasstestView.class);
                //getActivity().startActivity(redirect);
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fade_in,R.animator.fade_out)
                        .replace(R.id.login, new Attstud_list())
                        .commit();
            }
        });
        return view;
    }

}
