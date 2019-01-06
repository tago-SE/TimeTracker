package tago.timetrackerapp.ui.managers;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

// Reference: https://proandroiddev.com/change-language-programmatically-at-runtime-on-android-5e6bc15c758

/**
 * Used to manage language inside the app by overriding the device language on each activity cycle
 * by executing setLocale or setNewLocale.
 */
public class LocaleManager {

    private static String languageTag;
    private static Locale locale;
    /**
     * Resets the current Locale language to the one stored in cache, used to override the default
     * user language. This means that it must be called every time the layout is drawn.
     * @param c context
     */
    public static void setLocale(Context c) {
        setNewLocale(c, languageTag);
    }

    /**
     * Changes the language of the current Locale. Use this to change the current language of the
     * Locale.
     * @param c context
     * @param language 2 letter Locale country / language
     */
    public static void setNewLocale(Context c, String language) {
        languageTag = language;
        if (language != null)
            updateResources(c, language);
    }

    /**
     * Returns the current cached language.
     * @param c context
     * @return 2 letter Locale country / language
     */
    public static String getLanguage(Context c) {
        if (languageTag == null) {
            return Locale.getDefault().getLanguage();
        }
        return languageTag;
    }

    private static void updateResources(Context context, String language) {
        locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static Locale getLocale() {
        return locale;
    }
}