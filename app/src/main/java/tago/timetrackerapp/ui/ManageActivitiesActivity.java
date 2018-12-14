package tago.timetrackerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import tago.timetrackerapp.R;
import tago.timetrackerapp.model.Activities;
import tago.timetrackerapp.model.Activity;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class ManageActivitiesActivity extends AppCompatActivity {

    private Activities model = Activities.instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Changes language to match settings, must be done before setting Content view
        LocaleManager.setLocale(this);
        setContentView(R.layout.activity_manage_activities);

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.edit_activities));

        // Floating add activity button
        FloatingActionButton fab = findViewById(R.id.add_activity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddActivity();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAddActivity() {
        model.newActivity();
        startActivity( new Intent(this, EditTaskActivity.class));
    }

    public void startEditActivity(Activity activity) {
        model.editActivity(activity);
        startActivity(new Intent(this, EditTaskActivity.class));
    }
}
