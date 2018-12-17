package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity
public class Category {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "color")
    public int color;

    @Ignore
    public List<Activity> activities;

    public Category() {
        // Required empty constructor
    }

    @Ignore
    public Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public boolean hasActivities() {
        return activities != null && !activities.isEmpty();
    }

    public String getActivitiesString() {
        if (!hasActivities())
            return "";  // No activities
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Activity activity : activities) {
            if (!first) {
                sb.append(", ").append(activity.name);
            } else {
                sb.append(activity.name);
                first = false;
            }
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", color=" + color +
                ", activities=" + activities +
                '}';
    }
}
