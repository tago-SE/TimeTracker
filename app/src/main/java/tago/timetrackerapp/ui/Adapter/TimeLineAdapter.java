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

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.ViewHolder> {

    private final Context context;
    private List<TimeLog> timeLogs;

    /*

    // Labels not yet implemented

    private List<Item> items = new ArrayList<>();
    private abstract class Item {

    }

    private class DataItem extends Item{
        TimeLog data;

    }

    private class HeaderItem extends Item {
        public String date;
    }
    */

    public TimeLineAdapter(Context context, List<TimeLog> timeLogs) {
        this.timeLogs = timeLogs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.frame_time_event, viewGroup, false);
        return new TimeLineAdapter.ViewHolder(v);
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
        long time = DateManager.millisecondsBetweenDates(data.start, data.stop, "yyyy-MM-dd HH:mm:ss");
        String start = DateManager.reformatDate(data.start, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        String stop = DateManager.reformatDate(data.stop, "yyyy-MM-dd HH:mm:ss", "HH:mm");
        viewHolder.duration.setText(DateManager.formatTime(time));
        viewHolder.startStop.setText(start + " - " + stop);
    }

    @Override
    public int getItemCount() {
        return timeLogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView name;
        public TextView startStop;
        public TextView duration;

        private ViewHolder(View v) {
            super(v);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            startStop = itemView.findViewById(R.id.start_stop);
            duration = itemView.findViewById(R.id.time);
        }
    }
}