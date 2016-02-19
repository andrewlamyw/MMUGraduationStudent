package com.lamyatweng.mmugraduationstudent;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static Activity sMainActivity;
    SessionManager mSession;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_view);

        // Destroy MainActivity so that MainActivity is removed from back stack
        sMainActivity = this;

        // Redirects to LoginActivity if user is not logged in
        mSession = new SessionManager(getApplicationContext());
        mSession.checkLogin();

        // Keep this location in sync. 10MB of previously synced data will be cached
        Constants.FIREBASE_REF_ROOT_STUDENT.keepSynced(true);

        // Set ActionBar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set toggling drawer with ActionBar Icon
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                         /* host Activity */
                mDrawerLayout,                /* DrawerLayout object */
                toolbar,                      /* Toolbar to inject into */
                R.string.drawer_open,         /* "open drawer" description */
                R.string.drawer_close         /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Set default fragment to display
        NewsFragment newsFragment = new NewsFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, newsFragment).commit();

        // Load page based on user clicked drawer menu item
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (findViewById(R.id.fragment_container) != null) {
                    if (savedInstanceState != null) {
                        // If we're being restored from a previous state,
                        // then we don't need to do anything and should return or else
                        // we could end up with overlapping fragments.
                        return false;
                    }
                    displayFragment(menuItem);
                }
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        // Set user email address and name in navigation drawer header
        View view = navigationView.getHeaderView(0);
        TextView emailHeader = (TextView) view.findViewById(R.id.header_email);
        TextView nameHeader = (TextView) view.findViewById(R.id.header_username);
        emailHeader.setText(mSession.getUserEmail());
        nameHeader.setText(mSession.getUserName());
    }

    /**
     * Display fragment based on selected drawerMenuItem
     * Add new item at layout/menu/navigation_items.xml
     */
    public void displayFragment(MenuItem menuItem) {

        switch (menuItem.getTitle().toString()) {
            case Constants.TITLE_NEWS:
                NewsFragment newsFragment = new NewsFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, newsFragment).commit();
                break;
            case Constants.TITLE_PROFILE:
                ProfileFragment profileFragment = new ProfileFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                break;
            case Constants.TITLE_GRADUATION:
                GraduationFragment graduationFragment = new GraduationFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, graduationFragment).commit();
                break;
            case Constants.TITLE_MAP:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;
            case Constants.TITLE_LOGOUT:
                mSession.logoutUser();
                break;
        }
    }

    /**
     * Set toggling drawer with ActionBar Icon
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // if current sMainActivity is main/home sMainActivity, then clicking the menu/appbar icon will open drawer
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set toggling drawer with ActionBar Icon
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    /**
     * Set toggling drawer with ActionBar Icon
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
