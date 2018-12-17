package tago.timetrackerapp.repo.local.daos;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.local.db.AppDatabase;

public class CategoryDBHelper {

    private static List<Category> categoryList = new ArrayList<>();

    private CategoryDao categoryDao;

    public CategoryDBHelper(AppDatabase db) {
        categoryDao = db.categoryDao();
    }

    public List<Category> getCategories() {
        //List<CategoryEntity> categoryEntities = categoryDao.getAll();




        return null;
    }

    public static void save(Category... categories) {

    }

    public static List<Category> loadAll() {

        return null;
    }
}
