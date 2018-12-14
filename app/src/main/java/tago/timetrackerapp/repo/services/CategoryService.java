package tago.timetrackerapp.repo.services;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.Converter;
import tago.timetrackerapp.repo.entities.CategoryEntity;
import tago.timetrackerapp.repo.local.AppDatabase;
import tago.timetrackerapp.repo.local.daos.CategoryDao;
import tago.timetrackerapp.repo.model.Category;

public class CategoryService {
    private static final CategoryService ourInstance = new CategoryService();

    public static CategoryService getInstance() {
        return ourInstance;
    }

    private CategoryService() {
    }

    public void createCategory(Category category) {
        AppDatabase db = AppDatabase.getInstance(null);
        CategoryDao dao = db.categoryDao();
        dao.insertOrUpdate(Converter.toEntity(category));
    }

    public void deleteCategory(Category category) {
        AppDatabase db = AppDatabase.getInstance(null);
        db.categoryDao().delete(Converter.toEntity(category));
    }


    public List<Category> getAllCategories() {
        AppDatabase db = AppDatabase.getInstance(null);
        List<Category> categoryList = new ArrayList<>();
        for (CategoryEntity e : db.categoryDao().getAll())
            categoryList.add(Converter.toModel(e));
        return categoryList;
    }

}
