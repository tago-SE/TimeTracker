package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.ui.Adapter.ActivitiesAdapter;
import tago.timetrackerapp.viewmodels.EditActivities;

public class TrackTimeFragment extends Fragment {

    public TrackTimeFragment() {
        // Required empty constructor
    }

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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        GridView gridView = getView().findViewById(R.id.grid);
        gridView.setVerticalSpacing(12);
        gridView.setHorizontalSpacing(12);
        gridView.setNumColumns(4);
        // Populate category list
        List<Activity> activities = EditActivities.instance.load();
        ActivitiesAdapter activitiesAdapter = new ActivitiesAdapter(getContext(), activities) {
            @Override
            public void onActivitySelected(Activity activity) {
                // Clicked activity
            }
        };
        gridView.setAdapter(activitiesAdapter);

    }



}
