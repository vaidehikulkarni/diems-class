package com.example.diemsct;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager manager;
    RelativeLayout rl;
    NavigationView navigationView;
    static boolean signedin;
    static MenuItem profile,notification,signin,signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signedin = false;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login, new HomeAct())
                .commit();

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
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

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
        Drawable drawable = menu.findItem(R.id.notification).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        drawable = menu.findItem(R.id.profile).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

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
        String title = "DIEMS";
        manager = getSupportFragmentManager();

        switch(id)
        {
            case R.id.sign_in:
                manager.beginTransaction().replace(R.id.login,new SignIn()).commit();
                navigationView.getMenu().findItem(R.id.nav_more).setVisible(false);
                title = "Sign In";
                break;
            case R.id.contact:
                title = "Contact";
                break;
            case R.id.notification:
                title = "Notification";
                break;
            case R.id.profile:
                manager.beginTransaction().replace(R.id.login,new StudInfo()).commit();
                title = "Profile";
                break;
            case R.id.sign_out:
                title = "DIEMS";
                signedin = false;
                checksignin();
                manager.beginTransaction().replace(R.id.login,new HomeAct()).commit();
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show();
                break;
        }

        for(int i=0;i<navigationView.getMenu().size();i++)
        {
            navigationView.getMenu().getItem(i).setChecked(false);
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
        switch (item.getItemId())
        {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new HomeAct())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                title = "DIEMS";
                break;
            case R.id.nav_about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new AboutDiems())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_about).setChecked(true);
                title = "About";
                break;
            case R.id.nav_academics:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login, new Academics())
                        .commit();
                navigationView.getMenu().findItem(R.id.nav_academics).setChecked(true);
                title = "Academics";
                break;
            case R.id.nav_student:
                navigationView.getMenu().findItem(R.id.nav_student).setChecked(true);
                title = "Student";
                break;
        }
        getSupportActionBar().setTitle(title);

        checksignin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static private void checksignin()
    {
        if(signedin) {
            profile.setVisible(true);
            notification.setVisible(true);
            signout.setVisible(true);
            signin.setVisible(false);
        }
        else {
            profile.setVisible(false);
            notification.setVisible(false);
            signout.setVisible(false);
            signin.setVisible(true);
        }
    }
}
