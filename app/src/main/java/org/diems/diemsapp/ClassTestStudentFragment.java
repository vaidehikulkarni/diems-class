package org.diems.diemsapp;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ClassTestStudentFragment extends Fragment {

  //  BarChart barChart;

    public ClassTestStudentFragment() {
        // Required empty public constructor
    }

    LineChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Class Test");
        MainActivity.navigationBarMenu.findItem(R.id.nav_class_test).setChecked(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classtest_view, container, false);
        chart= (LineChart) view.findViewById(R.id.chart);
        // add data
        setData();

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
//        barChart = (BarChart)view.findViewById(R.id.barchart);
//
//        ArrayList<BarEntry> classTest1 = new ArrayList<>();
//        classTest1.add(new BarEntry(14f, 0));
//        classTest1.add(new BarEntry(18f, 1));
//        classTest1.add(new BarEntry(16f, 2));
//        classTest1.add(new BarEntry(12f, 3));
//        classTest1.add(new BarEntry(19f, 4));
//
//        ArrayList<BarEntry> classTest2 = new ArrayList<>();
//        classTest2.add(new BarEntry(9f, 0));
//        classTest2.add(new BarEntry(10f, 1));
//        classTest2.add(new BarEntry(18f, 2));
//        classTest2.add(new BarEntry(20f, 3));
//        classTest2.add(new BarEntry(9f, 4));
//
//        BarDataSet barDataSet1 = new BarDataSet(classTest1, "CT-1");
//        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        BarDataSet barDataSet2 = new BarDataSet(classTest2, "CT-2");
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        ArrayList<String> theDates = new ArrayList<>();
//        theDates.add("MV");
//        theDates.add("PCD");
//        theDates.add("PDC");
//        theDates.add("DWDM");
//        theDates.add("CC");
//
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
//
//        BarData data = new BarData(theDates, dataSets);
//        barChart.setData(data);
//        barChart.setTouchEnabled(true);
//        barChart.setDragEnabled(true);
//        barChart.setScaleEnabled(true);

        return view;
    }
    private ArrayList<String> setXAxisValues(){
        ArrayList<String> Subjects = new ArrayList<String>();
        Subjects.add("");Subjects.add("Subject1");
        Subjects.add("Sub2");
        Subjects.add("Sub3");
        Subjects.add("Sub4");
        Subjects.add("Sub5");
        Subjects.add("Sub6");
        Subjects.add("");

        return Subjects;
    }
    private ArrayList<Entry> Data(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        // yVals.add(new Entry(, 0));
        yVals.add(new Entry(25, 1));
        yVals.add(new Entry(30, 2));
        yVals.add(new Entry(10, 3));
        yVals.add(new Entry(35, 4));
        yVals.add(new Entry(10, 5));
        yVals.add(new Entry(12, 6));
        return yVals;
    }
    private ArrayList<Entry> Data2(){
        ArrayList<Entry> yVal = new ArrayList<Entry>();
//        yVal.add(new Entry(10, 0));
        yVal.add(new Entry(20, 1));
        yVal.add(new Entry(35, 2));
        yVal.add(new Entry(15, 3));
        yVal.add(new Entry(30, 4));
        yVal.add(new Entry(20, 5));
        yVal.add(new Entry(12, 6));
        return yVal;
    }
    private void setData() {
        ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> Data1=Data();
        ArrayList<Entry> Data2=Data2();
        LineDataSet set1,set2;
        set1 = new LineDataSet(Data1, "Classtest 1");
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(false);
        set2 = new LineDataSet(Data2, "Classtest 2");
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);
        set2.setLineWidth(1f);
        set2.setCircleRadius(3f);
        set2.setDrawCircleHole(false);
        set2.setValueTextSize(9f);
        set2.setDrawFilled(false);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        LineData data = new LineData(xVals, dataSets);

//        LimitLine upper_limit = new LimitLine(40, "Maximum Marks");
//        upper_limit.setLineWidth(4f);
//        upper_limit.enableDashedLine(10f, 10f, 0f);
//        upper_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        upper_limit.setTextSize(10f);

        LimitLine lower_limit = new LimitLine(24, "Minimum Marks :24");
        lower_limit.setLineWidth(4f);
        lower_limit.enableDashedLine(10f, 10f, 0f);
        lower_limit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lower_limit.setTextSize(10f);

        YAxis leftAxis = chart.getAxisLeft();
// reset all limit lines to avoid overlapping lines
        leftAxis.removeAllLimitLines();
        // leftAxis.addLimitLine(upper_limit);
        leftAxis.addLimitLine(lower_limit);
        leftAxis.setAxisMaxValue(40);
        leftAxis.setAxisMinValue(0);
//leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);
//        XAxis RightAxis =chart.getXAxis();
        // RightAxis.setDrawAxisLine(true);
// limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        chart.getAxisRight().setEnabled(false);

        chart.setDescription("Class Test Graph");
        chart.setNoDataTextDescription("You need to provide data for the chart.");
        // set data
        chart.setData(data);

    }
}
