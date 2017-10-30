package org.diems.diemsapp;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;

import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class ClassTestStudentFragment extends Fragment {

    JSONArray marksArray;
    LinearLayout content;
    RequestQueue requestQueue;
    ProgressBar progressBar;
    TextView name, rollNo;
    BarChart chart;

    public ClassTestStudentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.actionBar.setTitle("Class Test");
        MainActivity.navigationBarMenu.findItem(R.id.nav_class_test).setChecked(true);
        View view = inflater.inflate(R.layout.fragment_classtest_view, container, false);
        content = (LinearLayout) view.findViewById(R.id.content);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        name = (TextView) view.findViewById(R.id.name);
        rollNo = (TextView) view.findViewById(R.id.rollNo);
        chart = (BarChart) view.findViewById(R.id.chart);
        requestQueue = Volley.newRequestQueue(getActivity());

        String url = MainActivity.IP + "/api/students/marks?access_token=" + MainActivity.userData.getAccessToken();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    if(res.getString("status").equals("200")) {
                        marksArray = res.getJSONArray("marks");
                        name.setText(res.getString("name"));
                        rollNo.setText(res.getString("roll_no"));
                        createChart(chart);
                        content.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(getActivity(), res.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(stringRequest);
        return view;
    }

    private  void  createChart(BarChart chart) {
        chart.getAxisLeft().setAxisMaxValue(45f);
        BarData data = null;
        try {
            data = new BarData(getXAxisValues(), getDataSet());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private int getMaterialColor(String typeColor)
    {
        int returnColor = Color.BLACK;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", getActivity().getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }
}
