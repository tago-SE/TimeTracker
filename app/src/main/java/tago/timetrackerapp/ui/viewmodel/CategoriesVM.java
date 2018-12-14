package tago.timetrackerapp.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import tago.timetrackerapp.repo.model.Category;
import tago.timetrackerapp.repo.services.CategoryService;

public class CategoriesVM extends ViewModel {


    public final MutableLiveData<List<Category>> categoriesLiveData = new MutableLiveData<>();

    // Service to save data
    private final CategoryService categoryService;

    /**
     * Constructor
     */
    public CategoriesVM() {
        categoryService = CategoryService.getInstance();
    }

    public void load() {
        categoriesLiveData.postValue(categoryService.getAllCategories());
    }
}
