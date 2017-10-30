package org.diems.diemsapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ClassTestList extends Fragment {

    RecyclerView recyclerView;
    List<MarksTeacher> marksTeacherList;
    List<MarksSubject> marksSubjectList;
    MarksTeacherAdapter marksTeacherAdapter;
    MarksSubjectAdapter marksSubjectAdapter;
    RequestQueue requestQueue;
    ProgressBar progressBar;

    public ClassTestList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_class_test_list, container, false);

        Bundle bundle = getArguments();
        requestQueue = Volley.newRequestQueue(getActivity());
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.classTestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        marksTeacherList = new ArrayList<>();
        marksSubjectList = new ArrayList<>();

        marksTeacherAdapter = new MarksTeacherAdapter(marksTeacherList, recyclerView, getActivity(), getFragmentManager());
        marksSubjectAdapter = new MarksSubjectAdapter(marksSubjectList, recyclerView);

        if (bundle.getString("type").equals("teacher"))
            recyclerView.setAdapter(marksTeacherAdapter);
        else
            recyclerView.setAdapter(marksSubjectAdapter);

        String url;
        if (bundle.getString("type").equals("teacher")) {

            if (bundle.getString("name").equals("my_class"))
                url = MainActivity.IP + "/api/staff/class?access_token=" + MainActivity.userData.getAccessToken();
            else
                url = MainActivity.IP + "/api/staff/mentees?access_token=" + MainActivity.userData.getAccessToken();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                        JSONArray jsonArray = new JSONObject(response).getJSONArray("students");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            marksTeacherList.add(new MarksTeacher(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("roll_no"),
                                    jsonObject.getString("below_avg_subjects")
                            ));
                        }

                        marksTeacherAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            requestQueue.add(stringRequest);
        } else if (bundle.getString("type").equals("subject")) {
            url = MainActivity.IP + "/api/staff/subjects/" + bundle.getString("id") + "?access_token=" + MainActivity.userData.getAccessToken();

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    try {
                        JSONArray jsonArray = new JSONObject(response).getJSONArray("students");
                        marksSubjectList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            marksSubjectList.add(new MarksSubject(
                                    object.getString("name"),
                                    object.getString("roll_no"),
                                    object.getJSONArray("marks").getJSONObject(0).getString("obt_marks"),
                                    object.getJSONArray("marks").getJSONObject(1).getString("obt_marks"),
                                    object.getString("ct_avg"),
                                    object.getString("class"),
                                    object.getString("branch"),
                                    object.getString("division")
                            ));
                        }

                        marksSubjectAdapter.notifyDataSetChanged();
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
        }

        return view;
    }
}
