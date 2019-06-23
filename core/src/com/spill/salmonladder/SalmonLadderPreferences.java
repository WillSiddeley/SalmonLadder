package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

class SalmonLadderPreferences {

    private static final String FILE = "Preferences";

    private static final String BEST = "Best";
    private static final String MUSIC = "Music";
    private static final String SOUND = "Sound";
    private static final String STARS = "Stars";
    private static final String STATUS = "Status";
    private static final String TUTORIAL_COMPLETED = "Tutorial";

    private Preferences preferences;

    SalmonLadderPreferences() {

    }

    private Preferences getPreferences() {

        if (preferences == null) {

            preferences = Gdx.app.getPreferences(FILE);

        }

        return preferences;

    }

    // MUSIC AND SOUNDS

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

    // STARS AND STATUS

    String getStatus(int levelNumber) {

        return getPreferences().getString(STATUS + levelNumber, "Locked");

    }

    void setStatus(int levelNumber, String status) {

        getPreferences().putString(STATUS + levelNumber, status);
        getPreferences().flush();

    }

    int getStars(int levelNumber) {

        return getPreferences().getInteger(STARS + levelNumber, 0);

    }

    void setStars(int levelNumber, int stars) {

        getPreferences().putInteger(STARS + levelNumber, stars);
        getPreferences().flush();

    }

    // BEST MOVES

    int getBestMoves(int levelNumber) {

        return getPreferences().getInteger(BEST + levelNumber, -1);

    }

    void setBestMoves(int levelNumber, int best) {

        getPreferences().putInteger(BEST + levelNumber, best);
        getPreferences().flush();

    }

    // TUTORIALS

    boolean getTutorialCompeted(int levelNumber) {

        return getPreferences().getBoolean(TUTORIAL_COMPLETED + levelNumber, false);

    }

    void setTutorialCompleted(int levelNumber, boolean completed) {

        getPreferences().putBoolean(TUTORIAL_COMPLETED + levelNumber, completed);
        getPreferences().flush();

    }
}
