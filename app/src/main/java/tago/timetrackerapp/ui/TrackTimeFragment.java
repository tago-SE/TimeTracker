package tago.timetrackerapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.ui.Adapter.ActivitiesAdapter;
import tago.timetrackerapp.ui.managers.DateManager;
import tago.timetrackerapp.viewmodels.TrackTime;

public class TrackTimeFragment extends Fragment {

    private static final String TAG = "TrackTime";

    private GridView gridView;
    private LinearLayout bottomLayout;
    private ActivitiesAdapter activitiesAdapter;
    private TrackTime model = TrackTime.instance;
    private final TrackTimeFragment fragment = this;

    public TrackTimeFragment() {
        // Required empty constructor
    }

    // Updates UI periodically while fragment is open to account for time increments.
    private Handler handler = new Handler();
    private Runnable runnableTimeUpdate = new Runnable() {
        @Override
        public void run() {
            try {
                updateUI();
            } finally {
                handler.postDelayed(runnableTimeUpdate, 1000);
            }
        }
    };

    /**
     * Factory method for creating a TrackTimeFragment.
     */
    public static TrackTimeFragment newInstance() {
        TrackTimeFragment fragment = new TrackTimeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track_time, container, false);
        setupBottomMenu(view);
        return view;
    }

    private void setupBottomMenu(View view) {
        bottomLayout = view.findViewById(R.id.time_track_bottom_menu);
        // initially starts hidden
        bottomLayout.setVisibility(View.INVISIBLE);
        // Event listeners
        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = fragment.getContext();
                if (model.hasSelection()) {
                    model.submit();
                    startActivity(new Intent(context, LogTimeActivity.class));
                } else {
                    // Error
                    AlertDialog alertDialog= new AlertDialog.Builder(context).create();
                    alertDialog.setMessage(getString(R.string.err_no_selected_activity));
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,
                            getString(android.R.string.ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        Button cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.cancel();
                updateUI();
            }
        });
    }

    private void setupGrid() {
        gridView = getView().findViewById(R.id.grid);
        gridView.setVerticalSpacing(12);
        gridView.setHorizontalSpacing(12);
        gridView.setNumColumns(4);
        // Populate category list
        activitiesAdapter = new ActivitiesAdapter(getContext(), model.getActivities()) {
            @Override
            public void onActivityClick(Activity activity, View view, int position) {
                model.onClickActivity(activity);
                // Start new activity instantly if not attempting to select multiple
                if (!model.isSelectingMultiple()) {
                    if (model.hasSelection()) {
                        model.submit();
                        startActivity(new Intent(fragment.getContext(), LogTimeActivity.class));
                    }
                }
                updateUI();
            }
            @Override
            public void onActivityLongClick(Activity activity, View view, int position) {
                model.enableBottomMenu();
            }
        };
        gridView.setAdapter(activitiesAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        setupGrid();
        updateUI();
        runnableTimeUpdate.run();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnableTimeUpdate);
    }

    public void updateUI() {
        if (model.isSelectingMultiple())
            bottomLayout.setVisibility(View.VISIBLE);
        else
            bottomLayout.setVisibility(View.INVISIBLE);
        activitiesAdapter.notifyDataSetChanged();

        // Last tracked time
        long time = model.getMillisecondsSinceLastTrack();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String lastTime = dateFormat.format(model.getLastTrackedDate());
        TextView sinceTextView = getView().findViewById(R.id.since);
        sinceTextView.setText(getString(R.string.since)+ " " + lastTime + " (" + DateManager.formatTime(time) + ")");
    }

}
