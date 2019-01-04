package tago.timetrackerapp.repo.db;

import java.util.List;

import tago.timetrackerapp.repo.db.daos.ActivityDao;
import tago.timetrackerapp.repo.db.daos.CategoryDao;
import tago.timetrackerapp.repo.entities.Category;

public class CategoryDBHelper extends BaseDBHelper {

    private CategoryDao categoryDao;
    private ActivityDao activityDao;

    private static CategoryDBHelper instance = null;

    public static CategoryDBHelper getInstance() {
        if (instance == null) {
            instance = new CategoryDBHelper(AppDatabase.getInstance(null));
        }
        return instance;
    }

    private CategoryDBHelper(AppDatabase db) {
        categoryDao = db.categoryDao();
        activityDao = db.activityDao();
    }

    public void insertOrUpdate(Category... categories) {
        categoryDao.insertOrUpdate(categories);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void insert(Category... categories) {
        categoryDao.insert(categories);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void update(Category... categories) {
        categoryDao.update(categories);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public void delete(Category... categories) {
        categoryDao.delete(categories);
        timestampMilliseconds = System.currentTimeMillis();
    }

    public Category get(long id) {
        Category category = categoryDao.get(id);
        getForeignData(category);
        return category;
    }

    public List<Category> getAll() {
        List<Category> categories = categoryDao.getAll();
        for (Category category : categories)
            getForeignData(category);
        return categories;
    }

    private void getForeignData(Category category) {
        category.activities = activityDao.getAllByCategoryId(category.id);
    }

}
