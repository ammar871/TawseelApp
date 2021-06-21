package com.ammar.tawseel.uitllis;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

public class Cemmon {
    public static String FIREBASE_TOKEN;
    public static String BASE_URL = "https://tawseel.pal-dev.com";
    public final static String BASE_URL_MAP = "https://appweb.website/100top/";

    public static String NAME_OF_USER;
    public static String IMAGE_OF_USER;
    public static String IMAGE_OF_DRIVER;
    public static String DATE_AND_TIME;
    public static String LONG_USER;
    public static void setLocale(Context context, String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());


    }

    public static double latude;
    public static double langtude;

    public static LatLng latLngUser;
    public static LatLng latLngDriver;

    public static boolean isNetworkOnline(Context context) {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
