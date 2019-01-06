package tago.timetrackerapp.ui.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;
import tago.timetrackerapp.ui.managers.DateManager;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private final Context context;
    private List<TimeLog> timeLogs;

    public StatisticsAdapter(Context context, List<TimeLog> timeLogs) {
        this.timeLogs = timeLogs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.frame_activity_statistics, viewGroup, false);
        return new StatisticsAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TimeLog data = timeLogs.get(position);
        Activity activity = data.getActivity();
        if (activity == null) {
            timeLogs.remove(position);
            return;
        }
        viewHolder.name.setText(activity.name);
        viewHolder.icon.setColorFilter(activity.color);
        viewHolder.duration.setText(DateManager.formatTime(data.milliseconds));
    }

    @Override
    public int getItemCount() {
        return timeLogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView icon;
        public TextView name;
        public TextView duration;

        private ViewHolder(View v) {
            super(v);
            v.setOnClickListener(this);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            duration = itemView.findViewById(R.id.time);
        }

        @Override
        public void onClick(View v) {
            // Not yet implemented
        }
    }
}