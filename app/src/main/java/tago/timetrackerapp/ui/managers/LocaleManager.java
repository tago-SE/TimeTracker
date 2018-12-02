package tago.timetrackerapp.ui.managers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

/**
 * Needs to be updated onCreate and onConfigurationChanged
 */
public class LocaleManager {

    private static String cacheLanguage;

    public static void setLocale(Context c) {
        setNewLocale(c, getLanguage(c));
    }

    public static void setNewLocale(Context c, String language) {
        cacheLanguage = language;
        persistLanguage(c, language);
        updateResources(c, language);
    }


    public static String getLanguage(Context c) {
        return cacheLanguage;
    }

    private static void persistLanguage(Context c, String language) {
        // Not yet implemented
    }

    private static void updateResources(Context context, String language) {
        System.out.println("UpdateLocale: " + language);
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}