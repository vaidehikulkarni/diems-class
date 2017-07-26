package com.example.diemsct;


import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] Pics= {R.drawable.diems1,R.drawable.diems2,R.drawable.diems3,R.drawable.place,R.drawable.diems5};
    private ArrayList<Integer> PicsArray = new ArrayList<Integer>();

    public HomeFragment() {
        // Required empty public constructor
    }

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return view;
    }

    private void init() {
        for(int i=0;i<Pics.length;i++)
            PicsArray.add(Pics[i]);

        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new MyAdapter(getActivity(),PicsArray));
      //  CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
       // indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == Pics.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);
    }
}
