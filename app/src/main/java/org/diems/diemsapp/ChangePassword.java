package org.diems.diemsapp;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class ChangePassword extends Fragment {

    MaterialEditText oldPass, newPass, reNewPass;
    TextView error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        MainActivity.actionBar.setTitle("Change Password");
        if (MainActivity.actionBarMenu.findItem(R.id.nav_admin_changepass) != null)
            MainActivity.actionBarMenu.findItem(R.id.nav_admin_changepass).setChecked(true);
        if (MainActivity.actionBarMenu.findItem(R.id.nav_account_changepass) != null)
            MainActivity.actionBarMenu.findItem(R.id.nav_account_changepass).setChecked(true);

        Button button = (Button) view.findViewById(R.id.btnCont);
        oldPass = (MaterialEditText) view.findViewById(R.id.oldPass);
        newPass = (MaterialEditText) view.findViewById(R.id.newPass);
        reNewPass = (MaterialEditText) view.findViewById(R.id.reNewPass);
        error = (TextView) view.findViewById(R.id.errorpass);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cont = true;
                if (oldPass.getText().toString().trim().equals("")) {
                    oldPass.setError("Old password is required");
                    cont = false;
                }

                if (newPass.getText().toString().trim().equals("")) {
                    newPass.setError("New password is required");
                    cont = false;
                }

                if (reNewPass.getText().toString().trim().equals("")) {
                    reNewPass.setError("New password is required");
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

                String url = MainActivity.IP + "/changepass?access_token=" + MainActivity.accessToken;

                JSONObject json = new JSONObject();
                try {
                    json.put("old_pass", oldPass.getText().toString());
                    json.put("new_pass", newPass.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest json_request = new JsonObjectRequest(Request.Method.PUT, url, json, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("status").equals("202")) {
                                MainActivity.accessToken = response.getString("access_token");
                                getActivity().onBackPressed();
                                Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
//                                Toast.makeText(getActivity(), response.getString("error"), Toast.LENGTH_SHORT).show();
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

                Volley.newRequestQueue(getActivity()).add(json_request);
            }
        });
        return view;
    }

}
