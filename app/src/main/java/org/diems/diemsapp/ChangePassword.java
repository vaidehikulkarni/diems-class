package org.diems.diemsapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangePassword extends Fragment {

    EditText oldPass, newPass, reNewPass;
    TextView error;
    RequestQueue requestQueue;
    TextInputLayout oldPassLayout, newPassLayout, reNewPassLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        MainActivity.actionBar.setTitle("Change Password");
        if (MainActivity.actionBarMenu.findItem(R.id.nav_admin_changepass) != null)
            MainActivity.actionBarMenu.findItem(R.id.nav_admin_changepass).setChecked(true);
        if (MainActivity.actionBarMenu.findItem(R.id.nav_account_changepass) != null)
            MainActivity.actionBarMenu.findItem(R.id.nav_account_changepass).setChecked(true);

        requestQueue = Volley.newRequestQueue(getActivity());
        Button button = (Button) view.findViewById(R.id.btnCont);
        oldPass = (EditText) view.findViewById(R.id.oldPass);
        newPass = (EditText) view.findViewById(R.id.newPass);
        reNewPass = (EditText) view.findViewById(R.id.reNewPass);
        error = (TextView) view.findViewById(R.id.errorpass);
        oldPassLayout = (TextInputLayout) view.findViewById(R.id.oldPassLayout);
        newPassLayout = (TextInputLayout) view.findViewById(R.id.newPassLayout);
        reNewPassLayout = (TextInputLayout) view.findViewById(R.id.reNewPassLayout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont = true;
                if (oldPass.getText().toString().trim().equals("")) {
                    oldPassLayout.setError("Old password is required");
                    cont = false;
                }

                if (newPass.getText().toString().trim().equals("")) {
                    newPassLayout.setError("New password is required");
                    cont = false;
                }

                if (reNewPass.getText().toString().trim().equals("")) {
                    reNewPassLayout.setError("New password is required");
                    cont = false;
                }

                if (!cont)
                    return;

                if (oldPass.getText().toString().equals(newPass.getText().toString())) {
                    newPass.setError("Old and new password cannot be same");
                    return;
                }

                if (!newPass.getText().toString().equals(reNewPass.getText().toString())) {
                    reNewPass.setError("Both password do not match");
                    return;
                }

                String url = MainActivity.IP + "/api/changepass?access_token=" + MainActivity.accessToken;

                JSONObject json = new JSONObject();
                try {
                    json.put("old_pass", oldPass.getText().toString());
                    json.put("new_pass", newPass.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.PUT, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("202")) {
                                MainActivity.accessToken = response.getString("access_token");
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            else
                            {
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
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });

                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!json_request.hasHadResponseDelivered())
                        {
                            progressDialog.dismiss();
                            requestQueue.cancelAll(json_request);
                            Toast.makeText(getActivity(), "Could not connect to server. Please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 15000);

                json_request.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(json_request);
            }
        });
        return view;
    }
}
