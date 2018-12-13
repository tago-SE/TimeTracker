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

    public void setColor(int color) {
        colorLiveData.setValue(color);
    }

    public void setName(String name) {
        nameLiveData.setValue(name);
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
        categoryService.createCategory(new Category(name, color));
        saveLiveData.postValue(SAVE_OK);
    }

    public void delete() {

    }

}
