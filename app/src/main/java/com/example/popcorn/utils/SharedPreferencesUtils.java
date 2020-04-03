package com.example.popcorn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONArray;

public class SharedPreferencesUtils {

    private static final String MOVIES_PREF = "movies";

    public static void storeData(Context context, JSONArray jsonArray){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MOVIES_PREF, 0);
        Editor editor = sharedPreferences.edit();

        editor.putString("movies", jsonArray.toString());
        editor.apply();
    }
}
