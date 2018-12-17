package tago.timetrackerapp.model;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import tago.timetrackerapp.repo.local.daos.ActivityDBHelper;
import tago.timetrackerapp.repo.local.db.AppDatabase;
import tago.timetrackerapp.ui.util.Colorizer;

public class EditActivity {

    public static final EditActivity instance = new EditActivity();

    private static final int STATE_ADD       = 1;
    private static final int STATE_EDIT      = 2;

    private int state;
    private Activity activity;
    private Activity startingActivity;

    private EditActivity() {}

    public void editNewActivity() {
        activity = new Activity("", Colorizer.getRandomColor());
        setupStartingActivity();
        state = STATE_ADD;
    }

    public void editOldActivity(Activity activity) {
        this.activity = activity;
        setupStartingActivity();
        state = STATE_EDIT;
    }

    private void setupStartingActivity() {
        startingActivity = new Activity();
        startingActivity.setName(activity.getName());
        startingActivity.setIcon(activity.getIcon());
        startingActivity.setColor(activity.getColor());
        startingActivity.setCategory(activity.getCategory());
    }

    public boolean hasChanged() {
        String name1 = startingActivity.getName();
        String name2 = activity.getName();
        return !name1.equals(name2) ||
                startingActivity.getColor() != activity.getColor() ||
                startingActivity.getIcon() != activity.getIcon() ||
                (activity.getCategory() != null && !activity.getCategory().equals(startingActivity.getCategory()));
    }

    public void setName(String name) {
        activity.setName(name);
    }

    public String getName() {
        return activity.getName();
    }
    
    public void setColor(int color) {
        activity.setColor(color);
    }

    public int getColor() {
        return activity.getColor();
    }

    public void setIcon(int icon) {
        activity.setIcon(icon);
    }

    public int getIcon() {
        return activity.getIcon();
    }

    public void setCategory(Category category) {
        activity.setCategory(category);
    }

    public Category getCategory() {
        return activity.getCategory();
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

                AppDatabase db = AppDatabase.getInstance(null);
                db.categoryDao().insertOrUpdate();
                return Observable.just(null);
            }
        });
    }

    public void delete() {
        ActivityDBHelper activityDBHelper = new ActivityDBHelper(AppDatabase.getInstance(null));



    }

}
