package com.android.hremote;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class RemoteActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DiscoveryFragment mDiscoveryFragment;
    public MousePadFragment mMousePadFragment;
    public KeyboardFragment mKeyboardFragment;
    private ActionBarDrawerToggle mDToggle;
    private Menu mRemoteMenu;
    private int mSelectItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_remote);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if (mSelectItem == R.id.nav_keyboard) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
            }
        };

        drawer.setDrawerListener(mDToggle);
        mDToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mDiscoveryFragment = new DiscoveryFragment();
        mMousePadFragment = new MousePadFragment();
        mKeyboardFragment = new KeyboardFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, mDiscoveryFragment).commit();
        mSelectItem = R.id.nav_conn;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.remote, menu);
        mRemoteMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mDiscoveryFragment.refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        mSelectItem = item.getItemId();

        if (mSelectItem != R.id.nav_keyboard)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (mSelectItem== R.id.nav_conn) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, mDiscoveryFragment).commit();
        } else if (mSelectItem == R.id.nav_touchpad) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, mMousePadFragment).commit();
        } else if (mSelectItem == R.id.nav_keyboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_fragment, mKeyboardFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
