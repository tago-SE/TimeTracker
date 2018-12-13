package tago.timetrackerapp.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import tago.timetrackerapp.ui.util.SingleLiveEvent;

public class EditCategoryVM  extends ViewModel{

    public final MutableLiveData<Integer> colorLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> nameLiveData = new MutableLiveData<>();
    public final SingleLiveEvent<Integer> saveLiveData = new SingleLiveEvent<>();

    public static final int SAVE_ERR        = 0;
    public static final int SAVE_OK         = 1;
    public static final int SAVE_NO_NAME    = 2;
    public static final int SAVE_NO_COLOR   = 3;

    public EditCategoryVM() {
        // Empty constructor
    }

    public void setColor(int color) {
        colorLiveData.setValue(color);
    }

    public void setName(String name) {
        nameLiveData.setValue(name);
    }

    public void save() {
        String name = nameLiveData.getValue();
        if (name.equals("")) {
            saveLiveData.postValue(SAVE_NO_NAME);
        }
    }

}
