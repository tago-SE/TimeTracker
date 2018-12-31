package tago.timetrackerapp.ui.Adapter;


import android.content.Context;
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

public class TimeLogsAdapter extends RecyclerView.Adapter<TimeLogsAdapter.ViewHolder> {

    private final Context context;
    private final List<TimeLog> items;

    public TimeLogsAdapter(Context context, List<TimeLog> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.frame_log_time, viewGroup, false);
        return new TimeLogsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        TimeLog timeLog = items.get(position);
        Activity activity = timeLog.getActivity();

        System.out.println("TimeLogsAdapter: " + activity.name);
        if (activity != null) {
            viewHolder.name.setText(activity.name);
        } else {
            viewHolder.name.setText("");
        }
        viewHolder.time.setText("0 min");
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
            v.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            seekBar = itemView.findViewById(R.id.seekBar);
        }

        @Override
        public void onClick(View v) {
            // Not yet implemented
        }
    }
}

