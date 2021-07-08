package com.ammar.tawseel.editor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ammar.tawseel.netWorke.APIClient;
import com.ammar.tawseel.netWorke.APIInterFace;
import com.ammar.tawseel.pojo.data.DataUser;
import com.ammar.tawseel.ui.SplashActivity;
import com.ammar.tawseel.ui.auth.LoginActivity;

import java.util.HashMap;

public class ShardEditor {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    APIInterFace apiInterFace;
    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String FILE_NAME = "coursatApp";
    public static final String KEY_NAME = "name";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_LOCATION = "address";
    public static final String KEY_EMAIL = "email";
    public static final String IS_LOGIN_WITH = "login";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_LANG = "language";
    public static final String KEY_IMAGE= "image";
    public static final String SELECT_SHOW = "show";

    public static final String ORDER_ID = "order_id";
    public static final String IS_CLOSE_POP = "close";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String IS_TOKEN ="token" ;
    public ShardEditor(Context context) {
        apiInterFace = APIClient.getClient().create(APIInterFace.class);
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public void saveData(DataUser muser) {

        editor.putString(KEY_NAME, muser.getName());
        editor.putString(KEY_USER_PHONE, muser.getPhone());
        editor.putString(KEY_EMAIL, muser.getEmail());
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
    public HashMap<String, String> loadData() {
        HashMap<String, String> userData = new HashMap<>();
        userData.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        userData.put(KEY_USER_PHONE, pref.getString(KEY_USER_PHONE, ""));
        userData.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        userData.put(KEY_IMAGE, pref.getString(KEY_IMAGE, ""));
        userData.put(KEY_TOKEN, pref.getString(KEY_TOKEN, ""));
        userData.put(KEY_LOCATION, pref.getString(KEY_LOCATION, ""));
        userData.put(KEY_LANG, pref.getString(KEY_LANG, ""));
        userData.put(ORDER_ID, pref.getString(ORDER_ID, ""));


        return userData;
    }
    public void saveDataLoginWith(boolean entred) {

        editor.putBoolean(IS_LOGIN_WITH, entred);
        editor.commit();
    }
    public void saveToken(String token) {

        editor.putString(IS_TOKEN, token);
        editor.commit();
    }

    public void saveLang(String lang) {

        editor.putString(KEY_LANG, lang);
        editor.commit();
    }

    public void loadLang(int lang) {

        pref.getString(KEY_LANG, "");
        editor.commit();
    }
    public void saveOrderId(String orderId) {

        editor.putString(ORDER_ID, orderId);
        editor.commit();
    }


    public HashMap<String, Boolean> loadDataEnter() {
        HashMap<String, Boolean> userData = new HashMap<>();
        userData.put(IS_LOGIN_WITH, pref.getBoolean(IS_LOGIN_WITH, false));

        return userData;
    }

    //select Show
    public void saveSelect(int number) {

        editor.putInt(SELECT_SHOW, number);
        editor.commit();
    }
    public HashMap<String, Integer> loadSelect() {
        HashMap<String, Integer> userData = new HashMap<>();
        userData.put(SELECT_SHOW, pref.getInt(SELECT_SHOW, 0));

        return userData;
    }



    //close Pop
    public HashMap<String, Boolean> loadDataClosing() {
        HashMap<String, Boolean> userData = new HashMap<>();
        userData.put(IS_CLOSE_POP, pref.getBoolean(IS_CLOSE_POP, false));

        return userData;
    }


    public void saveClose(boolean entred) {

        editor.putBoolean(IS_CLOSE_POP, entred);
        editor.commit();
    }
    public void logOut() {
       editor.remove(IS_LOGIN_WITH).apply();
        editor.commit();
        Intent mIntent = new Intent(_context, SplashActivity.class);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(mIntent);

    }




}
