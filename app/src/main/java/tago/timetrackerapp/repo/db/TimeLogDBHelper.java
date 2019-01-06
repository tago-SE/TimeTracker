package tago.timetrackerapp.repo.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import tago.timetrackerapp.repo.db.daos.ActivityDao;
import tago.timetrackerapp.repo.db.daos.TimeLogDao;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;

public class TimeLogDBHelper extends BaseDBHelper {

    private static TimeLogDBHelper instance = null;

    private TimeLogDao timeLogDao;

    public static TimeLogDBHelper getInstance() {
        if (instance == null) {
            instance = new TimeLogDBHelper(AppDatabase.getInstance(null));
        }
        return instance;
    }

    private TimeLogDBHelper(AppDatabase db) {
        timeLogDao = db.timeLogDao();
    }

    public void insertOrUpdate(TimeLog... timeLogs) {
        timeLogDao.insertOrUpdate(timeLogs);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void insert(TimeLog... timeLogs) {
        timeLogDao.insert(timeLogs);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void update(TimeLog... timeLogs) {
        timeLogDao.update(timeLogs);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void delete(TimeLog... timeLogs) {
        timeLogDao.delete(timeLogs);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public TimeLog get(long id) {
        TimeLog t = timeLogDao.get(id);
        getForeignData(t);
        return t;
    }

    public List<TimeLog> getAll() {
        List<TimeLog> list = timeLogDao.getAll();
        for (TimeLog t : list)
            getForeignData(t);
        return list;
    }

    public List<TimeLog> getAllDescending() {
        List<TimeLog> list = timeLogDao.getAllDescending();
        for (TimeLog t : list)
            getForeignData(t);
        return list;
    }

    public TimeLog getLast() {
        TimeLog t = timeLogDao.getLast();
        getForeignData(t);
        return t;
    }

    public List<TimeLog> getRange(String startDate, String stopDate) {
        List<TimeLog> list = timeLogDao.getRange(startDate, stopDate);
        for (TimeLog t : list)
            getForeignData(t);
        return list;
    }

    /**
     * Aggregates the records based on the activity yielding list containing the  total sum of
     * time spent within the given range in milliseconds.
     * @param startDate
     * @param endDate
     * @return
     */
    public List<TimeLog> getSumRange(String startDate, String endDate) {
        Hashtable<Long, TimeLog> resultSet = new Hashtable<>();
        for (TimeLog t : getRange(startDate, endDate)) {
            if (t.getActivity() == null)
                continue; // skip
            TimeLog resultTime;
            if (!resultSet.containsKey(t.activityId)) {
                resultTime = new TimeLog();
                resultTime.setActivity(t.getActivity());
                resultSet.put(t.activityId,resultTime);
            } else {
                resultTime = resultSet.get(t.activityId);
            }
            resultTime.milliseconds += t.milliseconds;
        }
        return new ArrayList<>(resultSet.values());
    }

    private void getForeignData(TimeLog timeLog) {
        if (timeLog == null) {
            return;
        }
        ActivityDBHelper helper = ActivityDBHelper.getInstance();
        Activity a = helper.get(timeLog.activityId);
        timeLog.setActivity(a);
    }
}
