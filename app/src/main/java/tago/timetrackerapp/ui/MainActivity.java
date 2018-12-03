package tago.timetrackerapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);


        String country = Locale.getDefault().getCountry();
        String lang = Locale.getDefault().getDisplayLanguage();
        Log.w(TAG, "LANGUAGE: " + lang);
        String language = prefs.getString("language", "");

        LocaleManager.setNewLocale(this, Locale.getDefault().getDisplayLanguage());
        // Start home activity
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


}
