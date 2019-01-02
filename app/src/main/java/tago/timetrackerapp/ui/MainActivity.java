package tago.timetrackerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import tago.timetrackerapp.R;
import tago.timetrackerapp.repo.db.AppDatabase;
import tago.timetrackerapp.repo.db.CategoryDBHelper;
import tago.timetrackerapp.repo.entities.Activity;
import tago.timetrackerapp.repo.entities.Category;
import tago.timetrackerapp.ui.managers.LocaleManager;
import tago.timetrackerapp.ui.util.Colorizer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Required to instantiate local database
        AppDatabase.getInstance(this);

        String country = Locale.getDefault().getCountry();
        String lang = Locale.getDefault().getDisplayLanguage();


        Log.w(TAG, "LANGUAGE: " + lang);
        String language = prefs.getString("language", "default");
        Log.w(TAG, "language: " + language);

        if (language.equals("default")) {
            LocaleManager.setNewLocale(this, null);
        } else {
            LocaleManager.setNewLocale(this, language);
        }

        // Executes only  the first time the app is run
        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
            /* TODO CREATE strings definitions */
            // Setup default categories
            List<Category> categories = new ArrayList<>();
            categories.add(new Category("Rest", Colorizer.getRandomColor()));
            categories.add(new Category("Rest", Colorizer.getRandomColor()));
            categories.add(new Category("Eating", Colorizer.getRandomColor()));
            categories.add(new Category("Rest", Colorizer.getRandomColor()));
            categories.add(new Category("Break", Colorizer.getRandomColor()));
            categories.add(new Category("Work", Colorizer.getRandomColor()));
            categories.add(new Category("Health", Colorizer.getRandomColor()));
            categories.add(new Category("Social media", Colorizer.getRandomColor()));
            categories.add(new Category("Computer", Colorizer.getRandomColor()));
            categories.add(new Category("Study", Colorizer.getRandomColor()));
            categories.add(new Category("Chores", Colorizer.getRandomColor()));
            categories.add(new Category("Hobbies", Colorizer.getRandomColor()));
            categories.add(new Category("Relationship", Colorizer.getRandomColor()));

            // Setup default activities
            List<Activity> activities = new ArrayList<>();

            CategoryDBHelper categoryHelper = CategoryDBHelper.getInstance();
            for (Category category : categories) {
                categoryHelper.insert(category);
            }
        }

        // Start home activity
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


}
