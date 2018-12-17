
package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.managers.EmailManager;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Home";

    private static Fragment currentFragment;

    // Used to pass a flag if the activity was just recreated, used to recreate the activity if
    // returning from another activity (to prevent erroneous translations).
    private static boolean wasJustCreated = false;

    // Used to pass a flag if the drawer should start open on screen rotation
    private static boolean openDrawer = false;

    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (openDrawer)
            drawer.openDrawer(GravityCompat.START);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        openDrawer = false;

        // Bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // A flag for if the activity was just created, used to prevent recreation, onResume.
        wasJustCreated = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!wasJustCreated) {
            recreate(); // Recreate, if returning from another activity
        }
        wasJustCreated = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        openDrawer = drawer.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
                currentFragment = null;
            }
            switch (item.getItemId()) {
                case R.id.track_time:
                    TrackTimeFragment trackTimeFragment = TrackTimeFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_holder, trackTimeFragment).commit();
                    currentFragment = trackTimeFragment;
                    return true;
                case R.id.timeline:
                    TimelineFragment timelineFragment = TimelineFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_holder, timelineFragment).commit();
                    currentFragment = timelineFragment;
                    return true;
                case R.id.statistics:
                    StatisticsFragment statisticsFragment = StatisticsFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_holder, statisticsFragment).commit();
                    currentFragment = statisticsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation_bar view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_edit_category) {
            startActivity(new Intent(this, ManageCategoriesActivity.class));
        } else if (id == R.id.nav_edit_activity) {
            startActivity(new Intent(this, ManageActivitiesActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, InformationActivity.class));
        } else if (id == R.id.nav_share) {
            Log.w(TAG, "nav_share");
        } else if (id == R.id.nav_feedback) {
            feedbackAction();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void feedbackAction() {
        startActivity(EmailManager.createSendEmailIntent(
                getResources().getString(R.string.chose_email_client),
                "tiagor@kth.se",
                "Feedback",
                "Hi,\n"));
    }
}
