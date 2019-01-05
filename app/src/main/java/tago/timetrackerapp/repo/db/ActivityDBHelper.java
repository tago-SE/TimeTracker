package tago.timetrackerapp.repo.db;

import java.util.List;

import tago.timetrackerapp.repo.db.daos.ActivityDao;
import tago.timetrackerapp.repo.db.daos.CategoryDao;
import tago.timetrackerapp.repo.entities.Activity;

public class ActivityDBHelper extends BaseDBHelper {

    private ActivityDao activityDao;
    private CategoryDao categoryDao;

    private static ActivityDBHelper instance = null;

    public static ActivityDBHelper getInstance() {
        if (instance == null) {
            instance = new ActivityDBHelper(AppDatabase.getInstance(null));
        }
        return instance;
    }

    private ActivityDBHelper(AppDatabase db) {
        categoryDao = db.categoryDao();
        activityDao = db.activityDao();
    }

    public void insertOrUpdate(Activity... activities) {
        activityDao.insertOrUpdate(activities);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void insert(Activity... activities) {
        activityDao.insert(activities);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void update(Activity... activities) {
        activityDao.update(activities);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void delete(Activity... activities) {
        activityDao.delete(activities);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public Activity get(long id) {
        Activity activity = activityDao.get(id);
        getForeignData(activity);
        return activity;
    }

    public List<Activity> getAll() {
        List<Activity> activities = activityDao.getAll();
        for (Activity activity : activities) {
            getForeignData(activity);
        }
        return activities;
    }

    private void getForeignData(Activity activity) {
        if (activity == null)
            return;
        activity.setCategory(categoryDao.get(activity.categoryId));
    }

    private void updateForeignData(Activity activity) {
        // Not yet implemented
    }

}
