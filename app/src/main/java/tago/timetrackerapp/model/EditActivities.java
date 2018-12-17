package tago.timetrackerapp.model;

import java.util.ArrayList;
import java.util.List;

import tago.timetrackerapp.repo.entities.ActivityEntity;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.repo.db.AppDatabase;

public class EditActivities {

    public static final EditActivities instance = new EditActivities();

    private List<Category> activities;

    private EditActivities() {}

    /**
     * Changes state to edit old activity
     * @param activity
     */
    public void editActivity(Activity activity) {
        EditActivity.instance.editOldActivity(activity);
    }

    /**
     * Changes state to edit new category
     */
    public void newActivity() {
        EditActivity.instance.editNewActivity();
    }

    /**
     * Loads all activities stored
     * @return
     */
    public List<Activity> load() {
        AppDatabase db = AppDatabase.getInstance(null);
        List<Activity> list = new ArrayList<>();
        for (ActivityEntity e : db.activityDao().getAll())
            ;//list.add(Converter.toModel(e));
        return list;
    }
}
