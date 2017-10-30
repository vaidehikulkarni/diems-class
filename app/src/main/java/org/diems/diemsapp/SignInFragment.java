package org.diems.diemsapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class SignInFragment extends Fragment {
    private Button cont;
    private Spinner sp;
    private EditText username, password;
    private String[] login = {"Student Login", "Staff Login", "Admin Login"};
    private ArrayAdapter aa;
    private FragmentManager manager;
    private TextView error;
    private String userType = "";
    private ProgressDialog dialog;
    private TextInputLayout usernameLayout, passwordLayout;
    JsonObjectRequest json_request;
    RequestQueue requestQueue;
    Handler handler;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainActivity.actionBar.setTitle("Sign In");
        for (int i = 0; i < MainActivity.navigationBarMenu.size(); i++) {
            MainActivity.navigationBarMenu.getItem(i).setChecked(false);
        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        usernameLayout = (TextInputLayout) view.findViewById(R.id.usernameLayout);
        passwordLayout = (TextInputLayout) view.findViewById(R.id.passwordLayout);
        cont = (Button) view.findViewById(R.id.btncont);
        sp = (Spinner) view.findViewById(R.id.spinner);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        error = (TextView) view.findViewById(R.id.error);
        requestQueue = Volley.newRequestQueue(getActivity());

        aa = new ArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, login);
        sp.setAdapter(aa);
        manager = getFragmentManager();

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

                    //Bypass login for testing
//                    if (sp.getText().toString().equals("Staff Login")) {
//                        getFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.login, new ClassTestFragment())
//                                .commit();
//                        return;
//                    }
                    dialog = new ProgressDialog(getActivity());
                    dialog.setMessage("Loading");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();

                    JSONObject json = new JSONObject();
                    json.put("username", username.getText().toString());
                    json.put("password", password.getText().toString());

                    switch (sp.getSelectedItem().toString()) {
                        case "Admin Login":
                            json.put("u_type", "notice-admin");
                            break;
                        case "Staff Login":
                            json.put("u_type", "staff");
                            break;
                        case "Student Login":
                            json.put("u_type", "student");
                            break;
                    }

                    String url = MainActivity.IP + "/api/login";
                    json_request = new JsonObjectRequest(url, json, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                dialog.dismiss();
                                if (response.getString("status").equals("202")) {
                                    manager.popBackStack("sign in", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction transaction = manager
                                            .beginTransaction()
                                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                                            .addToBackStack("login");
                                    switch (userType) {
                                        case "student":
                                            transaction
                                                    .replace(R.id.login, new StudentDashboard());
                                            break;
                                        case "staff":
                                            transaction
//                                                    .replace(R.id.login, new StaffDashboard());
                                                    .replace(R.id.login, new ClassTestFragment());
                                            break;
                                        case "admin":
                                            transaction
                                                    .replace(R.id.login, new AdminDashBoard());
                                            break;
                                    }
                                    transaction
                                            .commit();

                                    MainActivity.userData = new UserData(
                                            "id",
                                            response.getString("username"),
                                            response.getString("access_token"),
                                            userType);

                                    MainActivity.checksignin();

                                } else {
                                    error.setText(response.getString("error"));
                                    error.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Could not connect to server. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!json_request.hasHadResponseDelivered()) {
                                requestQueue.cancelAll(json_request);
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Could not connect to server. Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, 15000);
                    json_request.setRetryPolicy(new DefaultRetryPolicy(
                            0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(json_request);
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
            usernameLayout.setError("Username is required");
            cont = false;
        }

        if (password.getText().toString().trim().equals("")) {
            passwordLayout.setError("Password is required");
            cont = false;
        }

        switch (sp.getSelectedItem().toString()) {
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

    @Override
    public void onStop() {
        super.onStop();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);

        if (json_request != null)
            requestQueue.cancelAll(json_request);
    }
}