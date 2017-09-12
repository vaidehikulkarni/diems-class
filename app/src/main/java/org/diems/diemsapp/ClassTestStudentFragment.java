package org.diems.diemsapp;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import java.util.ArrayList;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.Arrays;
import java.util.List;

public class ClassTestStudentFragment extends Fragment {

  //  BarChart barChart;
  List<Integer> colors,colors1;
    public ClassTestStudentFragment() {
        // Required empty public constructor
    }
    LineChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Class Test");
        MainActivity.navigationBarMenu.findItem(R.id.nav_class_test).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_classtest_view, container, false);
        BarChart chart = (BarChart) view.findViewById(R.id.chart);
        chart.getAxisLeft().setAxisMaxValue(45f);
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
       // chart.setDescription("Class Test Marks");
        chart.animateXY(0,2000);
        chart.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        chart.invalidate();

        LimitLine lower_limit = new LimitLine(24f, "Min Marks");
        lower_limit.setLineWidth(2f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
      lower_limit.setTextSize(10f);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setDrawLimitLinesBehindData(true);
        chart.getAxisRight().setEnabled(false);
       return view;
    }

    private List<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;
//        colors = Arrays.asList(new Integer[]{Color.rgb(0, 255,0),Color.rgb(0, 255,0),
//                Color.rgb(0, 255,0),Color.rgb(0, 255,0),Color.rgb(0, 255,0),Color.rgb(0, 255,0)});
//        colors1 = Arrays.asList(new Integer[]{Color.rgb(0, 0, 255),Color.rgb(0, 0, 255),
//                Color.rgb(0, 0, 255),Color.rgb(0, 0, 255),Color.rgb(0, 0, 255),Color.rgb(0, 0, 255)});

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarEntry v1e1 = new BarEntry(0f, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(23f, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(31f, 2);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(25f, 3);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(34f, 4);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(22.000f, 5);
        valueSet1.add(v1e6);

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(0.000f, 0);
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(29.000f, 1);
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(30.000f, 2);
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(36.000f, 3);
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(12.000f, 4);
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(31.000f, 5);
        valueSet2.add(v2e6);


//        for (int i=0;i<6;i++){
//
//            if(valueSet1.get(i).getVal()<=24){
//                colors.set(i,Color.rgb(150,180,0));
//            }
//            if(valueSet2.get(i).getVal()<=24){
//                colors1.set(i,Color.rgb(0,0,180));
//            }
//        }


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "ClassTest 1");
        barDataSet1.setColor(getResources().getColor(R.color.colorAccent));
        barDataSet1.setValueTextSize(12f);
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "ClassTest 2");
      // barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet2.setColor(getResources().getColor(R.color.purple));
        barDataSet2.setValueTextSize(12f);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);

        return dataSets;
    }

    private List<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Subject");
        xAxis.add("Subject");
        xAxis.add("Subject");
        xAxis.add("Subject");
        xAxis.add("Subject");
        xAxis.add("Subject");
        return xAxis;
    }
}
