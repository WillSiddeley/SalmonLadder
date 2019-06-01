package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SalmonLadderSettings {

    private static final String SETTINGS_FILE = "Settings";
    private static final String SOUND = "Sound";
    private static final String MUSIC = "Music";

    private Preferences preferences;

    public SalmonLadderSettings() {

    }

    protected Preferences getPreferences() {

        if (preferences == null) {

            preferences = Gdx.app.getPreferences(SETTINGS_FILE);

        }

        return preferences;

    }

    public boolean isSoundEnabled() {

        return getPreferences().getBoolean(SOUND, true);

    }

    public void setSoundEnabled(boolean soundEnabled) {

        getPreferences().putBoolean(SOUND, soundEnabled);
        getPreferences().flush();

    }

    public boolean isMusicEnabled() {

        return getPreferences().getBoolean(MUSIC, true);

    }

    public void setMusicEnabled(boolean musicEnabled) {

        getPreferences().putBoolean(MUSIC, musicEnabled);
        getPreferences().flush();

    }

}
