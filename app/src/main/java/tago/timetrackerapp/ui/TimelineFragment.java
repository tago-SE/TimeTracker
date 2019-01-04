package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.db.TimeLogDBHelper;
import tago.timetrackerapp.repo.entities.TimeLog;
import tago.timetrackerapp.ui.Adapter.TimeLineAdapter;

public class TimelineFragment extends Fragment {

    private RecyclerView recyclerView;

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
        // Setup recycle view
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        TimeLogDBHelper helper = TimeLogDBHelper.getInstance();
        List<TimeLog> timeLogs = helper.getAllDescending();
        TimeLineAdapter timeLineAdapter = new TimeLineAdapter(getContext(), timeLogs);
        recyclerView.setAdapter(timeLineAdapter);
    }
}
