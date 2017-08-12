package org.diems.diemsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

public class studPager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int tabCount;

    //Constructor to the class
    public studPager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return new RedhatAcademy();
            case 1:
                return new MentorshipProg();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
