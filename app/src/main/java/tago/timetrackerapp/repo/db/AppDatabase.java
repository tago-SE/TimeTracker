package tago.timetrackerapp.repo.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import tago.timetrackerapp.repo.db.daos.ActivityDao;
import tago.timetrackerapp.repo.db.daos.CategoryDao;
import tago.timetrackerapp.repo.db.daos.TimeLogDao;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.entities.TimeLog;

@Database(entities = {Activity.class, Category.class, TimeLog.class}, version = 15, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "user-database";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context c) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(c, AppDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    protected abstract ActivityDao activityDao();

    protected abstract CategoryDao categoryDao();

    protected abstract TimeLogDao timeLogDao();

}
