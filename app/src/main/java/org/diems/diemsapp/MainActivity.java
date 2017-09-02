package org.diems.diemsapp;

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
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String accessToken = "";
    public static String IP = org.diems.diemsapp.IP.baseAddress;
    FragmentManager fragmentManager;
    RelativeLayout rl;
    NavigationView navigationView;
    static MenuItem profile, notification, signin, signout,aboutapp;
    static String loginType;
    static Menu actionBarMenu, navigationBarMenu;
    static ActionBar actionBar;
    FragmentTransaction transaction;
    DrawerLayout drawer;
    static TextView name_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        loginType = "student";

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getChildAt(0).setVerticalScrollBarEnabled(false);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        name_user = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name_user);

        fragmentManager = getFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.login, new HomeFragment())
                .commit();

        navigationBarMenu = navigationView.getMenu();
        rl = (RelativeLayout) findViewById(R.id.login);

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            //Checking for fragment count on backstack
            if (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStack();
            } else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

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
        aboutapp=menu.findItem(R.id.about_app);
        checksignin();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        transaction = fragmentManager.beginTransaction().setCustomAnimations(R.animator.fade_in, R.animator.fade_out);

        switch (id) {
            case R.id.about_app:
                startActivity(new Intent(this,AboutApp.class));
                break;
            case R.id.sign_in:
                transaction.replace(R.id.login, new SignInFragment())
                        .addToBackStack("sign in")
                        .commit();
                break;
            case R.id.feedback:
                startActivity(new Intent(this, Contact.class));
                break;
            case R.id.notification:
                startActivity(new Intent(this, NotificationActivity.class));
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
                                fragmentManager.popBackStack("login", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                transaction
                                        .replace(R.id.login, new HomeFragment())
                                        .addToBackStack(null)
                                        .commit();
                                Toast.makeText(MainActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                loginType = "";
                                accessToken = "";
                                checksignin();
                            }
                        })
                        .show();
                break;
        }

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
                        .addToBackStack(null)
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
                transaction
                        .replace(R.id.login, new Students())
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.nav_class_test:

                if (loginType.equals("student")) {
                    transaction
                            .replace(R.id.login, new ClassTestStudentFragment())
                            .addToBackStack(null)
                            .commit();
                } else if (loginType.equals("staff")) {
                    transaction
                            .replace(R.id.login, new ClassTestFragment())
                            .addToBackStack(null)
                            .commit();
                }
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
            case R.id.nav_account_changepass:
            case R.id.nav_admin_changepass:
                transaction
                        .replace(R.id.login, new ChangePassword())
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
        switch (loginType) {
            case "":
               aboutapp.setVisible(true);
                profile.setVisible(false);
                signout.setVisible(false);
                signin.setVisible(true);
                navigationBarMenu.setGroupVisible(R.id.nav_account, false);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, false);
                break;
            case "student":
                aboutapp.setVisible(true);
                profile.setVisible(true);
                signout.setVisible(true);
                signin.setVisible(false);
                navigationBarMenu.setGroupVisible(R.id.nav_account, true);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, false);
                break;
            case "staff":
                aboutapp.setVisible(true);
                profile.setVisible(true);
                signout.setVisible(true);
                signin.setVisible(false);
                navigationBarMenu.setGroupVisible(R.id.nav_account, true);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, false);
                break;
            case "admin":
                aboutapp.setVisible(true);
                profile.setVisible(false);
                signout.setVisible(true);
                signin.setVisible(false);
                navigationBarMenu.setGroupVisible(R.id.nav_account, false);
                navigationBarMenu.setGroupVisible(R.id.nav_admin, true);
                break;
        }

        navigationBarMenu.findItem(R.id.nav_attendance).setVisible(false);
        navigationBarMenu.findItem(R.id.nav_dashboard).setVisible(false);

    }
}
