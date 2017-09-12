package org.diems.diemsapp;

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

import static android.view.View.GONE;

class MarksSubjectAdapter extends RecyclerView.Adapter<MarksSubjectAdapter.ViewHolder> {

    List<MarksSubject> list;
    private RecyclerView recyclerView;

    MarksSubjectAdapter(List<MarksSubject> list, RecyclerView recyclerView) {
        this.list = list;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_marks_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MarksSubject marksSubject = list.get(position);

        holder.name.setText(marksSubject.name);
        holder.rollNo.setText(marksSubject.rollNo);
        holder.subText.setText(marksSubject._class + " " + marksSubject.branch + " " + marksSubject.division);
        holder.marks_ct1.setText(marksSubject.marks_ct1);
        holder.marks_ct2.setText(marksSubject.marks_ct2);
        holder.marks_average.setText(marksSubject.marks_average);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(recyclerView);
                if(holder.expand.getVisibility() == View.GONE)
                {
                    holder.expand.setVisibility(View.VISIBLE);
                    holder.arrow.animate().rotation(180).start();
                }
                else
                {
                    holder.expand.setVisibility(GONE);
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

        TextView name, rollNo, marks_ct1, marks_ct2, marks_average, subText;
        LinearLayout expand;
        ImageView arrow;
        CardView cardView;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            rollNo = (TextView) view.findViewById(R.id.rollNo);
            marks_ct1 = (TextView) view.findViewById(R.id.marks_ct1);
            marks_ct2 = (TextView) view.findViewById(R.id.marks_ct2);
            marks_average = (TextView) view.findViewById(R.id.marks_average);
            subText = (TextView) view.findViewById(R.id.subText);
            expand = (LinearLayout) view.findViewById(R.id.expand);
            arrow = (ImageView) view.findViewById(R.id.arrow);
            cardView = (CardView) view.findViewById(R.id.marksSubjectCard);
        }
    }
}
