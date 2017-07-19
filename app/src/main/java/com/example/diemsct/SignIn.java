package com.example.diemsct;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment {

    MaterialBetterSpinner sp;
    String[] logn={"Student Login","Staff Login"};
    ArrayAdapter aa;
    public SignIn() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);

        sp = (MaterialBetterSpinner) view.findViewById(R.id.spinner);
        aa = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,logn);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(aa);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
           // if(position==0)

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                return view;
    }

}
