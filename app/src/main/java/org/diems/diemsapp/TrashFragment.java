package org.diems.diemsapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TrashFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        MainActivity.actionBar.setTitle("Bin");
        MainActivity.navigationBarMenu.findItem(R.id.nav_bin).setChecked(true);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = MainActivity.IP + "/api/notices/self/trashed?access_token=" + MainActivity.userData.getAccessToken();

        progressBar.setVisibility(View.VISIBLE);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    setupViewPager(viewPager, jsonArray);

                    tabLayout.addOnTabSelectedListener(TrashFragment.this);
                    tabLayout.setupWithViewPager(viewPager);

                    progressBar.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);

        return view;
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

    private void setupViewPager(ViewPager viewPager, JSONArray jsonArray) throws JSONException {
        TrashFragment.ViewPagerAdapter adapter = new TrashFragment.ViewPagerAdapter(getFragmentManager());

        JSONArray deletedArray, expiredArray;
        deletedArray = new JSONArray();
        expiredArray = new JSONArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            Log.d(TAG, "setupViewPager: " + jsonArray.getJSONObject(i).getInt("expired"));
            if(jsonArray.getJSONObject(i).getInt("expired") == 1)
                expiredArray.put(jsonArray.getJSONObject(i));
            else
                deletedArray.put(jsonArray.getJSONObject(i));
        }

        Bundle deleted = new Bundle();
        deleted.putString("array", deletedArray.toString());
        DeletedNoticeFragment deletedNoticeFragment = new DeletedNoticeFragment();
        deletedNoticeFragment.setArguments(deleted);
        adapter.addFragment(deletedNoticeFragment, "Deleted");

        Bundle expired = new Bundle();
        expired.putString("array", expiredArray.toString());
        DeletedNoticeFragment deletedNoticeFragment1 = new DeletedNoticeFragment();
        deletedNoticeFragment1.setArguments(expired);
        adapter.addFragment(deletedNoticeFragment1, "Expired");

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<android.app.Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFragment(android.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
