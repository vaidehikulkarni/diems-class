package org.diems.diemsapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Students extends Fragment implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;

    public Students() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Student");
        MainActivity.navigationBarMenu.findItem(R.id.nav_student).setChecked(true);

        View view = inflater.inflate(R.layout.fragment_students, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        viewPager = (ViewPager) view.findViewById(R.id.pager);

        setupViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
      adapter.addFragment(new RedhatAcademy(), "Redhat Academy");
        adapter.addFragment(new MentorshipProg(), "Mentorship Programme");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
