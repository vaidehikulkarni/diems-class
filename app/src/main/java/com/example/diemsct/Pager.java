package com.example.diemsct;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;


public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

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
                AcdFE tab1 = new AcdFE();
                return tab1;
            case 1:
                AcdCse tab2 = new AcdCse();
                return tab2;
            case 2:
                AcdCivil tab3 = new AcdCivil();
                return tab3;
            case 3:
                AcdMech tab4 = new AcdMech();
                return tab4;
            case 4:
                AcdEtc tab5 = new AcdEtc();
                return tab5;
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