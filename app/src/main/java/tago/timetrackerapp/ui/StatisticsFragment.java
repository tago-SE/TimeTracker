package tago.timetrackerapp.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.Adapter.StatisticsAdapter;
import tago.timetrackerapp.viewmodels.Statistics;

public class StatisticsFragment extends Fragment {

    private static final String TAG = "Statistics";

    private RecyclerView recyclerView;
    private TextView label;
    private ImageView left;
    private ImageView right;
    private Button day, week, month, year;

    private Statistics model = Statistics.instance;


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
        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        left = view.findViewById(R.id.left);
        left.setClickable(true);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "moveLeft");
                model.moveLeft();
                updateUI();
            }
        });
        right = view.findViewById(R.id.right);
        right.setClickable(true);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w(TAG, "moveRight");
                model.moveRight();
                updateUI();
            }
        });
        label = view.findViewById(R.id.label);
        day = view.findViewById(R.id.day);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.toggleDay();
                updateUI();
            }
        });
        week = view.findViewById(R.id.week);
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.toggleWeek();
                updateUI();
            }
        });
        month = view.findViewById(R.id.month);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.toggleMonth();
                updateUI();
            }
        });
        year = view.findViewById(R.id.year);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.toggleYear();
                updateUI();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        StatisticsAdapter adapter = new StatisticsAdapter(getContext(), model.load());
        recyclerView.setAdapter(adapter);
        label.setText(model.getLabel());
        // Setup left right movers
        left.setClickable(model.canMoveLeft());
        right.setClickable(model.canMoveRight());
        if (model.canMoveRight())
            right.setColorFilter(Color.BLACK);
        else
            right.setColorFilter(Color.GRAY);
        if (model.canMoveLeft())
            left.setColorFilter(Color.BLACK);
        else
            left.setColorFilter(Color.GRAY);
        // Color toggle buttons
        String currentToggle = model.currentToggle();
        if (currentToggle.equals("Day"))
            day.setTextColor(Color.WHITE);
        else
            day.setTextColor(Color.BLACK);
        if (currentToggle.equals("Week"))
            week.setTextColor(Color.WHITE);
        else
            week.setTextColor(Color.BLACK);
        if (currentToggle.equals("Month"))
            month.setTextColor(Color.WHITE);
        else
            month.setTextColor(Color.BLACK);
        if (currentToggle.equals("Year"))
            year.setTextColor(Color.WHITE);
        else
            year.setTextColor(Color.BLACK);
    }
}
