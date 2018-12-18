package tago.timetrackerapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.ui.Adapter.ActivitiesAdapter;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.viewmodels.EditActivities;

/* TODO: Change to a recycle view (list) rather than a grid? */

public class ManageActivitiesActivity extends AppCompatActivity {

    private static final int MARGIN = 20;
    private static final int PADDING = 10;
    private EditActivities model = EditActivities.instance;


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
    public void onResume() {
        super.onResume();
        GridView gridView = findViewById(R.id.grid);
        gridView.setVerticalSpacing(12);
        gridView.setHorizontalSpacing(12);
        gridView.setNumColumns(getMaxNumColumns());
        // Populate category list
        List<Activity> activities = model.load();
        //ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(this, activities);
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(this, activities) {
            @Override
            public void onActivityClick(Activity activity) {
                startEditActivity(activity);
            }

            @Override
            public void onActivityLongClick(Activity activity) {

            }
        };
        gridView.setAdapter(activitiesAdapter);
        //gridView.invalidate();
    }


    /* TODO needs to be dynamic depending on screen dimensions and frame size */
    private int getMaxNumColumns() {
        return 4;
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
        startActivity( new Intent(this, EditActivityActivity.class));
    }

    public void startEditActivity(Activity activity) {
        model.editActivity(activity);
        startActivity(new Intent(this, EditActivityActivity.class));
    }
}
