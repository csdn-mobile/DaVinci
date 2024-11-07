package net.csdn.davinci.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferenceUtil {

    public static final String STATUS_PERMISSION_GRANTED = "PERMISSION_GRANTED";

    public static void putSharedPreferencesBoolean(Context context, String key, boolean val) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, val);
        edit.commit();
    }

    public static boolean getSharedPreferencesBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }


}
