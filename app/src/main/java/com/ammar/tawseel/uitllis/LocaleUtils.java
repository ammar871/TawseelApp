package com.ammar.tawseel.uitllis;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleUtils {

    public static final String ENGLISH = "en";
    public static final String FRENCH = "fr";
    public static final String SPANISH = "es";


    public static void initialize(Context context, @LocaleDef String defaultLanguage) {
        setLocale(context, defaultLanguage);
    }

    public static boolean setLocale(Context context, @LocaleDef String language) {
        return updateResources(context, language);
    }

    private static boolean updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        context.createConfigurationContext(configuration);
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return true;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, FRENCH, SPANISH})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = {ENGLISH, FRENCH, SPANISH};
    }


    private static SharedPreferences getDefaultSharedPreference(Context context) {
        if (PreferenceManager.getDefaultSharedPreferences(Application.getInstance().getApplicationContext()) != null)
            return PreferenceManager.getDefaultSharedPreferences(Application.getInstance().getApplicationContext());
        else
            return null;
    }

    public static void setSelectedLanguageId(String id){
        final SharedPreferences prefs = getDefaultSharedPreference(Application.getInstance().getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("app_language_id", id);
        editor.apply();
    }

    public static String getSelectedLanguageId(){
        return getDefaultSharedPreference(Application.getInstance().getApplicationContext())
                .getString("app_language_id", "en");
    }
}