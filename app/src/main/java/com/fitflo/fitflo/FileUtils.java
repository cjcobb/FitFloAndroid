package com.fitflo.fitflo;

import android.content.Context;
import android.content.SharedPreferences;

import static android.provider.Settings.Global.getString;

/**
 * Created by cj on 2/4/16.
 * used to read and write from files
 */
public class FileUtils {

    public static void writeString(Context context,String key, String val) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "FitFloPreferenceFile", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, val);
        editor.apply();
    }


    public static void writeBoolean(Context context, String key, boolean val) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "FitFloPreferenceFile", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, val);
        editor.apply();
    }



    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "FitFloPreferenceFile", Context.MODE_PRIVATE);
       return sharedPref.getString(key, "");
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                "FitFloPreferenceFile", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }
}
