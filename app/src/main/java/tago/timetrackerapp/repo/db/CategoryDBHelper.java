package tago.timetrackerapp.repo.db;

import java.util.List;

import tago.timetrackerapp.repo.daos.CategoryDao;
import tago.timetrackerapp.repo.entities.Category;

public class CategoryDBHelper {

    private CategoryDao categoryDao;

    private static CategoryDBHelper instance = null;

    public static CategoryDBHelper getInstance() {
        if (instance == null) {
            instance = new CategoryDBHelper(AppDatabase.getInstance(null));
        }
        return instance;
    }

    private CategoryDBHelper(AppDatabase db) {
        categoryDao = db.categoryDao();
    }

    public void insertOrUpdate(Category... categories) {
        categoryDao.insertOrUpdate(categories);
    }

    public void insert(Category... categories) {
        categoryDao.insert(categories);
    }

    public void update(Category... categories) {
        categoryDao.update(categories);
    }

    public void delete(Category... categories) {
        categoryDao.delete(categories);
    }

    public Category get(long id) {
        return categoryDao.get(id);
    }

    public List<Category> getAll() {
        return categoryDao.getAll();
    }

}
