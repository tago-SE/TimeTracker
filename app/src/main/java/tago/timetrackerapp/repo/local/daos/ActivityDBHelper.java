package tago.timetrackerapp.repo.local.daos;

import java.util.List;

import tago.timetrackerapp.repo.entities.ActivityEntity;
import tago.timetrackerapp.repo.local.db.AppDatabase;

public class ActivityDBHelper {

    private ActivityDao activityDao;
    private CategoryDao categoryDao;


    public ActivityDBHelper(AppDatabase db) {
        this.activityDao = db.activityDao();
        this.categoryDao = db.categoryDao();
    }

    @Deprecated
    public List<ActivityEntity> getAll() {
        List<ActivityEntity> activities = activityDao.getAll();
        for (ActivityEntity entity: activities) {
           // entity.setCategory(categoryDao.get(entity.getCategoryId()));
        }
        return activities;
    }

    public void insertOrUpdate(ActivityEntity ... entities) {
        activityDao.insertOrUpdate(entities);
    }

    public void delete(ActivityEntity ... entities) {
        activityDao.delete(entities);
    }

    public void update(ActivityEntity ... entities) {
        activityDao.update(entities);
    }

}
