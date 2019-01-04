package tago.timetrackerapp.viewmodels;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import tago.timetrackerapp.repo.db.ActivityDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.ui.util.Colorizer;

public class EditActivity {

    public static final EditActivity instance = new EditActivity();

    public static final int SAVE_ERR            = 0;
    public static final int SAVE_OK             = 1;
    public static final int SAVE_NO_NAME        = 2;

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
        startingActivity.name = activity.name;
        startingActivity.icon = activity.icon;
        startingActivity.color = activity.color;
        startingActivity.setCategory(activity.getCategory());
    }

    public boolean hasChanged() {
        String name1 = startingActivity.name;
        String name2 = activity.name;
        return !name1.equals(name2) ||
                startingActivity.color != activity.color ||
                startingActivity.icon != activity.icon ||
                (activity.getCategory() != null && !activity.getCategory().equals(startingActivity.getCategory()));
    }

    public void setName(String name) {
        activity.name = name;
    }

    public String getName() {
        return activity.name;
    }
    
    public void setColor(int color) {
        activity.color = color;
    }

    public int getColor() {
        return activity.color;
    }

    public void setIcon(int icon) {
        activity.icon = icon;
    }

    public int getIcon() {
        return activity.icon;
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
                    String name = activity.name;
                    if (activity.name == null || name.equals(""))
                        return Observable.error(new Exception("" + SAVE_NO_NAME));
                    if (!hasChanged())
                        return Observable.just(SAVE_OK);
                    ActivityDBHelper.getInstance().insertOrUpdate(activity);
                    return Observable.just(SAVE_OK);
                }
            });
    }

    public void delete() {
        if (activity != null) {
            ActivityDBHelper.getInstance().delete(activity);
            activity = null;
        }
    }
}
