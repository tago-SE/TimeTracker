package tago.timetrackerapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.MenuItem;

import tago.timetrackerapp.R;
import tago.timetrackerapp.ui.managers.LocaleManager;

public class SettingsActivity extends AppCompatActivity  {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onResume() {
        super.onResume();
        LocaleManager.setLocale(this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SettingsFragment.newInstance(this))
                .commit();

        // Setup ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.settings));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private Activity activity;

        public static final SettingsFragment newInstance(Activity activity) {
            Bundle args = new Bundle();
            SettingsFragment fragment = new SettingsFragment();
            fragment.setArguments(args);
            fragment.activity = activity;
            return fragment;
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String rootKey) {
            addPreferencesFromResource(R.xml.pref_general);
            // Language Preference
            ListPreference language = (ListPreference) getPreferenceScreen().findPreference("language");
            language.setSummary(language.getEntry());
            language.setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {
                        @Override
                        public boolean onPreferenceChange(Preference preference, Object o) {
                            Log.d(TAG, "languageChange: " + o.toString());
                            LocaleManager.setNewLocale(activity, o.toString());
                            activity.startActivity(new Intent(activity, SettingsActivity.class));
                            activity.finish();
                            return true;
                        }
                    }
            );
            //
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // Back button pressed
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
