package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SalmonLadderStars {

    private static final String STARS_FILE = "Stars";
    private static final String STATUS = "Status";
    private static final String STARS = "Stars";

    private Preferences preferences;

    public SalmonLadderStars() {

    }

    protected Preferences getPreferences() {

        if (preferences == null) {

            preferences = Gdx.app.getPreferences(STARS_FILE);

        }

        return preferences;

    }

    public String getStatus(int levelNumber) {

        return getPreferences().getString(STATUS + levelNumber, "Locked");

    }

    public void setStatus(int levelNumber, String status) {

        getPreferences().putString(STATUS + levelNumber, status);
        getPreferences().flush();

    }

    public int getStars(int levelNumber) {

        return getPreferences().getInteger(STARS + levelNumber, 0);

    }

    public void setStars(int levelNumber, int stars) {

        getPreferences().putInteger(STARS + levelNumber, stars);
        getPreferences().flush();

    }

}
