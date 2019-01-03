package tago.timetrackerapp.viewmodels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tago.timetrackerapp.repo.db.ActivityDBHelper;
import tago.timetrackerapp.repo.db.TimeLogDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;

public class TrackTime {

    public static final TrackTime instance = new TrackTime();

    private boolean selectingMultiple = false;
    private List<Activity> selectedActivities = new ArrayList<>();
    private List<Activity> activities = null;
    private long milliseconds;
    private Date lastDate;

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

    public Date getLastTrackedDate() {
        return lastDate;
    }

    public long getMillisecondsSinceLastTrack() {
        TimeLogDBHelper timeLogDBHelper = TimeLogDBHelper.getInstance();
        TimeLog timeLog = timeLogDBHelper.getLast();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        lastDate = null;
        try {
            lastDate = dateFormat.parse(timeLog.stop);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        milliseconds = getTimeSinceDate(lastDate);
        return milliseconds;
    }

    private long getTimeSinceDate(Date date) {
        return (new Date()).getTime() - date.getTime();
    }

    public void cancel() {
        for (Activity activity : activities)
            activity.selected = false;
        selectedActivities.clear();
        selectingMultiple = false;
    }

    public void submit() {
        LogTime.instance.start(selectedActivities, lastDate, milliseconds);
        cancel();           // Clears selection after passing it
        selectedActivities.clear();
    }

}
