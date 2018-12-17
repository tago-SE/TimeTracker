package tago.timetrackerapp.repo.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

import tago.timetrackerapp.model.Activity;

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
    private List<Activity> activities;

    public Category() {
        // Required empty constructor
    }

    @Ignore
    public Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public boolean hasActivities() {
        return activities != null && !activities.isEmpty();
    }

    public String getActivitiesString() {
        return "";
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
