package org.diems.diemsapp;


import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    //    private static ViewPager mPager;
//    private static int currentPage = 0;
    ViewFlipper viewFlipper;
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
        viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in));
        viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out));
        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                viewFlipper.showNext();
                return false;
            }
        });

        TextView visiondiems = (TextView) view.findViewById(R.id.vision_diems);
        visiondiems.setText(Html.fromHtml(getString(R.string.visiondiems)));
        TextView missiondiems = (TextView) view.findViewById(R.id.mission_diems);
        missiondiems.setText(Html.fromHtml(getString(R.string.missiondiems)));

        return view;
    }
}
