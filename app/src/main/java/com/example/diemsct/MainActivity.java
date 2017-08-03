package com.example.diemsct;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager manager;
    RelativeLayout rl;
    static boolean signedin;
    static MenuItem profile,notification,signin,signout;
    static String loginType;
    static Menu actionBarMenu,navigationBarMenu;
    static String title;
    FragmentTransaction transaction;
    static NavigationView navigationView;
    String prevFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO : Replace 'false' with value from database
        signedin = false;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        manager = getFragmentManager();

        manager
                .beginTransaction()
                .add(R.id.login, new HomeFragment())
                .commit();

        navigationBarMenu = navigationView.getMenu();
        rl = (RelativeLayout)findViewById(R.id.login);

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        actionBarMenu = menu;
//        Drawable drawable = menu.findItem(R.id.notification).getIcon();
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        }
//        drawable = menu.findItem(R.id.profile).getIcon();
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        }

        profile = menu.findItem(R.id.profile);
        notification = menu.findItem(R.id.notification);
        signin = menu.findItem(R.id.sign_in);
        signout = menu.findItem(R.id.sign_out);
        checksignin();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        title = (String) getSupportActionBar().getTitle();
        transaction = manager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);

        for(int i=0;i<navigationView.getMenu().size();i++)
        {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        switch(id)
        {
            case R.id.sign_in:
                transaction.replace(R.id.login,new SignIn()).commit();
                title = "Sign In";
                break;
            case R.id.contact:
                startActivity(new Intent(this, Contact .class));
                title = "Contact";
                break;
            case R.id.notification:
                startActivity(new Intent(this, Notification.class));
                break;
            case R.id.profile:
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
                title = "Profile";
                break;
            case R.id.sign_out:
                new MaterialDialog.Builder(this)
                        .title("Warning!")
                        .content("Are you sure you want to logout?")
                        .negativeText("No")
                        .positiveText("Yes")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                signedin = false;
                                checksignin();
                                transaction.replace(R.id.login,new HomeFragment()).commit();
                                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                MainActivity.title = "DIEMS";
                                //redundant code because this is working asynchronously
                                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                                getSupportActionBar().setTitle(title);
                            }
                        })
                        .show();
                break;
        }

        getSupportActionBar().setTitle(title);

        checksignin();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String title = "DIEMS";
        transaction = manager.beginTransaction().setCustomAnimations(R.animator.fade_in,R.animator.fade_out);
        switch (item.getItemId())
        {
            case R.id.nav_home:
                transaction
                        .replace(R.id.login, new HomeFragment())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                title = "DIEMS";
                break;
            case R.id.nav_about:
                transaction
                        .replace(R.id.login, new AboutDiems())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_about).setChecked(true);
                title = "About";
                break;
            case R.id.nav_academics:
                transaction
                        .replace(R.id.login, new Academics())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_academics).setChecked(true);
                title = "Academics";
                break;
            case R.id.nav_student:
                manager.beginTransaction()
                        .replace(R.id.login, new Students())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_student).setChecked(true);
                title = "Student";
                break;
            case R.id.nav_class_test:
                transaction
                        .replace(R.id.login, new ClasstestView())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_class_test).setChecked(true);
                title = "Class Test";
                break;
            case R.id.nav_attendance:
                navigationView.getMenu().findItem(R.id.nav_attendance).setChecked(true);
                title = "Attendance";
                break;
            case R.id.nav_signout:
                onOptionsItemSelected(signout);
                break;
            case R.id.nav_dashboard:
                if(loginType.equals("student")) {
                    transaction
                            .replace(R.id.login, new StudentDashboard())
                            .commit();
                }
                else if(loginType.equals("staff")) {
                    transaction
                            .replace(R.id.login, new StaffDashboard())
                            .commit();
                }
                title = "Dashboard";
                break;
        }
        getSupportActionBar().setTitle(title);

        checksignin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static public void checksignin()
    {
        if(signedin) {
            profile.setVisible(true);
            notification.setVisible(true);
            signout.setVisible(true);
            signin.setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_account).setVisible(true);
            navigationBarMenu.setGroupVisible(R.id.nav_account,true);
        }
        else {
            profile.setVisible(false);
            notification.setVisible(false);
            signout.setVisible(false);
            signin.setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_account).setVisible(false);
            navigationBarMenu.setGroupVisible(R.id.nav_account,false);
        }
    }
}
