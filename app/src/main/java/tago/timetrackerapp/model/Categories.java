package tago.timetrackerapp.model;

import java.util.List;

import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.local.db.AppDatabase;

/**
 * A class ment
 */
public class Categories {

    public static final Categories instance = new Categories();

    private Categories() {}

    /**
     * Loads all categories stored
     * @return
     */
    public List<Category> load() {
        AppDatabase db = AppDatabase.getInstance(null);
        return db.categoryDao().getAll();
    }

}
