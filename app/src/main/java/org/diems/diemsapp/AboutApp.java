package org.diems.diemsapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AboutApp extends AppCompatActivity {
   TextView ver,website,feed;
Intent intent=getIntent();

    // Array of strings...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        String version="";
        ver=(TextView)findViewById(R.id.version);
        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // you can also use BuildConfig.APPLICATION_ID
                Context context=getApplicationContext();
                String appId = context.getPackageName();
                Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appId));
                boolean marketFound = false;

                // find all applications able to handle our rateIntent
                final List<ResolveInfo> otherApps = context.getPackageManager()
                        .queryIntentActivities(rateIntent, 0);
                for (ResolveInfo otherApp: otherApps) {
                    // look for Google Play application
                    if (otherApp.activityInfo.applicationInfo.packageName
                            .equals("com.android.vending")) {

                        ActivityInfo otherAppActivity = otherApp.activityInfo;
                        ComponentName componentName = new ComponentName(
                                otherAppActivity.applicationInfo.packageName,
                                otherAppActivity.name
                        );
                        // make sure it does NOT open in the stack of your activity
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // task reparenting if needed
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        // if the Google Play was already open in a search result
                        //  this make sure it still go to the app page you requested
                        rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // this make sure only the Google Play app is allowed to
                        // intercept the intent
                        rateIntent.setComponent(componentName);
                        context.startActivity(rateIntent);
                        marketFound = true;
                        break;

                    }
                }

                // if GP not present on device, open web browser
                if (!marketFound) {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id="+appId));
                    context.startActivity(webIntent);
                }
            }
        });
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ver.setText("Version "+version);
        website=(TextView)findViewById(R.id.website);
        website.setText("dietms.org");
        feed=(TextView)findViewById(R.id.feedback);
//        website.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                intent.setData(Uri.parse("http://dietms.org/diems-notice-app/"));
//                startActivity(intent);
//            }
//        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutApp.this,Contact.class));
            }
        });
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("About");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }


}
