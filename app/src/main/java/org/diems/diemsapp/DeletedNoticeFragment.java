package org.diems.diemsapp;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class DeletedNoticeFragment extends Fragment {

    private AnimatedExpandableListView listView;
    TextView textView;
    boolean empty = true;
    List<GroupItem> items;
    View view;

    public DeletedNoticeFragment() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deleted_notice, container, false);
        textView = (TextView) view.findViewById(R.id.empty);
//        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        items = new ArrayList<>();

        Log.d(TAG, "onCreateView: " + getArguments().toString());

        try {
            setUpListView(new JSONArray(getArguments().getString("array")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private static class GroupItem {
        String titleText;
        List<ChildItem> items = new ArrayList<>();
    }

    private static class ChildItem {
        String bodyText;
        String imageSrc;
        String id;
    }

    private static class ChildHolder {
        TextView body;
        ImageView image;
        ProgressBar progressBar;
    }

    private static class GroupHolder {
        TextView title;
    }


    private class ExampleAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        ExampleAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildHolder holder;
            final ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.body = (TextView) convertView.findViewById(R.id.textBody);
                holder.image = (ImageView) convertView.findViewById(R.id.noticeImage);
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.noticeProgressBar);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.body.setText(Html.fromHtml(item.bodyText));
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageDisplay.class);
                    intent.putExtra("imageSource", item.imageSrc);
                    intent.putExtra("body", items.get(groupPosition).titleText);
                    startActivity(intent);
                }
            });

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, 1);
            }

            if (!item.imageSrc.equals("")) {
                Picasso
                        .with(getActivity())
                        .load(item.imageSrc)
                        .fit()
                        .centerCrop()
                        .into(holder.image, new Callback() {
                            @Override
                            public void onSuccess() {
                                holder.progressBar.setVisibility(View.GONE);
                                holder.image.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            } else {
                holder.image.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textBody);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.titleText);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

    }

    void checkEmpty() {

        if (empty)
            textView.setVisibility(View.VISIBLE);
    }

    void setUpListView(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                GroupItem item = new GroupItem();
                item.titleText = js.getString("title");
                ChildItem child = new ChildItem();
                if (!js.getString("body").equals("null"))
                    child.bodyText = js.getString("body");
                else {
                    child.bodyText = "";
                }
                child.id = js.getString("id");
                child.imageSrc = js.getString("img_url");
                item.items.add(child);

                items.add(item);
                empty = false;

                ExampleAdapter adapter = new ExampleAdapter(getActivity());
                adapter.setData(items);

                listView = (AnimatedExpandableListView) view.findViewById(R.id.admin_animlistview);
                listView.setAdapter(adapter);
            }
            checkEmpty();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        if (listView != null)
            listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    // We call collapseGroupWithAnimation(int) and
                    // expandGroupWithAnimation(int) to animate group
                    // expansion/collapse.
                    if (listView.isGroupExpanded(groupPosition)) {
                        listView.collapseGroupWithAnimation(groupPosition);
                    } else {
                        listView.expandGroupWithAnimation(groupPosition);
                    }
                    return true;
                }

            });

    }
}
