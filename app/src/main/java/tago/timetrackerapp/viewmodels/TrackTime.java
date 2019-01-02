package tago.timetrackerapp.viewmodels;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.db.ActivityDBHelper;
import tago.timetrackerapp.repo.entities.Activity;

public class TrackTime {

    public static final TrackTime instance = new TrackTime();

    private boolean selectingMultiple = false;
    private List<Activity> selectedActivities = new ArrayList<>();
    private List<Activity> activities = null;

    private long timestamp;

    private TrackTime() {}

    public List<Activity> getActivities() {
        ActivityDBHelper helper = ActivityDBHelper.getInstance();
        long lastModified = helper.getLastModifiedMilliseconds();
        if (activities == null || lastModified != timestamp) {
            activities = helper.getAll();
            timestamp = lastModified;
            selectedActivities.clear();
        }
        return activities;
    }

    public boolean isSelectingMultiple() {
        return selectingMultiple;
    }

    public void enableBottomMenu() {
        selectingMultiple = true;
    }

    public void onClickActivity(Activity activity) {
        activity.selected = !activity.selected;
        if (activity.selected) {
            selectedActivities.add(activity);
        } else {
            selectedActivities.remove(activity);
        }
    }

    public boolean hasSelection() {
        return !selectedActivities.isEmpty();
    }

    public void cancel() {
        for (Activity activity : activities)
            activity.selected = false;
        selectedActivities.clear();
        selectingMultiple = false;
    }

    public void submit() {
        LogTime.instance.start(selectedActivities);
        cancel();           // Clears selection after passing it
      selectedActivities.clear();
    }
}
