package tago.timetrackerapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tago.timetrackerapp.R;

public class StatisticsFragment extends Fragment {


    public StatisticsFragment() {
        // Required empty constructor
    }

    /**
     * Factory method for creating a TrackTimeFragment.
     */
    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        /*
        TimeLogDBHelper helper = TimeLogDBHelper.getInstance();

        for (int i = 0; i < 30; i++) {
            String day = "0" + i;
            if (i > 9)
                day = "" + i;
            Date d = DateManager.stringToDate("2018-07-" + day + " 00:00:00","yyyy-MM-dd HH:mm:ss");
            TimeLog t = new TimeLog();
            t.start = DateManager.dateToString(d);
            t.stop = DateManager.dateToString(d);
            //helper.insert(t);
        }

        List<TimeLog> queryResult = helper.getRange("2018-07-01", "2018-07-05");
        for (TimeLog t : queryResult) {
            System.out.println("date: " + t.start);
        }
        System.out.println("RESULT: " + queryResult.toString());
        System.out.println("size " + queryResult.size());
        */
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
