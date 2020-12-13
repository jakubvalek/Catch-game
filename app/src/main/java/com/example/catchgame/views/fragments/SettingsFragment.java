package com.example.catchgame.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;

import com.example.catchgame.R;
import com.example.catchgame.services.MusicService;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Preference musicPreference = findPreference(getString(R.string.musicVolume_key));
        musicPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent musicIntent = new Intent();
                musicIntent.setAction(MusicService.CHANGE_MUSIC_VOLUME);
                musicIntent.putExtra(MusicService.CHANGE_MUSIC_VOLUME, (int)newValue);
                getActivity().sendBroadcast(musicIntent);
                return true;
            }
        });
        Preference soundPreference = findPreference(getString(R.string.soundVolume_key));
        soundPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Intent soundIntent = new Intent();
                soundIntent.setAction(MusicService.CHANGE_SOUND_VOLUME);
                soundIntent.putExtra(MusicService.CHANGE_SOUND_VOLUME, (int)newValue);
                getActivity().sendBroadcast(soundIntent);
                return true;
            }
        });
    }
}