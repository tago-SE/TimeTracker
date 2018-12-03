package tago.timetrackerapp.repo.local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import tago.timetrackerapp.repo.daos.ActivityDao;
import tago.timetrackerapp.repo.daos.CategoryDao;
import tago.timetrackerapp.repo.entities.ActivityEntity;
import tago.timetrackerapp.repo.entities.CategoryEntity;

@Database(entities = {ActivityEntity.class, CategoryEntity.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context c) {
        if (instance == null) {
            instance = Room
                    .databaseBuilder(c, AppDatabase.class, "user-database")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract ActivityDao activityDao();

    public abstract CategoryDao categoryDao();


}
