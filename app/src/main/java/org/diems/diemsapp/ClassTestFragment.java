package org.diems.diemsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ClassTestFragment extends Fragment {

    ProgressBar progressBar;
    LinearLayout cards;
    CardView myClass, myMentees, subject1, subject2, subject3;
    TextView totalStudentsClass, totalStudentsMentees, subjectName1, subjectName2, subjectName3, belowAverage1, belowAverage2, belowAverage3;
    RequestQueue requestQueue;

    public ClassTestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_test, container, false);

        MainActivity.actionBar.setTitle("Class Test");
        MainActivity.navigationBarMenu.findItem(R.id.nav_class_test).setChecked(true);

        //Initialise views
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        cards = (LinearLayout) view.findViewById(R.id.cards);
        myClass = (CardView) view.findViewById(R.id.myClass);
        myMentees = (CardView) view.findViewById(R.id.myMentees);
        subject1 = (CardView) view.findViewById(R.id.subject1);
        subject2 = (CardView) view.findViewById(R.id.subject2);
        subject3 = (CardView) view.findViewById(R.id.subject3);
        totalStudentsClass = (TextView) view.findViewById(R.id.totalStudentsClass);
        totalStudentsMentees = (TextView) view.findViewById(R.id.totalStudentsMentees);
        subjectName1 = (TextView) view.findViewById(R.id.subjectName1);
        subjectName2 = (TextView) view.findViewById(R.id.subjectName2);
        subjectName3 = (TextView) view.findViewById(R.id.subjectName3);
        belowAverage1 = (TextView) view.findViewById(R.id.belowAverage1);
        belowAverage2 = (TextView) view.findViewById(R.id.belowAverage2);
        belowAverage3 = (TextView) view.findViewById(R.id.belowAverage3);

        //Initialise request queue
        requestQueue = Volley.newRequestQueue(getActivity());

        myClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition((ViewGroup) getActivity().findViewById(R.id.login));
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new ClassTestList())
                        .commit();
            }
        });

        String url = MainActivity.IP + "/api/staff?access_token=" + MainActivity.accessToken;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Handle response
                try {
                    JSONObject res = new JSONObject(response);

                    progressBar.setVisibility(View.GONE);
                    cards.setVisibility(View.VISIBLE);

                    if (!res.isNull("my_class"))
                    {
                        //my_class exists
                        JSONObject my_class = res.getJSONObject("my_class");
                        totalStudentsClass.setText(my_class.getString("total_students"));
                        myClass.setVisibility(View.VISIBLE);
                    }

                    if (!res.isNull("my_mentees"))
                    {
                        //my_mentees exists
                        JSONObject my_mentees = res.getJSONObject("my_mentees");
                        totalStudentsMentees.setText(my_mentees.getString("total_students"));
                        myMentees.setVisibility(View.VISIBLE);
                    }

                    switch (res.getJSONArray("my_subjects").length())
                    {
                        case 0:
                            break;

                        case 3:
                            JSONObject object2 = res.getJSONArray("my_subjects").getJSONObject(2);
                            subjectName3.setText(object2.getString("subject_name"));
                            belowAverage3.setText(object2.getString("below_avg_test_1"));
                            subject3.setVisibility(View.VISIBLE);
                        case 2:
                            JSONObject object1 = res.getJSONArray("my_subjects").getJSONObject(1);
                            subjectName2.setText(object1.getString("subject_name"));
                            belowAverage2.setText(object1.getString("below_avg_test_1"));
                            subject2.setVisibility(View.VISIBLE);
                        case 1:
                            JSONObject object0 = res.getJSONArray("my_subjects").getJSONObject(0);
                            subjectName1.setText(object0.getString("subject_name"));
                            belowAverage1.setText(object0.getString("below_avg_test_1"));
                            subject1.setVisibility(View.VISIBLE);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Handle error
            }
        });

        //Add retry policy to request
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Add request to queue
        requestQueue.add(stringRequest);

        return view;
    }
}
