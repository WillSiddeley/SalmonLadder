package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

class SalmonLadderSettings {

    private static final String SETTINGS_FILE = "Settings";
    private static final String SOUND = "Sound";
    private static final String MUSIC = "Music";

    private Preferences preferences;

    SalmonLadderSettings() {

    }

    private Preferences getPreferences() {

        if (preferences == null) {

            preferences = Gdx.app.getPreferences(SETTINGS_FILE);

        }

        return preferences;

    }

    boolean isSoundEnabled() {

        return getPreferences().getBoolean(SOUND, true);

    }

    void setSoundEnabled(boolean soundEnabled) {

        getPreferences().putBoolean(SOUND, soundEnabled);
        getPreferences().flush();

    }

    boolean isMusicEnabled() {

        return getPreferences().getBoolean(MUSIC, true);

    }

    void setMusicEnabled(boolean musicEnabled) {

        getPreferences().putBoolean(MUSIC, musicEnabled);
        getPreferences().flush();

    }

}
