package com.example.diemsct;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInFragment extends Fragment {
    private Button cont;
    private MaterialBetterSpinner sp;
    private MaterialEditText username, password;
    private String[] login = {"Student Login", "Staff Login", "Admin Login"};
    private ArrayAdapter aa;
    private FragmentManager manager;
    private TextView error;
    private String userType = "";
    private ProgressDialog dialog;
    private boolean responseRecieved;

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
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    if (getActivity().getCurrentFocus() != null)
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    if (!validate())
                        return;

                    responseRecieved = false;
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Loading");
                    dialog.show();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!responseRecieved)
                            {
                                dialog.dismiss();
                            }
                        }
                    },15000);

                    JSONObject json = new JSONObject();
                    json.put("username", username.getText().toString());
                    json.put("password", password.getText().toString());

                    String url = getString(R.string.IP) + "/login";
                    JsonObjectRequest json_request = new JsonObjectRequest(url, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                responseRecieved = true;
                                dialog.dismiss();
//                                if (response.getString("status").equals("202")) {
                                    FragmentTransaction transaction = manager
                                            .beginTransaction()
                                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
//                                            .addToBackStack(null);
                                    switch (userType) {
                                        case "student":
                                            transaction
                                                    .replace(R.id.login, new StudentDashboard())
                                                    .commit();
                                            break;
                                        case "staff":
                                            transaction
                                                    .replace(R.id.login, new StaffDashboard())
                                                    .commit();
                                            break;
                                        case "admin":
                                            Bundle bundle = new Bundle();
                                            AdminDashBoard object = new AdminDashBoard();
                                            bundle.putString("dept", "CSE");
                                            object.setArguments(bundle);
                                            transaction
                                                    .replace(R.id.login, object)
                                                    .commit();
                                            break;
                                    }
                                    MainActivity.loginType = userType;
                                    MainActivity.accessToken = response.getString("access_token");
                                    MainActivity.checksignin();

//                                } else {
//                                    error.setText(response.getString("error"));
//                                    error.setVisibility(View.VISIBLE);
//                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    Volley.newRequestQueue(getActivity()).add(json_request);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (getView() != null)
                        Snackbar.make(getView(), "Error", 2000);
                }
            }
        });

        return view;
    }

    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getActivity().getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    boolean validate() {
        boolean cont = true;
        if (username.getText().toString().trim().equals("")) {
            username.setError("Username is required");
            cont = false;
        }

        if (password.getText().toString().trim().equals("")) {
            password.setError("Password is required");
            cont = false;
        }

        switch (sp.getText().toString()) {
            case "Student Login":
                userType = "student";
                error.setVisibility(View.GONE);
                break;
            case "Staff Login":
                userType = "staff";
                error.setVisibility(View.GONE);
                break;
            case "Admin Login":
                userType = "admin";
                error.setVisibility(View.GONE);
                break;
            case "":
            default:
                error.setVisibility(View.VISIBLE);
                cont = false;
        }

        return cont;
    }
}