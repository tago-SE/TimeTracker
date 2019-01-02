package tago.timetrackerapp.repo.db;

import java.util.List;

import tago.timetrackerapp.repo.db.daos.TimeLogDao;
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
        return timeLogDao.get(id);
    }

    public List<TimeLog> getAll() {
        return timeLogDao.getAll();
    }

    public TimeLog getLast() {
        return timeLogDao.getLast();
    }
}
