package com.example.diemsct;


import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //    private static ViewPager mPager;
//    private static int currentPage = 0;
    ViewFlipper viewFlipper;
    TextView visiondiems,aboutDiems, missiondiems;
//    private static final Integer[] Pics= {R.drawable.diems1,R.drawable.diems2,R.drawable.diems3,R.drawable.place,R.drawable.diems5};
//    private ArrayList<Integer> PicsArray = new ArrayList<Integer>();

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
        visiondiems = (TextView) view.findViewById(R.id.vision_diems);
        visiondiems.setText(Html.fromHtml(getString(R.string.visiondiems)));
        missiondiems = (TextView) view.findViewById(R.id.mission_diems);
        missiondiems.setText(Html.fromHtml(getString(R.string.missiondiems)));



        return view;
    }

//    private void init() {
//        for(int i=0;i<Pics.length;i++)
//            PicsArray.add(Pics[i]);
//
//        mPager = (ViewPager) view.findViewById(R.id.pager);
//        mPager.setAdapter(new MyAdapter(getActivity(),PicsArray));
//      //  CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
//       // indicator.setViewPager(mPager);
//
//        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == Pics.length) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
//    }
}
