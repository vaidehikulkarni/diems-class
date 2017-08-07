package com.example.diemsct;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdminDashBoard extends Fragment {

    CardView cardView;
    FloatingActionButton floatingActionButton;

    public AdminDashBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.navigationBarMenu.findItem(R.id.nav_admin_dashboard).setChecked(true);
        MainActivity.actionBar.setTitle("Dashboard");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false);
        cardView = (CardView) view.findViewById(R.id.cv1);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.login, new UploadNotice())
                        .addToBackStack(null)
                        .commit();
            }
        });
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.login, new UploadNotice())
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

}
