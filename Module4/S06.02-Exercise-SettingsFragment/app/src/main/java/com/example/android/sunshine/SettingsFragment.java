package com.example.android.sunshine;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preference);

        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen ps = getPreferenceScreen();
        int count = ps.getPreferenceCount();

        for(int i = 0; i < count;i++) {
            Preference p = ps.getPreference(i);
            if(!(p instanceof CheckBoxPreference)) {
                String val = sp.getString(p.getKey(), "");
                setPreferenceSummary(p, val);
            }
        }

    }
    private void setPreferenceSummary(Preference preference, Object value) {
        String stringVal = value.toString();
        String key = preference.getKey();
        if (preference instanceof ListPreference) {
            ListPreference lp = (ListPreference) preference;
            int prefIndex = lp.findIndexOfValue(stringVal);
            if(prefIndex >= 0) {
                preference.setSummary(lp.getEntries()[prefIndex]);
            }
        } else {
            preference.setSummary(stringVal);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(null != preference) {
            if(!(preference instanceof CheckBoxPreference)) {
                setPreferenceSummary(preference,sharedPreferences.getString(key, ""));
            }
        }
    }
}
