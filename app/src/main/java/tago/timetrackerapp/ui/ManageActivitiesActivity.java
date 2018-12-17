package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.viewmodels.EditActivities;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.ui.managers.LocaleManager;

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
        gridView.setNumColumns(getMaxNumColumns());
        // Populate category list
        List<Activity> activities = model.load();
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(this, activities);
        gridView.setAdapter(activitiesAdapter);
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

    private class ActivitiesAdapter extends BaseAdapter {

        private Context context;
        private List<Activity> items;

        public ActivitiesAdapter(Context context, List<Activity> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getCount() {
            if (items == null)
                return 0;
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            if (items == null)
                return null;
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Activity data = items.get(position);
            if (convertView == null) {
                final LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.frame_activity, null);
            }
            final ImageView icon = convertView.findViewById(R.id.icon);
            final TextView name = convertView.findViewById(R.id.name);
            name.setText(data.name);
            icon.setColorFilter(data.color);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startEditActivity(data);
                }
            });
            return convertView;
        }
    }
}
