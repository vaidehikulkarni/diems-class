package org.diems.diemsapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassTestMarksAdapter extends RecyclerView.Adapter<ClassTestMarksAdapter.ViewHolder>{

    List<ClassTestMarks> list;

    public ClassTestMarksAdapter(List<ClassTestMarks> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_test_marks_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassTestMarks object = list.get(position);
        holder.subject.setText(object.subject);
        holder.marks1.setText(object.marks1);
        holder.marks2.setText(object.marks2);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView subject, marks1, marks2;

        ViewHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.subject);
            marks1 = (TextView) itemView.findViewById(R.id.marks1);
            marks2 = (TextView) itemView.findViewById(R.id.marks2);
        }
    }
}
