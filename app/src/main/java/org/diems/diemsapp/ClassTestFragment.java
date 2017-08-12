package org.diems.diemsapp;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ClassTestFragment extends Fragment {

    BarChart barChart;

    public ClassTestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Class Test");
        MainActivity.navigationBarMenu.findItem(R.id.nav_class_test).setChecked(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classtest_view, container, false);

        barChart = (BarChart)view.findViewById(R.id.barchart);

        ArrayList<BarEntry> classTest1 = new ArrayList<>();
        classTest1.add(new BarEntry(14f, 0));
        classTest1.add(new BarEntry(18f, 1));
        classTest1.add(new BarEntry(16f, 2));
        classTest1.add(new BarEntry(12f, 3));
        classTest1.add(new BarEntry(19f, 4));

        ArrayList<BarEntry> classTest2 = new ArrayList<>();
        classTest2.add(new BarEntry(9f, 0));
        classTest2.add(new BarEntry(10f, 1));
        classTest2.add(new BarEntry(18f, 2));
        classTest2.add(new BarEntry(20f, 3));
        classTest2.add(new BarEntry(9f, 4));

        BarDataSet barDataSet1 = new BarDataSet(classTest1, "CT-1");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(classTest2, "CT-2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("MV");
        theDates.add("PCD");
        theDates.add("PDC");
        theDates.add("DWDM");
        theDates.add("CC");

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        BarData data = new BarData(theDates, dataSets);
        barChart.setData(data);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);

        return view;
    }

}
