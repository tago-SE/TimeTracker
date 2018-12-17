package tago.timetrackerapp.viewmodels;

import java.util.List;

import tago.timetrackerapp.repo.db.ActivityDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.Category;

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
      return ActivityDBHelper.getInstance().getAll();
    }
}
