package tago.timetrackerapp.ui.Adapter;


import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;
import tago.timetrackerapp.ui.managers.DateManager;

public abstract class TimeLogsAdapter extends RecyclerView.Adapter<TimeLogsAdapter.ViewHolder> {

    private final Context context;
    private final List<TimeLog> items;
    private long totalMilliseconds;

    private static final int TOTAL_PROGRESS = 100;

    public TimeLogsAdapter(Context context, List<TimeLog> items, long totalMilliseconds) {
        this.context = context;
        this.items = items;
        this.totalMilliseconds = totalMilliseconds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frame_log_time, viewGroup, false);
        return new TimeLogsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
        final TimeLog timeLog = items.get(position);
        Activity activity = timeLog.getActivity();
        if (activity != null) {
            viewHolder.name.setText(activity.name);
        } else {
            viewHolder.name.setText("");
        }
        viewHolder.name.setTextColor(activity.color);
        viewHolder.seekBar.setProgress(timeLog.progress);
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(activity.color, PorterDuff.Mode.MULTIPLY);
        viewHolder.seekBar.getProgressDrawable().setColorFilter(colorFilter);
        viewHolder.setTime(timeLog);
        viewHolder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Limit to global progress of 100 shared between multiple ´seek bars.
                int remaining = remaining();
                int prevProgress = timeLog.progress;
                int changed = progress - prevProgress;
                if (changed < 0) {
                    timeLog.progress = progress;
                } else if (remaining == 0) {
                    seekBar.setProgress(prevProgress);
                    progress = prevProgress;
                } else {
                    if (changed > remaining) {
                        progress = remaining + prevProgress;
                        seekBar.setProgress(progress);
                    }
                    timeLog.progress = progress;
                };
                // Update and notify of changes
                viewHolder.setTime(timeLog);
                onSeekBarChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private int remaining() {
        int remaining = TOTAL_PROGRESS;
        for (TimeLog timeLog : items)
            remaining -= timeLog.progress;
        if (remaining >= 100)
            return 100;
        else if (remaining <= 0)
            return 0;
        return remaining;
    }

    private int storedProgress() {
        int totalProgress = 0;
        for (TimeLog timeLog : items)
            totalProgress += timeLog.progress;
        return totalProgress;
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView name;
        public TextView time;
        public SeekBar seekBar;

        private ViewHolder(View v) {
            super(v);
            //v.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            seekBar = itemView.findViewById(R.id.seekBar);
        }

        @Override
        public void onClick(View v) {
            // Not yet implemented
        }

        protected void setTime(TimeLog timeLog) {
            double factor = (double) timeLog.progress/TOTAL_PROGRESS;
            timeLog.milliseconds = (long) (factor*totalMilliseconds);
            time.setText("" + DateManager.formatTime(timeLog.milliseconds));
        }
    }

    public abstract void onSeekBarChanged();

}

