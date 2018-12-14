package tago.timetrackerapp.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import tago.timetrackerapp.repo.model.Category;
import tago.timetrackerapp.repo.services.CategoryService;
import tago.timetrackerapp.ui.util.SingleLiveEvent;

public class EditCategoryVM  extends ViewModel{

    // Save responses
    public static final int SAVE_ERR        = 0;
    public static final int SAVE_OK         = 1;
    public static final int SAVE_NO_NAME    = 2;
    public static final int SAVE_NO_COLOR   = 3;

    private Category startingCategory;

    // Observable live data
    public final MutableLiveData<Integer> colorLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> nameLiveData = new MutableLiveData<>();
    public final SingleLiveEvent<Integer> saveLiveData = new SingleLiveEvent<>();

    // Service to save data
    private final CategoryService categoryService;

    /**
     * Constructor
     */
    public EditCategoryVM() {
        categoryService = CategoryService.getInstance();
    }

    public void setStartingCategory(Category category) {
        startingCategory = category;
        setColor(category.getColor());
        setName(category.getName());
    }

    /**
     * Changes color configuration.
     * @param color
     */
    public void setColor(int color) {
        colorLiveData.setValue(color);
    }

    /**
     * Changes name configuration.
     * @param name
     */
    public void setName(String name) {
        nameLiveData.setValue(name);
    }

    /**
     * Returns true if the Category configurations has changed
     * @return
     */
    public boolean hasChanged() {
        // Compare name and color to starting category to determine if changes were made
        if (startingCategory != null) {
            return !startingCategory.getName().equals(nameLiveData.getValue()) ||
                    startingCategory.getColor() != colorLiveData.getValue();
        }
        return nameLiveData.getValue() != null;
    }

    /**
     * Sends a request to save the specified category to the CategoryService if the set color and
     * name is valid. Also posts a response back to the observer depending on result.
     */
    public void save() {
        String name = nameLiveData.getValue();
        if (name == null || name.equals("")) {
            saveLiveData.postValue(SAVE_NO_NAME);
            return;
        }
        int color = colorLiveData.getValue();
        if (color == 0) {
            saveLiveData.postValue(SAVE_NO_COLOR);
            return;
        }
        if (startingCategory == null) {
            // Save new Category
            categoryService.createCategory(new Category(name, color));
        } else {
            // Update old Category
            startingCategory.setColor(color);
            startingCategory.setName(name);
            categoryService.createCategory(startingCategory);

        }
        saveLiveData.postValue(SAVE_OK);
    }

    public void delete() {
        if (startingCategory != null) {
            categoryService.deleteCategory(startingCategory);
        }
    }

}
