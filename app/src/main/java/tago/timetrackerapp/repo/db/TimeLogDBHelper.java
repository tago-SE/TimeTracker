package tago.timetrackerapp.repo.db;

import java.util.List;

import tago.timetrackerapp.repo.db.daos.ActivityDao;
import tago.timetrackerapp.repo.db.daos.TimeLogDao;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.TimeLog;

public class TimeLogDBHelper extends BaseDBHelper {

    private static TimeLogDBHelper instance = null;

    private TimeLogDao timeLogDao;
    private ActivityDao activityDao;

    public static TimeLogDBHelper getInstance() {
        if (instance == null) {
            instance = new TimeLogDBHelper(AppDatabase.getInstance(null));
        }
        return instance;
    }

    private TimeLogDBHelper(AppDatabase db) {
        timeLogDao = db.timeLogDao();
        activityDao = db.activityDao();
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

    private void getForeignData(TimeLog timeLog) {
        Activity a = activityDao.get(timeLog.activityId);
        timeLog.setActivity(a);
    }
}
