package com.example.diemsct;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NotificationFE extends Fragment {
    private AnimatedExpandableListView listView;

    public NotificationFE() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification_fe, container, false);
        List<GroupItem> items = new ArrayList<>();

        // Populate our list with groups and it's children
        for (int i = 1; i < 3; i++) {
            GroupItem item = new GroupItem();

            item.title = "Group " + i;

//            for(int j = 0; j < i; j++) {
            ChildItem child = new ChildItem();
            child.title = "Awesome item " + i;
            child.imageSrc = "http://via.placeholder.com/1234x123";

            item.items.add(child);
//            }

            items.add(item);
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
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.list_item, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textTitle);
                holder.image = (ImageView) convertView.findViewById(R.id.noticeimage);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET}, 1);
            }

            Picasso
                    .with(getActivity())
                    .load(item.imageSrc)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
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
