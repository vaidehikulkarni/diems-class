package com.example.diemsct;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphView extends AppCompatActivity {

    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_view);

        barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> classTest1 = new ArrayList<BarEntry>();
        classTest1.add(new BarEntry(14f, 0));
        classTest1.add(new BarEntry(18f, 1));
        classTest1.add(new BarEntry(16f, 2));
        classTest1.add(new BarEntry(12f, 3));
        classTest1.add(new BarEntry(19f, 4));

        ArrayList<BarEntry> classTest2 = new ArrayList<BarEntry>();
        classTest2.add(new BarEntry(9f, 0));
        classTest2.add(new BarEntry(10f, 1));
        classTest2.add(new BarEntry(18f, 2));
        classTest2.add(new BarEntry(20f, 3));
        classTest2.add(new BarEntry(9f, 4));

        BarDataSet barDataSet1 = new BarDataSet(classTest1, "CT-1");
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(classTest2, "CT-2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<String> theDates = new ArrayList<String>();
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
//        BarData gasfk = BarData()

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }
}
