package com.example.diemsct;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class NotificationMech extends Fragment {

    private AnimatedExpandableListView listView;
    private long enqueue;
    private DownloadManager dm;
    private BroadcastReceiver receiver;

    public NotificationMech() {

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
        View view = inflater.inflate(R.layout.fragment_notification_mech, container, false);
        List<GroupItem> items = new ArrayList<>();

        for (int i = 0; i < NotificationActivity.jsonArray.length(); i++) {
            try {
                JSONObject js = NotificationActivity.jsonArray.getJSONObject(i);

                if (js.getString("branch").toLowerCase().equals("mech")) {
                    GroupItem item = new GroupItem();
                    item.title = js.getString("title");
                    ChildItem child = new ChildItem();
                    if (!js.getString("body").equals("null"))
                        child.title = js.getString("body");
                    else {
                        child.title = "";
                    }
                    child.imageSrc = js.getString("img_url");
                    item.items.add(child);

                    items.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        ExampleAdapter adapter = new ExampleAdapter(getActivity());
        adapter.setData(items);

        listView = (AnimatedExpandableListView) view.findViewById(R.id.animlistview);
        listView.setAdapter(adapter);

        // In order to show animations, we need to use a custom click handler
        // for our ExpandableListView.
        listView.setOnGroupClickListener(new OnGroupClickListener() {

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

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(enqueue);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c
                                .getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c
                                .getInt(columnIndex)) {
                            Toast.makeText(context, "Image downloaded into /Pictures/" + getString(R.string.app_name), Toast.LENGTH_SHORT).show();
                            getActivity().unregisterReceiver(receiver);
                        }
                    }
                }
            }
        };

        getActivity().registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return view;
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<>();
    }

    private static class ChildItem {
        String title;
        String imageSrc;
    }

    private static class ChildHolder {
        TextView title;
        ImageView image;
        Button btnDownload;
    }

    private static class GroupHolder {
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
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
        public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildHolder holder;
            final ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.image = (ImageView) convertView.findViewById(R.id.noticeimage);
                holder.btnDownload = (Button) convertView.findViewById(R.id.btnDownload);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(Html.fromHtml(item.title));

            holder.btnDownload.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.imageSrc))
                            .setTitle("Notice")
                            .setDescription("Downloading...")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir("/Pictures/" + getString(R.string.app_name), new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".png");
                    enqueue = dm.enqueue(request);
                }
            });

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, 1);
            }

            if (!item.imageSrc.equals(""))
            {
                Picasso
                        .with(getActivity())
                        .load(item.imageSrc)
                        .fit()
                        .centerCrop()
                        .into(holder.image);
            }
            else
            {
                holder.image.setVisibility(View.GONE);
                holder.btnDownload.setVisibility(View.GONE);
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
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

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

}
