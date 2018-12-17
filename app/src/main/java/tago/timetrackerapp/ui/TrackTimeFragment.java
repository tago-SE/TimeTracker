package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tago.timetrackerapp.R;

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
}
