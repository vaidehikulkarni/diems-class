package org.diems.diemsapp;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import static android.view.View.GONE;

class MarksTeacherAdapter extends RecyclerView.Adapter<MarksTeacherAdapter.ViewHolder> {

    private List<MarksTeacher> list;
    private RecyclerView recyclerView;
    private Context context;
    private JSONArray marksArray;
    private FragmentManager fragmentManager;

    MarksTeacherAdapter(List<MarksTeacher> list, RecyclerView recyclerView, Context context, FragmentManager fragmentManager) {
        this.list = list;
        this.recyclerView = recyclerView;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_marks_teacher, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MarksTeacher obj = list.get(position);

        holder.name.setText(obj.name);
        holder.rollNo.setText(obj.rollNo);
        holder.belowAvg.setText(obj.belowAvg);

        final List<ClassTestMarks> marksList = new ArrayList<>();
        final ClassTestMarksAdapter adapter = new ClassTestMarksAdapter(marksList);
        holder.recyclerView.setAdapter(adapter);

        final RequestQueue requestQueue = Volley.newRequestQueue(context);

        //expand or collapse card
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(recyclerView);
                if (holder.expand.getVisibility() == GONE) {
                    //expand
                    holder.expand.setVisibility(View.VISIBLE);
                    holder.arrow.animate().rotation(180).start();

                    //send request
                    String url = MainActivity.IP + "/api/students/" + obj.id + "?access_token=" + MainActivity.userData.getAccessToken();
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                marksArray = new JSONObject(response).getJSONArray("marks");

                                marksList.clear();
                                for (int i = 0; i < marksArray.length(); i++) {
                                    JSONObject object = marksArray.getJSONObject(i);
                                    String ct1 = object.getJSONArray("class_test").getJSONObject(0).getString("obt_marks");
                                    String ct2 = object.getJSONArray("class_test").getJSONObject(1).getString("obt_marks");

                                    ClassTestMarks classTestMarks = new ClassTestMarks(object.getString("subject_name"), ct1, ct2);
                                    marksList.add(classTestMarks);
                                }

                                adapter.notifyDataSetChanged();
                                TransitionManager.beginDelayedTransition(recyclerView);
                                holder.progressBar.setVisibility(GONE);
                                holder.table.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });

                    requestQueue.add(stringRequest);

                } else {
                    //collapse
                    holder.expand.setVisibility(GONE);
                    holder.arrow.animate().rotation(0).start();
                }
            }
        });

        holder.viewGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog customDialog = new CustomDialog();
                Bundle bundle = new Bundle();
                bundle.putString("marks", marksArray.toString());
                customDialog.setArguments(bundle);
                customDialog.show(fragmentManager, "");
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, rollNo, belowAvg;
        ImageView arrow;
        FrameLayout expand;
        RecyclerView recyclerView;
        CardView cardView;
        LinearLayout table;
        ProgressBar progressBar;
        Button viewGraph;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            rollNo = (TextView) view.findViewById(R.id.rollNo);
            belowAvg = (TextView) view.findViewById(R.id.subText);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            expand = (FrameLayout) view.findViewById(R.id.expand);
            cardView = (CardView) view.findViewById(R.id.marksTeacherCard);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            table = (LinearLayout) view.findViewById(R.id.table);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            viewGraph = (Button) view.findViewById(R.id.viewGraph);

            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        }
    }

}
