package tago.timetrackerapp.viewmodels;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;

public class LogTime {

    public static final LogTime instance = new LogTime();
    private List<TimeLog> logTimeList = new ArrayList<>();

    private LogTime() {
        // Required empty constructor
    }

    public void start(List<Activity> activities) {
        logTimeList.clear();
        for (Activity activity : activities) {
            logTimeList.add(new TimeLog(activity));
        }
    }

    public List<TimeLog> getTimeLogs() {
        return logTimeList;
    }

}
