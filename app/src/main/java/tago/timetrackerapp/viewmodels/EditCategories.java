package tago.timetrackerapp.viewmodels;

import java.util.List;

import tago.timetrackerapp.repo.db.CategoryDBHelper;
import tago.timetrackerapp.repo.entities.Category;

/**
 * A class ment
 */
public class EditCategories {

    public static final EditCategories instance = new EditCategories();

    private EditCategories() {}

    /**
     * Loads all categories stored
     * @return
     */
    public List<Category> load() {
        return CategoryDBHelper.getInstance().getAll();
    }

}
