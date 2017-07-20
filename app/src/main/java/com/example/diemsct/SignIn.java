package com.example.diemsct;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

import static android.R.attr.fragment;
import static android.R.attr.spinnerMode;


public class SignIn extends Fragment {
    Button cont;
    MaterialBetterSpinner sp;
    String[] logn={"Student Login","Staff Login"};
    ArrayAdapter aa;
    public SignIn() {
        // Required empty public constructor
    }

    int post= -1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);
        cont = (Button)view.findViewById(R.id.btncont);
        sp = (MaterialBetterSpinner) view.findViewById(R.id.spinner);
        aa = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,logn);
        sp.setAdapter(aa);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                post = -1;
                if(position==0){
                    post=0;
                }
                if (position==1){
                    post=1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(sp.getText().toString().equals("Student Login"))
            {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new Stud_Activity())
                        .commit();
            }
            else {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new Staff_Activity())
                        .commit();
            }
            }
        });

        return view;
    }

}
