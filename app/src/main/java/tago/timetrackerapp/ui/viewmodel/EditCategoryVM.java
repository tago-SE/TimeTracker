package tago.timetrackerapp.ui.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class EditCategoryVM  extends ViewModel{

    public final MutableLiveData<Integer> colorLiveData = new MutableLiveData<>();
    public final MutableLiveData<String> nameLiveData = new MutableLiveData<>();

    public EditCategoryVM() {
    }

    public void setColor(int color) {
        colorLiveData.setValue(color);
    }

    public void setName(String name) {
        nameLiveData.setValue(name);
    }

    public void save() {

    }

}
