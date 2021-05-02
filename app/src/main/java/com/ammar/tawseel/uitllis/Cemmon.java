package com.ammar.tawseel.uitllis;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.util.Locale;

public class Cemmon {
public static  String FIREBASE_TOKEN;

    public static void setLocale(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        Resources res =context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


    }
    public  static double latude;
    public  static double langtude;

}