package tago.timetrackerapp.model;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.Converter;
import tago.timetrackerapp.repo.entities.CategoryEntity;
import tago.timetrackerapp.repo.local.AppDatabase;

public class Categories {

    public static final Categories instance = new Categories();

    private List<Category> categories;

    private Categories() {}

    /**
     * Changes state to edit old category
     * @param category
     */
    public void editCategory(Category category) {
        EditCategory.instance.editOldCategory(category);
    }

    /**
     * Changes state to edit new category
     */
    public void newCategory() {
        EditCategory.instance.editNewCategory();
    }

    /**
     * Loads all categories stored
     * @return
     */
    public List<Category> load() {
        AppDatabase db = AppDatabase.getInstance(null);
        List<Category> categoryList = new ArrayList<>();
        for (CategoryEntity e : db.categoryDao().getAll())
            categoryList.add(Converter.toModel(e));
        return categoryList;
    }

}
