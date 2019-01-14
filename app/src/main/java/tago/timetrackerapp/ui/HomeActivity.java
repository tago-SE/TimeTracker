
package tago.timetrackerapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.db.TimeLogDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.entities.TimeLog;
import tago.timetrackerapp.ui.managers.DateManager;
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
    private com.google.api.services.calendar.Calendar googleCalendarService;
    private Sheets googleSheetService;
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
                .requestScopes(new Scope("https://www.googleapis.com/auth/calendar"))
                .requestScopes(new Scope ("https://www.googleapis.com/auth/spreadsheets"))
                .requestScopes(new Scope(Scopes.DRIVE_FILE))
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
            case R.id.export_option_2:
                System.out.println("clicked");
                exportToDrive();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void exportToCalendar() {

        Log.e(TAG, "in exportToCalendar");
        if ((checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED)){
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
                                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.GET_ACCOUNTS}, REQUEST_CALENDAR);
                            }

                        });

                AlertDialog alert = builder.create();
                alert.show();

            }

            if (preferences.getBoolean("homeFirstRun", true)){
                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR, Manifest.permission.GET_ACCOUNTS}, REQUEST_CALENDAR);
                preferences.edit().putBoolean("homeFirstRun", false).commit();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        System.out.println(requestCode);
        if (requestCode == REQUEST_CALENDAR){

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
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
            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton("https://www.googleapis.com/auth/calendar"));
            credential.setSelectedAccount(gso.getAccount());
            credential.setSelectedAccountName(account.getEmail());

            googleCalendarService = new com.google.api.services.calendar.Calendar.Builder(
                    AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                    .setApplicationName("Time Tracker")
                    .build();

            List<Event> events = new ArrayList<>();
            TimeLogDBHelper helper = TimeLogDBHelper.getInstance();
            BatchRequest batch = googleCalendarService.batch();
            JsonBatchCallback<Event> callback = new JsonBatchCallback<Event>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                    runOnUiThread(() ->
                            Toast.makeText(context, "Something went wrong with uploading to the calendar", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onSuccess(Event event, HttpHeaders responseHeaders) throws IOException {

                    runOnUiThread(() ->
                            Toast.makeText(context, "Successfully imported events to your calendar!", Toast.LENGTH_SHORT).show());
                }
            };

            int i = 0;
            for (TimeLog t : helper.getAllDescending()){
                Activity a = t.getActivity();
                if (a == null){
                    continue;
                }

                if (t.calendarExported){
                    continue;
                }

                t.calendarExported = true;
                helper.insertOrUpdate(t);
                Category c = a.getCategory();

                if (c == null){
                    events.add(new Event()
                            .setSummary(a.name)
                            .setDescription("Category missing")) ;

                    DateTime startDateTime = new DateTime(t.start.replace(" ", "T") + "+01:00");
                    EventDateTime start = new EventDateTime()
                            .setDateTime(startDateTime);
                    events.get(i).setStart(start);

                    DateTime endDateTime = new DateTime(t.stop.replace(" ", "T") + "+01:00");
                    EventDateTime end = new EventDateTime()
                            .setDateTime(endDateTime);
                    events.get(i).setEnd(end);

                    Event.Organizer organizer = new Event.Organizer();
                    organizer.setEmail(account.getEmail());
                    organizer.setSelf(true);
                    organizer.setId(account.getId());
                    events.get(i).setOrganizer(organizer);

                    EventAttendee[] attendee = new EventAttendee[] {new EventAttendee().setEmail(account.getEmail())};
                    events.get(i).setAttendees(Arrays.asList(attendee));
                    i++;
                }

                else {

                    events.add(new Event()
                            .setSummary(a.name)
                            .setDescription(c.name)) ;

                    DateTime startDateTime = new DateTime(t.start.replace(" ", "T") + "+01:00");
                    EventDateTime start = new EventDateTime()
                            .setDateTime(startDateTime);
                    events.get(i).setStart(start);

                    DateTime endDateTime = new DateTime(t.stop.replace(" ", "T") + "+01:00");
                    EventDateTime end = new EventDateTime()
                            .setDateTime(endDateTime);
                    events.get(i).setEnd(end);

                    Event.Organizer organizer = new Event.Organizer();
                    organizer.setEmail(account.getEmail());
                    organizer.setSelf(true);
                    organizer.setId(account.getId());
                    events.get(i).setOrganizer(organizer);

                    EventAttendee[] attendee = new EventAttendee[] {new EventAttendee().setEmail(account.getEmail())};
                    events.get(i).setAttendees(Arrays.asList(attendee));
                    i++;
                }
            }

            final String calendarId = "primary";

            new Thread() {
                public void run(){
                    try {
                        if (events.size() == 0){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "You have no new entries for tracking time!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        else {
                            for (int i = 0; i < events.size(); i++){
                                googleCalendarService.events().insert(calendarId, events.get(i)).queue(batch, callback);
                            }
                            batch.execute();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private void exportToDrive(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account == null){

            Toast.makeText(this, "You must be logged in to use this feature!", Toast.LENGTH_LONG).show();
        }

        else {

            GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(this, Collections.singleton("https://www.googleapis.com/auth/spreadsheets"));
            credential.setSelectedAccount(gso.getAccount());
            credential.setSelectedAccountName(account.getEmail());

            googleSheetService = new Sheets.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
                    .setApplicationName("Time Tracker")
                    .build();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Time Tracker");
            builder.setMessage("Name of your spreadsheet");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Export", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  String fileTitle = input.getText().toString();
                  fileTitle = fileTitle.replaceAll("[^a-zA-Z0-9]+", "");

                    final Spreadsheet[] spreadsheet = {new Spreadsheet()
                            .setProperties(new SpreadsheetProperties()
                            .setTitle(fileTitle))};

                    new Thread(){
                        public void run(){
                            try{
                                spreadsheet[0] = googleSheetService.spreadsheets().create(spreadsheet[0])
                                        .setFields("spreadsheetId")
                                        .execute();

                                String range = "A:E";

                                List<List<Object>> values = new ArrayList<>();
                                ArrayList<Object> titles = new ArrayList<>();
                                values.add(titles);
                                values.get(0).add("Activity");
                                values.get(0).add("Category");
                                values.get(0).add("Start");
                                values.get(0).add("Stop");
                                values.get(0).add("Time");

                                TimeLogDBHelper helper = TimeLogDBHelper.getInstance();

                                for (TimeLog t: helper.getAllDescending()) {
                                    List<Object> list = new ArrayList<>();
                                    Activity a = t.getActivity();
                                    if (a == null)
                                        continue;
                                    list.add(a.name);
                                    Category c = a.getCategory();
                                    String category = "";
                                    if (c != null)
                                        category = c.name;
                                    list.add(category);
                                    list.add(t.start);
                                    list.add(t.stop);
                                    list.add(DateManager.formatTime(t.milliseconds));
                                    values.add(list);
                                }

                                ValueRange body = new ValueRange()
                                        .setValues(values);
                                googleSheetService.spreadsheets().values().update(spreadsheet[0].getSpreadsheetId(), range, body)
                                        .setValueInputOption("RAW")
                                        .execute();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Spreadsheet created and uploaded to Drive!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
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


            Log.w(TAG, "CALENDAR SERVICE IS IN LOGIN: " + googleCalendarService);


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
