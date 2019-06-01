package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SalmonLadderConstants {

    static final float PIXEL_PER_METER = 32f;

    static final float PERCENTAGE_WIDTH = 0.3f;

    static final float PERCENTAGE_HEIGHT = 0.25f;

    static final float PERCENTAGE_WIDTH_LARGE = 0.4f;

    static final float PERCENTAGE_HEIGHT_LARGE = 0.3f;

    static final float ANIMATION_DURATION = 0.3f;

    static final String BACKGROUND_DIE = "Images/BackgroundDie.png";

    static final String BACKGROUND_PAUSE = "Images/BackgroundPause.png";

    static final String BACKGROUND_WIN = "Images/BackgroundWin.png";

    static final String IMAGE_PATH_BUTTON_LEVELS = "Images/ButtonLevels.png";

    static final String IMAGE_PATH_BUTTON_MUSIC_DOWN = "Images/ButtonMusicDown.png";

    static final String IMAGE_PATH_BUTTON_MUSIC_UP = "Images/ButtonMusicUp.png";

    static final String IMAGE_PATH_BUTTON_NEXT_LEVEL = "Images/ButtonNextLevel.png";

    static final String IMAGE_PATH_BUTTON_PAUSE = "Images/ButtonPause.png";

    static final String IMAGE_PATH_BUTTON_RESTART = "Images/ButtonRestart.png";

    static final String IMAGE_PATH_BUTTON_RESUME = "Images/ButtonResume.png";

    static final String IMAGE_PATH_BUTTON_SOUND_DOWN = "Images/ButtonSoundDown.png";

    static final String IMAGE_PATH_BUTTON_SOUND_UP = "Images/ButtonSoundUp.png";

    static final String IMAGE_PATH_STAR = "Images/ImageStar.png";

    static final String IMAGE_PATH_STAR_EMPTY = "Images/ImageStarEmpty.png";

    static final String IMAGE_PATH_TITLE = "Images/StartScreenTitle.png";

    static final String IMAGE_PATH_SPLASH = "Images/StartScreenSplash.png";

    static final int ROWS = 7;

    static final int COLUMNS = 4;

    static final int PAGES = 1;

    static final int LEVEL_COUNT = (ROWS * COLUMNS) * PAGES;

    static Skin SKIN = new Skin(Gdx.files.internal("Skins/uiskin.json"));

    static BitmapFont FONT = new BitmapFont(Gdx.files.internal("Skins/MovesFont.fnt"));

}
