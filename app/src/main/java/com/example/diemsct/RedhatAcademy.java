package com.example.diemsct;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class RedhatAcademy extends Fragment {
ImageView imageView;

    public RedhatAcademy() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_redhat_academy, container, false);

     imageView=(ImageView) view.findViewById(R.id.imageview2);

imageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),RedhatVideo.class);
        startActivity(intent);
    }
});
return view;
    }
}




