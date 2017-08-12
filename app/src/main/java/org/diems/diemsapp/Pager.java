package org.diems.diemsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;


public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    private int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return new AcdFE();
            case 1:
                return new AcdCse();
            case 2:
                return new AcdCivil();
            case 3:
                return new AcdMech();
            case 4:
                return new AcdEtc();
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}