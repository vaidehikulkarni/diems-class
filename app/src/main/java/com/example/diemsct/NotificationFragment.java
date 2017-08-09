package com.example.diemsct;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private AnimatedExpandableListView listView;
    TextView textView;
    boolean empty = true;

    public NotificationFragment() {

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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        textView = (TextView) view.findViewById(R.id.empty);
        List<GroupItem> items = new ArrayList<>();

        Log.d("JSON: ", NotificationActivity.jsonObject.toString());


        try {

            Bundle bundle = getArguments();

            JSONArray array = NotificationActivity.jsonObject.getJSONArray(bundle.get("dept").toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject js = array.getJSONObject(i);
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
                empty = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ExampleAdapter adapter = new ExampleAdapter(getActivity());
        adapter.setData(items);

        listView = (AnimatedExpandableListView) view.findViewById(R.id.animlistview);
        listView.setAdapter(adapter);
        checkEmpty();

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
        Button btnDownload;
        ProgressBar progressBar;
        WebView webView;
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
                holder.progressBar = (ProgressBar) convertView.findViewById(R.id.noticeProgressBar);
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(Html.fromHtml(item.title));

            Toast.makeText(getActivity(), item.imageSrc, Toast.LENGTH_SHORT).show();
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.imageSrc));
                    startActivity(intent);
                }
            });

            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        InputStream is = (InputStream) new URL("http://via.placeholder.com/1200x1200").getContent();
                        Drawable d = Drawable.createFromStream(is, "src name");
                        holder.image.setImageDrawable(d);
                        holder.progressBar.setVisibility(View.GONE);
                        holder.image.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

//            holder.btnDownload.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.imageSrc))
//                            .setTitle("Notice")
//                            .setDescription("Downloading...")
//                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                            .setDestinationInExternalPublicDir("/Pictures/" + getString(R.string.app_name), new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()) + ".png");
//                    NotificationActivity.dm.enqueue(request);
//                }
//            });

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

    void checkEmpty() {

        if (empty) {
            listView.setBackgroundColor(getResources().getColor(R.color.grey));
            textView.setVisibility(View.VISIBLE);
        } else
            listView.setBackgroundColor(getResources().getColor(R.color.white));
    }
}
