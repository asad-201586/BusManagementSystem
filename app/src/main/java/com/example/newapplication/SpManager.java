package com.example.newapplication;

import android.content.Context;
import android.content.SharedPreferences;

public class SpManager {

    public static final String SP_SUBJECT_NAME = "subject_name";
    public static final String SP_IMAGE = "image";
    public static final String SP_SUBJECT_ID = "subject_id";
    public static final String SP_CHAPTER_ID = "chapter_id";
    public static final String SP_BANNER_ID = "banner_id";
    public static final String SP_CHAPTER_NAME = "chapter_name";
    public static final String SP_CLASS_PRICE = "price";
    public static final String SP_CLASS_NAME = "class_name";
    public static final String SP_PRACTICE_ID = "practice_id";
    public static final String SP_TOTAL_QUESTION = "total_question";
    public static final String SP_TOTAL_CORRECT = "total_correct";
    public static final String SP_TOTAL_SKIPPED = "total_skipped";
    public static final String SP_TOTAL_INCORRECT = "total_incorrect";
    public static final String SP_TOTAL_ATTEMPTED = "total_attempted";
    public static final String SP_CLASS_DURATION = "duration";
    public static final String SP_TOTAL_POST_LIVE_VIDEO = "total_post_video";
    public static final String SP_TOTAL_LECTURE_VIDEO = "total_lecture_video";


    private Context context;
    private static final String PREF = "bus_pref";
    public static final String ADMIN_LOGIN_STATUS = "name";


    public SpManager(Context context) {
        this.context = context;
    }

    public static void saveBoolean(Context context, String key, Boolean value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveString(Context context, String key, String Value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, Value);
        editor.apply();

    }


    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static Boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "DNF");
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPref.getString(key, defaultValue);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPrf = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPrf.getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences sharedPrf = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return sharedPrf.getInt(key, defaultValue);
    }


    public static void clearData(Context context) {
        SharedPreferences sharedPrf = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrf.edit();
        editor.clear();
        editor.apply();
    }


}
