package com.example.diemsct;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ViewFlipper viewFlipper;

    public HomeFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("DIEMS");
        MainActivity.navigationBarMenu.findItem(R.id.nav_home).setChecked(true);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        // init();
        viewFlipper = (ViewFlipper) view.findViewById(R.id.view_flipper);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();
       viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.fade_in));
        // viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.fade_out));

        return view;
    }
}
