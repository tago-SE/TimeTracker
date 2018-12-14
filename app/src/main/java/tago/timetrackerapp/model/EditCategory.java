package tago.timetrackerapp.model;

import tago.timetrackerapp.repo.Converter;
import tago.timetrackerapp.repo.local.AppDatabase;
import tago.timetrackerapp.ui.util.Colorizer;

public class EditCategory {

    public static final EditCategory instance = new EditCategory();

    public static final int SAVE_ERR            = 0;
    public static final int SAVE_OK             = 1;
    public static final int SAVE_NO_NAME        = 2;
    public static final int SAVE_NO_COLOR       = 3;
    public static final int SAVE_ERR_NO_CHANGES = 4;

    private static final int STATE_ADD       = 1;
    private static final int STATE_EDIT      = 2;
    private int state;

    private Category category;
    private Category startingCategory;

    private EditCategory() {}

    protected void editNewCategory() {
        category = new Category("", Colorizer.getRandomColor());
        setupStartingCategory();
        state = STATE_ADD;
    }

    protected void editOldCategory(Category category) {
        this.category = category;
        setupStartingCategory();
        state = STATE_EDIT;
    }

    private void setupStartingCategory() {
        startingCategory = new Category();
        startingCategory.setColor(category.getColor());
        startingCategory.setName(category.getName());
    }

    public boolean hasChanged() {
        String name1 = startingCategory.getName();
        String name2 = category.getName();
        return !name1.equals(name2) ||
                startingCategory.getColor() != category.getColor();

    }

    public void setName(String name) {
        category.setName(name);
    }

    public void setColor(int color) {
        category.setColor(color);
    }

    public int getColor() {
        return category.getColor();
    }

    public String getName() {
        return category.getName();
    }

    public boolean isEditState() {
        return state == STATE_EDIT;
    }

    public boolean isAddState() {
        return state == STATE_ADD;
    }

    public int save() {
        if (!hasChanged())
            return SAVE_ERR_NO_CHANGES;
        String name = category.getName();
        if (name == null || name.equals(""))
            return SAVE_NO_NAME;
        if (category.getColor() == 0)
            return SAVE_NO_COLOR;
        AppDatabase db = AppDatabase.getInstance(null);
        db.categoryDao().insertOrUpdate(Converter.toEntity(category));
        return SAVE_OK;
    }

    public void delete() {
        AppDatabase db = AppDatabase.getInstance(null);
        db.categoryDao().delete(Converter.toEntity(category));
    }
}
