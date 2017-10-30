package org.diems.diemsapp;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends DialogFragment {

    JSONArray marksArray;

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
    }

    public CustomDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.layout_custom_dialog, container, false);

        try {
            marksArray = new JSONArray(getArguments().getString("marks"));

            BarChart chart = (BarChart) view.findViewById(R.id.chart);
            chart.getAxisLeft().setAxisMaxValue(45f);
            BarData data = new BarData(getXAxisValues(), getDataSet());
            chart.setData(data);
            // chart.setDescription("Class Test Marks");
            chart.animateY(2000);
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
            chart.setScaleEnabled(false);
            chart.setDescription(null);
            chart.setDoubleTapToZoomEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private List<BarDataSet> getDataSet() throws JSONException {
        ArrayList<BarDataSet> dataSets;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int i = 0; i < marksArray.length(); i++) {
            try {
                BarEntry barEntry = new BarEntry(Float.valueOf(marksArray
                        .getJSONObject(i)
                        .getJSONArray("class_test")
                        .getJSONObject(0)
                        .getString("obt_marks")), i);

                valueSet1.add(barEntry);
            } catch (NumberFormatException e) {
                valueSet1.add(new BarEntry(0f, i));
            }
        }

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        for (int i = 0; i < marksArray.length(); i++) {
            try {
                BarEntry barEntry = new BarEntry(Float.valueOf(marksArray
                        .getJSONObject(i)
                        .getJSONArray("class_test")
                        .getJSONObject(1)
                        .getString("obt_marks")), i);

                valueSet2.add(barEntry);
            } catch (NumberFormatException e) {
                valueSet2.add(new BarEntry(0f, i));
            }
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "ClassTest 1");
        int color1 = getMaterialColor("A400");
        barDataSet1.setColor(color1);
        barDataSet1.setValueTextSize(12f);
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "ClassTest 2");
        int color2;
        do {
            color2 = getMaterialColor("A400");
        }
        while (color2 == color1);
        barDataSet2.setColor(color2);
        barDataSet2.setValueTextSize(12f);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet1);

        return dataSets;
    }

    private List<String> getXAxisValues() throws JSONException {
        ArrayList<String> xAxis = new ArrayList<>();

        for (int i = 0; i < marksArray.length(); i++) {
            String subject = marksArray.getJSONObject(i).getString("subject_name");
            xAxis.add(subject);
        }

        return xAxis;
    }

    private int getMaterialColor(String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }
}
