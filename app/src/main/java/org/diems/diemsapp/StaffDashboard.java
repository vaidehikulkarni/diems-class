package org.diems.diemsapp;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StaffDashboard extends Fragment {

    CardView ct,ct2;
    FragmentTransaction transaction;
    public StaffDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Dashboard");
        MainActivity.navigationBarMenu.findItem(R.id.nav_dashboard).setChecked(true);
        transaction = getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in,R.animator.fade_out)
                .addToBackStack(null);

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_staff_dashboard, container, false);
        ct = (CardView)view.findViewById(R.id.cv1);
        ct2 = (CardView)view.findViewById(R.id.cv2);
        ct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent redirect=new Intent(getActivity(),ClassTestFragment.class);
                //getActivity().startActivity(redirect);
                transaction
                        .replace(R.id.login, new AttendanceFragment())
                        .commit();
            }
        });
        ct2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent redirect=new Intent(getActivity(),ClassTestFragment.class);
                //getActivity().startActivity(redirect);
                transaction
                        .replace(R.id.login, new AttendanceFragment())
                        .commit();
            }
        });
        return view;
    }

}
