package com.Showman.apps.moviegallery.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.Showman.apps.moviegallery.R;

public class SetupSharedPreference {

    public static boolean setupNotificationPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean statue = sharedPreferences.getBoolean(context.getString(R.string.show_notification_key),
                context.getResources().getBoolean(R.bool.pref_show_new_movies_notification_default));
        return statue == context.getResources().getBoolean(R.bool.pref_show_new_movies_notification_default);
    }

    public static String setupTheme(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPreferences.getString(context.getString(R.string.select_theme_list_key),
                context.getString(R.string.theme_dark_value));
    }


}
