package com.example.diemsct;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private ArrayList<String> students;

    public DataAdapter(ArrayList<String> students) {
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tv_country.setText(students.get(i));
   //     viewHolder.name.setText(students.get(i));
     //   viewHolder.attendance.setText(students.get(i));
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

  /*  public void addItem(String country) {
        students.add(country);
        notifyItemInserted(students.size());
    }

    public void removeItem(int position) {
        students.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, students.size());
    }*/
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_country,name,attendance;
        public ViewHolder(View view) {
            super(view);

            tv_country = (TextView) view.findViewById(R.id.tv_country);
        //    name = view.findViewById(R.id.name);
          //  attendance = view.findViewById(R.id.attendance);
        }
    }
}