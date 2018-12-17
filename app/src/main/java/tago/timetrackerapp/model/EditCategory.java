package tago.timetrackerapp.model;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.local.db.AppDatabase;
import tago.timetrackerapp.ui.util.Colorizer;

public class EditCategory {

    public static final EditCategory instance = new EditCategory();

    public static final int SAVE_ERR            = 0;
    public static final int SAVE_OK             = 1;
    public static final int SAVE_NO_NAME        = 2;
    public static final int SAVE_NO_COLOR       = 3;

    private static final int STATE_ADD       = 1;
    private static final int STATE_EDIT      = 2;

    private int state;
    private Category category;
    private Category startingCategory;

    private EditCategory() {}

    public void editNewCategory() {
        category = new Category("", Colorizer.getRandomColor());
        setupStartingCategory();
        state = STATE_ADD;
    }

    public void editOldCategory(Category category) {
        this.category = category;
        setupStartingCategory();
        state = STATE_EDIT;
    }

    private void setupStartingCategory() {
        startingCategory = new Category(category.name, category.color);
    }

    public boolean hasChanged() {
        String name1 = startingCategory.name;
        String name2 = category.name;
        return !name1.equals(name2) ||
                startingCategory.color != category.color;

    }

    public void setName(String name) {
        category.name = name;
    }

    public void setColor(int color) {
        category.color = color;
    }

    public int getColor() {
        return category.color;
    }

    public String getName() {
        return category.name;
    }

    public boolean isEditState() {
        return state == STATE_EDIT;
    }

    public boolean isAddState() {
        return state == STATE_ADD;
    }

    public Observable<Integer> save() {
        return Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                String name = category.name;
                if (name == null || name.equals(""))
                    return Observable.error(new Exception("" + SAVE_NO_NAME));
                if (category.color == 0)
                    return Observable.error(new Exception("" + SAVE_NO_COLOR));
                if (!hasChanged())
                    return Observable.just(SAVE_OK);
                AppDatabase db = AppDatabase.getInstance(null);
                db.categoryDao().insertOrUpdate(category);
                return Observable.just(SAVE_OK);
            }
        });
    }

    public void delete() {
        AppDatabase db = AppDatabase.getInstance(null);
        db.categoryDao().delete(category);
    }
}
