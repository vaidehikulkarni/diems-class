package com.example.diemsct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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

    FragmentManager fragmentManager;
    RelativeLayout rl;
    NavigationView navigationView;
    static boolean signedin;
    static String title;
    static MenuItem profile, notification, signin, signout;
    static String loginType = "";
    static Menu actionBarMenu, navigationBarMenu;
    static ActionBar actionBar;
    FragmentTransaction transaction;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        // TODO : Replace empty string with value from database
        loginType = "";

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        fragmentManager = getFragmentManager();

        fragmentManager
                .beginTransaction()
                .replace(R.id.login, new HomeFragment())
//                .addToBackStack(null)
                .commit();

        navigationBarMenu = navigationView.getMenu();
        rl = (RelativeLayout) findViewById(R.id.login);

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        }
        else {
            //Checking for fragment count on backstack
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        actionBarMenu = menu;
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
        transaction = fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in, R.animator.fade_out);

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }

        switch (id) {
            case R.id.sign_in:
                transaction.replace(R.id.login, new SignInFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.contact:
                break;
            case R.id.notification:
                startActivity(new Intent(this, Notification.class));
                break;
            case R.id.profile:
                Intent intent = new Intent(this, Profile.class);
                startActivity(intent);
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
                                transaction.replace(R.id.login, new HomeFragment())
                                        .commit();
                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                                    fragmentManager.popBackStack();
                                }
                                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                loginType = "";
                                checksignin();
                            }
                        })
                        .show();
                break;
        }

        actionBar.setTitle(title);

        checksignin();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        transaction = fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        switch (item.getItemId()) {
            case R.id.nav_home:
                transaction
                        .replace(R.id.login, new HomeFragment())
                        //.addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_about:
                transaction
                        .replace(R.id.login, new AboutDiems())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_academics:
                transaction
                        .replace(R.id.login, new Academics())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_student:
                fragmentManager.beginTransaction()
                        .replace(R.id.login, new Students())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_class_test:
                transaction
                        .replace(R.id.login, new ClassTestFragment())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_attendance:
                break;
            case R.id.nav_signout:
                onOptionsItemSelected(signout);
                break;
            case R.id.nav_dashboard:
                if (loginType.equals("student")) {
                    transaction
                            .replace(R.id.login, new StudentDashboard())
                            .addToBackStack(null)
                            .commit();
                } else if (loginType.equals("staff")) {
                    transaction
                            .replace(R.id.login, new StaffDashboard())
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.nav_upload:
                transaction
                        .replace(R.id.login, new UploadNotice())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_admin_dashboard:
                transaction
                        .replace(R.id.login, new AdminDashBoard())
                        .addToBackStack(null)
                        .commit();
                break;
        }

        checksignin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static public void checksignin() {
//        if (loginType.equals("")) {
//            profile.setVisible(true);
//            signout.setVisible(true);
//            signin.setVisible(false);
//            navigationBarMenu.setGroupVisible(R.id.nav_account, true);
//        } else if(loginType.equals("staff") || loginType.equals("student")) {
//            profile.setVisible(false);
//            signout.setVisible(false);
//            signin.setVisible(true);
//            navigationBarMenu.setGroupVisible(R.id.nav_account, false);
//        }
//        else if (loginType.equals("admin"))
//        {
//
//        }
        switch (loginType)
        {
            case "":
                profile.setVisible(false);
                signout.setVisible(false);
                signin.setVisible(true);
                navigationBarMenu.setGroupVisible(R.id.nav_account, false);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, false);
                break;
            case "student":
            case "staff":
                profile.setVisible(true);
                signout.setVisible(true);
                signin.setVisible(false);
                navigationBarMenu.setGroupVisible(R.id.nav_account, true);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, false);
                break;
            case "admin":
                profile.setVisible(true);
                signout.setVisible(true);
                signin.setVisible(false);
                navigationBarMenu.setGroupVisible(R.id.nav_account, false);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, true);
                break;
        }
    }
}
