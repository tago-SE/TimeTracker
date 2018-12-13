package tago.timetrackerapp.repo.services;

import tago.timetrackerapp.repo.Converter;
import tago.timetrackerapp.repo.model.Category;
import tago.timetrackerapp.repo.local.daos.CategoryDao;
import tago.timetrackerapp.repo.entities.CategoryEntity;
import tago.timetrackerapp.repo.local.AppDatabase;


public class CategoryService {
    private static final CategoryService ourInstance = new CategoryService();

    public static CategoryService getInstance() {
        return ourInstance;
    }

    private CategoryService() {

    }

    public void createCategory(Category category) {
        insertLocalPersistence(Converter.toEntity(category));
    }

    private void insertLocalPersistence(CategoryEntity categoryEntity) {
        AppDatabase db = AppDatabase.getInstance(null);
        CategoryDao dao = db.categoryDao();
        dao.insertAll(categoryEntity);
    }


}
