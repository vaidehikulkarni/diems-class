package org.diems.diemsapp;

import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

class MarksTeacherAdapter extends RecyclerView.Adapter<MarksTeacherAdapter.ViewHolder> {

    private List<MarksTeacher> list;
    RecyclerView recyclerView;

    MarksTeacherAdapter(List<MarksTeacher> list, RecyclerView recyclerView) {
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_marks_teacher, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MarksTeacher obj = list.get(position);

        holder.name.setText(obj.name);
        holder.rollNo.setText(obj.rollNo);
        holder.belowAvg.setText(obj.belowAvg);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(recyclerView);
                if (holder.expand.getVisibility() == View.GONE) {
                    holder.expand.setVisibility(View.VISIBLE);
                    holder.arrow.animate().rotation(180).start();
                } else {
                    holder.expand.setVisibility(View.GONE);
                    holder.arrow.animate().rotation(0).start();
                }
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
        LinearLayout expand;
        CardView cardView;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            rollNo = (TextView) view.findViewById(R.id.rollNo);
            belowAvg = (TextView) view.findViewById(R.id.subText);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            expand = (LinearLayout) view.findViewById(R.id.expand);
            cardView = (CardView) view.findViewById(R.id.marksTeacherCard);
        }
    }

}
