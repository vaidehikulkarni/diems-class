package com.example.diemsct;

import android.app.DownloadManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    public static JSONObject jsonObject;
    ProgressBar progressBar;
    boolean responseRecieved;
    String[] dept = {"All", "FE", "CSE", "ENTC", "CIVIL", "MECH"};
    public static boolean registered;
    BroadcastReceiver receiver;
    static DownloadManager dm;
    public static JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivity_notification);

        registered = false;

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        jsonObject = new JSONObject();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(NotificationActivity.this);
        String url = getString(R.string.IP) + "/notices";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            responseRecieved = true;
                            jsonArray = new JSONArray(response);

                            for (String str : dept)
                                jsonObject.put(str, filterDept(jsonArray, str));

                            Log.d("JSON OBJECT: \n", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                            jsonObject = null;
                        }

                        setupViewPager(viewPager);

                        //Adding onTabSelectedListener to swipe views
                        tabLayout.addOnTabSelectedListener(NotificationActivity.this);
                        tabLayout.setupWithViewPager(viewPager);

                        progressBar.setVisibility(View.GONE);
                        tabLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                jsonObject = null;
            }
        });

        responseRecieved = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!responseRecieved) {
//                    viewPager.setVisibility(View.GONE);
                    try {
                        new MaterialDialog.Builder(NotificationActivity.this)
                                .title("Error")
                                .content("Please check internet connection and try again later")
                                .positiveText("Ok")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        onBackPressed();
                                    }
                                })
                                .show();
                    }
                    catch (Exception e) {}
                }
            }
        }, 15000);

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Notification");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                            Toast.makeText(context, "Image downloaded into /Pictures/" + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setupViewPager(ViewPager viewPager) {
        NotificationActivity.ViewPagerAdapter adapter = new NotificationActivity.ViewPagerAdapter(getFragmentManager());

        for (String str : dept) {
            Bundle bundle = new Bundle();
            NotificationFragment object = new NotificationFragment();
            bundle.putString("dept", str);
            object.setArguments(bundle);
            adapter.addFragment(object, str);
        }

        viewPager.setAdapter(adapter);
    }

    private JSONArray filterDept(JSONArray object, String dept) throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < object.length(); i++) {
            if (object.getJSONObject(i).getString("branch").toLowerCase().equals(dept.toLowerCase())) {
                array.put(object.getJSONObject(i));
            }
        }
        return array;

        /*
        Json object format:
            {
                {"dept1" : [{
                        "key1" : "value",
                            .
                        },
                        {
                        "key2" : "value",
                            .
                        }]},
                {"dept2" : [{
                        "key1" : "notice1",
                            .
                        },
                        {
                        "key2" : "notice2",
                            .
                        }]},
                        .
            }
         */
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
