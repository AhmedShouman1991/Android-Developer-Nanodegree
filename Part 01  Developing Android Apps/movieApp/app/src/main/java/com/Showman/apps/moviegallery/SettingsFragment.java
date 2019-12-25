package com.Showman.apps.moviegallery;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.Showman.apps.moviegallery.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.main_pref);
    }
}
