
package tago.timetrackerapp.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.managers.EmailManager;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final Context context = this;
    private static final String TAG = "Home";
    private static int RC_SIGN_IN = 9001;
    private static int REQUEST_CALENDAR = 8001;

    private static Fragment currentFragment;

    // Used to pass a flag if the activity was just recreated, used to recreate the activity if
    // returning from another activity (to prevent erroneous translations).
    private static boolean wasJustCreated = false;

    // Used to pass a flag if the drawer should start open on screen rotation
    private static boolean openDrawer = false;

    private static int selectedItemId;

    NavigationView navigationView;


    private BottomNavigationView bottomNavigationView;

    private Toolbar toolbar;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (openDrawer)
            drawer.openDrawer(GravityCompat.START);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        openDrawer = false;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Bottom navigation
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Sets the current view to the currently selected one


        // A flag for if the activity was just created, used to prevent recreation, onResume.
        wasJustCreated = true;

        //Google sign in init
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .requestEmail()
                .requestScopes(new Scope(Scopes.PROFILE))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!wasJustCreated) {
            recreate(); // Recreate, if returning from another activity
        }
        wasJustCreated = false;
        setupToolbar(bottomNavigationView.getSelectedItemId());
        setupFragment(bottomNavigationView.getSelectedItemId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.export_option:
                System.out.println("clicked");
                exportToCalendar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportToCalendar() {

        Log.e(TAG, "in exportToCalendar");
        if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "in exportToCalendar-if");
            Log.w(TAG, "CALENDAR PERMISSIONS ARE GRANTED");
            exportToCalendarTask();
        }

        else {
            Log.e(TAG, "in exportToCalendar-else");
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CALENDAR)){
                Log.e(TAG, "in exportToCalendar-else(if)");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Permissions for your calendar are required to export events.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CALENDAR);
                            }

                        });

                AlertDialog alert = builder.create();
                alert.show();

            }

            if (preferences.getBoolean("homeFirstRun", true)){
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, REQUEST_CALENDAR);
                preferences.edit().putBoolean("homeFirstRun", false).commit();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        System.out.println(requestCode);
        if (requestCode == REQUEST_CALENDAR){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.w(TAG, "CALENDAR PERMISSIONS ARE GRANTED");
                exportToCalendarTask();
            }

            else {
                Toast.makeText(this, "Calendar permissions were not granted, aborting.", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void exportToCalendarTask(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null){
            Toast.makeText(this, "You must be logged in to use this feature!", Toast.LENGTH_LONG).show();
        }

        else {
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2019, 0, 2, 21, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.set(2019, 0, 2, 21, 30);
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, "Yoga")
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
                    .putExtra(Intent.EXTRA_EMAIL, account.getEmail());
            startActivity(intent);

            /*for(int i=2;i<=4;i++){
                Log.w(TAG, "Adding event");
                long startMillis = 0;
                long endMillis = 0;
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2019, 0, i, 17, 0);
                startMillis = beginTime.getTimeInMillis();
                Calendar endTime = Calendar.getInstance();
                endTime.set(2000, 0, i, 17, 30);
                endMillis = endTime.getTimeInMillis();
                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.DTSTART, startMillis);
                values.put(CalendarContract.Events.DTEND, endMillis);
                values.put(CalendarContract.Events.TITLE, "Jazzercise");
                values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
                values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

                // get the event ID that is the last element in the Uri
                long eventID = Long.parseLong(uri.getLastPathSegment());
            }*/

            /*
            long calID = 3;
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            beginTime.set(2019, 0, 2, 20, 0);
            startMillis = beginTime.getTimeInMillis();
            Calendar endTime = Calendar.getInstance();
            beginTime.set(2019, 0, 2, 20, 30);
            endMillis = endTime.getTimeInMillis();

            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE, "Jazzercise");
            values.put(CalendarContract.Events.DESCRIPTION, "Group workout");
            values.put(CalendarContract.Events.CALENDAR_ID, 1);
            values.put(CalendarContract.Events.GUESTS_CAN_SEE_GUESTS, true);
            values.put(CalendarContract.Events.ORGANIZER, account.getEmail());
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);


            // get the event ID that is the last element in the Uri
            long eventID = Long.parseLong(uri.getLastPathSegment());
            */

        }
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
            switch (item.getItemId()) {
                case R.id.track_time:
                    if (selectedItemId != R.id.track_time)
                        setupFragment(R.id.track_time);
                    return true;
                case R.id.timeline:
                    if (selectedItemId != R.id.timeline)
                        setupFragment(R.id.timeline);
                    return true;
                case R.id.statistics:
                    if (selectedItemId != R.id.statistics)
                        setupFragment(R.id.statistics);
                    return true;
            }
            return false;
        }
    };

    private void setupFragment(int selectedItemId) {
        Fragment fragment = null;
        if (selectedItemId == R.id.track_time) {
            fragment = TrackTimeFragment.newInstance();
        }
        else if (selectedItemId == R.id.timeline) {
            fragment = TimelineFragment.newInstance();
        }
        else if (selectedItemId == R.id.statistics) {
            fragment = StatisticsFragment.newInstance();
        }
        if (fragment != null) {
            HomeActivity.selectedItemId = selectedItemId;
            setupToolbar(selectedItemId);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_holder, fragment).commit();
            currentFragment = fragment;
        }
    }

    private void setupToolbar(int selectedItemId) {
        if (selectedItemId == R.id.track_time) {
            toolbar.setTitle(getResources().getString(R.string.track_time));
        }
        else if (selectedItemId == R.id.timeline) {
            toolbar.setTitle(getResources().getString(R.string.timeline));
        }
        else if (selectedItemId == R.id.statistics) {
            toolbar.setTitle(getResources().getString(R.string.statistics));
        }
    }

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
        } else if (id == R.id.nav_sign_in) {
            signIn();
        } else if (id == R.id.nav_sign_out){
            signOut();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
            Log.w(TAG, "SIGN IN SUCCESS: \n" +
                    account.getEmail() +
                    "\n" + account.getDisplayName() +
                    "\n" + account.getId() +
                    "\n" + account.getPhotoUrl());

            Toast.makeText(this, getString(R.string.successful_sign_in), Toast.LENGTH_SHORT).show();

        } catch(ApiException e){
            updateUI(null);
            Log.e(TAG, "signInResult:failed code = " + e.getStatusCode());
            updateUI(null);
        }
    }

    private void signOut(){
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
                Toast.makeText(context, getString(R.string.successful_sign_out), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(@Nullable GoogleSignInAccount account){

        Menu menu = navigationView.getMenu();
        MenuItem signIn = menu.findItem(R.id.nav_sign_in);
        MenuItem signOut = menu.findItem(R.id.nav_sign_out);

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.name_of_user);
        TextView eMail = headerView.findViewById(R.id.mail_of_user);
        CircleImageView profilePic = headerView.findViewById(R.id.profile_pic);

        if (account != null){
            signIn.setVisible(false);
            signOut.setVisible(true);
            //findViewById(R.id.nav_sign_in).setVisibility(View.GONE);


            name.setText(account.getDisplayName());
            eMail.setText(account.getEmail());

            Uri photoUri = account.getPhotoUrl();
            Picasso.get()
                    .load(photoUri)
                    .error(android.R.mipmap.sym_def_app_icon)
                    .centerCrop()
                    .resize(150,150)
                    .into(profilePic);

        }

        else{

            signIn.setVisible(true);
            signOut.setVisible(false);

            name.setText("");
            eMail.setText("");
            profilePic.setVisibility(View.GONE);
            //findViewById(R.id.nav_sign_in).setVisibility(View.VISIBLE);
        }
    }

    private void feedbackAction() {
        startActivity(EmailManager.createSendEmailIntent(
                getResources().getString(R.string.chose_email_client),
                "tiagor@kth.se",
                "Feedback",
                "Hi,\n"));
    }
}
