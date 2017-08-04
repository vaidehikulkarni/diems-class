package com.example.diemsct;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


public class SignInFragment extends Fragment {
    Button cont;
    MaterialBetterSpinner sp;
    MaterialEditText username, password;
    String[] login = {"Student Login", "Staff Login", "Admin Login"};
    ArrayAdapter aa;
    FragmentManager manager;
    TextView error;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Sign In");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        cont = (Button) view.findViewById(R.id.btncont);
        sp = (MaterialBetterSpinner) view.findViewById(R.id.spinner);
        username = (MaterialEditText) view.findViewById(R.id.username);
        password = (MaterialEditText) view.findViewById(R.id.password);
        error = (TextView) view.findViewById(R.id.error);

        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, login);
        sp.setAdapter(aa);
        manager = getFragmentManager();

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!username.getText().toString().matches(".+"))
                        username.setError("Username is required");
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!password.getText().toString().matches(".+"))
                        password.setError("Password is required");
                }
            }
        });

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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                FragmentTransaction transaction = manager.beginTransaction().setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
                if (sp.getText().toString().equals("Student Login")) {
                    transaction
                            .replace(R.id.login, new StudentDashboard())
                            .addToBackStack(null)
                            .commit();
                    MainActivity.loginType = "student";
                } else if (sp.getText().toString().equals("Staff Login")) {
                    transaction
                            .replace(R.id.login, new StaffDashboard())
                            .addToBackStack(null)
                            .commit();
                    MainActivity.loginType = "staff";
                }
                else if (sp.getText().toString().equals("Admin Login")) {
                    transaction
                            .replace(R.id.login, new AdminDashBoard())
                            .addToBackStack(null)
                            .commit();
                    MainActivity.loginType = "admin";
                }
                else {
                    error.setVisibility(View.VISIBLE);
                    MainActivity.loginType = "";
                }
                username.requestFocus();
                username.clearFocus();
                password.requestFocus();
                password.clearFocus();
                if (getActivity().getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                MainActivity.checksignin();
            }
        });

        return view;
    }

}