package tago.timetrackerapp.ui.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.entities.Activity;

public abstract class ActivitiesAdapter extends BaseAdapter {

    private Context context;
    private List<Activity> items;

    public ActivitiesAdapter(Context context, List<Activity> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items == null)
            return 0;
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        if (items == null)
            return null;
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Activity data = items.get(position);
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.frame_activity, null);
        }
        final ImageView icon = convertView.findViewById(R.id.icon);
        final TextView name = convertView.findViewById(R.id.name);
        final ConstraintLayout layout = convertView.findViewById(R.id.layout);
        name.setText(data.name);
        icon.setColorFilter(data.color);
        // Color white when selected
        if (data.selected) {
            layout.setBackgroundColor(Color.WHITE);
        } else {
            layout.setBackgroundColor(data.color);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onActivityClick(data, v, position);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onActivityLongClick(data, v, position);
                return false;
            }
        });
        // Sets the view id to the current adapter position
        return convertView;
    }

    public abstract void onActivityClick(Activity activity, View view, int position);

    public abstract void onActivityLongClick(Activity activity, View view, int position);
}
