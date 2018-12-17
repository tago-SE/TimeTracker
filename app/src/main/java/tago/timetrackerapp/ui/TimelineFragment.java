package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tago.timetrackerapp.R;

public class TimelineFragment extends Fragment {

    public TimelineFragment() {
        // Required empty constructor
    }

    /**
     * Factory method for creating a TrackTimeFragment.
     */
    public static TimelineFragment newInstance() {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        return view;
    }
}
