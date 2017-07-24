package com.example.diemsct;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class SignIn extends Fragment {
    Button cont;
    MaterialBetterSpinner sp;
    String[] login={"Student Login","Staff Login"};
    ArrayAdapter aa;
    public SignIn() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);
        cont = (Button)view.findViewById(R.id.btncont);
        sp = (MaterialBetterSpinner) view.findViewById(R.id.spinner);
        aa = new ArrayAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line,login);
        sp.setAdapter(aa);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = (String) ((AppCompatActivity)getActivity()).getSupportActionBar().getTitle();
                MainActivity.signedin = true;
                if(sp.getText().toString().equals("Student Login"))
                {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login, new StudentDashboard())
                            .commit();
                    title = "Dashboard";
                    MainActivity.loginType = "student";
                }
                else if(sp.getText().toString().equals("Staff Login")) {
                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.login, new StaffDashboard())
                            .commit();
                    title = "Dashboard";
                    MainActivity.loginType = "staff";
                }
                else
                {
                    Toast.makeText(getActivity(), "Please select user", Toast.LENGTH_SHORT).show();
                    MainActivity.signedin = false;
                }
                ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
                MainActivity.checksignin();
            }
        });

        return view;
    }

}
