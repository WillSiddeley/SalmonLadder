package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SalmonLadderTutorials {

    private static final String TUTORIAL_FILE = "Tutorials";
    private static final String TUTORIAL_COMPLETED = "Tutorial";

    private Preferences preferences;

    SalmonLadderTutorials() {

    }

    private Preferences getPreferences() {

        if (preferences == null) {

            preferences = Gdx.app.getPreferences(TUTORIAL_FILE);

        }

        return preferences;

    }

    boolean getTutorialCompeted(int levelNumber) {

        return getPreferences().getBoolean(TUTORIAL_COMPLETED + levelNumber, false);

    }

    void setTutorialCompleted(int levelNumber, boolean completed) {

        getPreferences().putBoolean(TUTORIAL_COMPLETED + levelNumber, completed);
        getPreferences().flush();

    }

}
