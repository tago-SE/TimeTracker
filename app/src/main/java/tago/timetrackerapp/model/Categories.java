package tago.timetrackerapp.model;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.entities.CategoryEntity;
import tago.timetrackerapp.repo.local.EMConverter;
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
        List<Category> categories = new ArrayList<>();
        for (CategoryEntity e : db.categoryDao().getAll())
            categories.add(EMConverter.toModel(e));
        return categories;
    }

}
