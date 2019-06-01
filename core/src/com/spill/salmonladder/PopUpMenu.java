package com.spill.salmonladder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PopUpMenu extends Table {

    private final String IMAGE_PATH_BUTTON_LEVELS = "Images/ButtonLevels.png";

    private final String IMAGE_PATH_BUTTON_MUSIC_DOWN = "Images/ButtonMusicDown.png";

    private final String IMAGE_PATH_BUTTON_MUSIC_UP = "Images/ButtonMusicUp.png";

    private final String IMAGE_PATH_BUTTON_NEXT_LEVEL = "Images/ButtonNextLevel.png";

    private final String IMAGE_PATH_BUTTON_RESTART = "Images/ButtonRestart.png";

    private final String IMAGE_PATH_BUTTON_RESUME = "Images/ButtonResume.png";

    private final String IMAGE_PATH_BUTTON_SOUND_DOWN = "Images/ButtonSoundDown.png";

    private final String IMAGE_PATH_BUTTON_SOUND_UP = "Images/ButtonSoundUp.png";

    private final String IMAGE_PATH_STAR = "Images/ImageStar.png";

    private final String IMAGE_PATH_STAR_EMPTY = "Images/ImageStarEmpty.png";

    private final float PERCENTAGE_WIDTH = 0.3f;

    private final float PERCENTAGE_HEIGHT = 0.25f;

    private final float PERCENTAGE_WIDTH_LARGE = 0.4f;

    private final float PERCENTAGE_HEIGHT_LARGE = 0.3f;

    private SalmonLadderSettings preferences;

    private float widthCenter;

    private float heightCenter;

    public PopUpMenu(float heightModifier, float widthModifier) {

        preferences = new SalmonLadderSettings();

        this.setVisible(false);

        this.setWidth(Gdx.graphics.getWidth() / widthModifier);

        this.setHeight(Gdx.graphics.getHeight() / heightModifier);

        this.widthCenter = (Gdx.graphics.getWidth() / 2f) - (this.getWidth() / 2f);

        this.heightCenter = (Gdx.graphics.getHeight() / 2f) - (this.getHeight() / 2f);

        this.setPosition(widthCenter, Gdx.graphics.getHeight() * 2f);

    }

    public void setNinePatchBG(String internalPath) {

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal(internalPath)), 8, 8, 8, 8);

        NinePatchDrawable background = new NinePatchDrawable(patch);

        this.setBackground(background);

    }

    public void bringToCenter(float duration, String menu) {

        this.setVisible(true);

        if (menu.equals("die")) {

            LevelParser.inDeath = true;

        } else if (menu.equals("pause")) {

            LevelParser.inMenu = true;

        } else {

            LevelParser.inWin = true;

        }

        this.addAction(moveTo(widthCenter, heightCenter, duration));

    }

    public void bringUp(float duration, final String menu) {

        if (menu.equals("die")) {

            LevelParser.inDeath = false;

        } else if (menu.equals("pause")) {

            LevelParser.inMenu = false;

        } else {

            LevelParser.inWin = false;

        }

        this.addAction(sequence(moveTo(widthCenter, Gdx.graphics.getHeight() * 2f, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

            }
        })));

    }


    public void createPauseMenu() {

        Label pauseLabel = new Label("Game Paused!", SalmonLadder.SKIN);

        pauseLabel.setStyle(new Label.LabelStyle(SalmonLadder.FONT, Color.BLACK));

        pauseLabel.setFontScale(1.5f);

        Drawable soundUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_SOUND_UP))));

        Drawable soundDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_SOUND_DOWN))));

        Drawable musicUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_MUSIC_UP))));

        Drawable musicDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_MUSIC_DOWN))));

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_LEVELS))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_RESTART))));

        Drawable resume = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_RESUME))));

        final ImageButton buttonSound = new ImageButton(soundUp, soundUp, soundDown);

        final ImageButton buttonMusic = new ImageButton(musicUp, musicUp, musicDown);

        if (preferences.isSoundEnabled()) {

            buttonSound.setChecked(false);

        } else {

            buttonSound.setChecked(true);

        }

        if (preferences.isMusicEnabled()) {

            buttonMusic.setChecked(false);

        } else {

            buttonMusic.setChecked(true);

        }

        ImageButton buttonLevels = new ImageButton(levels);

        ImageButton buttonRestart = new ImageButton(restart);

        ImageButton buttonResume = new ImageButton(resume);


        buttonSound.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (preferences.isSoundEnabled()) {

                    // turn Sound off

                    preferences.setSoundEnabled(false);

                    buttonSound.setChecked(true);

                } else {

                    // turn Sound on

                    preferences.setSoundEnabled(true);

                    buttonSound.setChecked(false);

                }

            }

        });

        buttonMusic.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (preferences.isMusicEnabled()) {

                    // turn Music off

                    preferences.setMusicEnabled(false);

                    buttonMusic.setChecked(true);

                } else {

                    // turn Music on

                    preferences.setMusicEnabled(true);

                    buttonMusic.setChecked(false);

                }

            }


        });

        buttonLevels.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

            }

        });

        buttonRestart.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(LevelParser.levelNumber));

            }

        });

        buttonResume.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.PauseTable.bringUp(0.3f, "pause");

            }

        });

        this.row().expand().center();

        this.add(pauseLabel).colspan(3);

        this.row().expand().center();

        Table middleRow = new Table();

        middleRow.add(buttonSound).width(Value.percentWidth(PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT_LARGE, this));

        middleRow.add(buttonMusic).width(Value.percentWidth(PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT_LARGE, this));

        this.add(middleRow).colspan(3);

        this.row().expand();

        this.add(buttonLevels).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));

        this.add(buttonRestart).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));

        this.add(buttonResume).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));

    }

    public void createWinMenu(int stars) {

        Label winLabel = new Label("Level Complete!", SalmonLadder.SKIN);

        winLabel.setStyle(new Label.LabelStyle(SalmonLadder.FONT, Color.BLACK));

        winLabel.setFontScale(1.25f);

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_LEVELS))));

        Drawable nextLevel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_NEXT_LEVEL))));

        ImageButton buttonLevels = new ImageButton(levels);

        ImageButton buttonNextLevel = new ImageButton(nextLevel);

        buttonLevels.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

            }

        });

        buttonNextLevel.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (LevelParser.levelNumber == ScreenLevelSelect.LEVEL_COUNT) {

                    LevelParser.resetBooleans(false);

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

                } else {

                    LevelParser.resetBooleans(false);

                    ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(LevelParser.levelNumber + 1));


                }

            }

        });

        this.row().expand().center();

        this.add(winLabel).colspan(3);

        this.row().expand().center();

        switch (stars) {

            case 0:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                break;

            case 1:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                break;

            case 2:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR_EMPTY)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                break;

            case 3:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_STAR)))))).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));
                break;
        }

        this.row().expand();

        Table bottomRow = new Table();

        bottomRow.add(buttonLevels).width(Value.percentWidth(PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT_LARGE, this));

        bottomRow.add(buttonNextLevel).width(Value.percentWidth(PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT_LARGE, this));

        this.add(bottomRow).colspan(3);

    }

    public void createDeathMenu() {

        Label deathLabel = new Label("Game Over!", SalmonLadder.SKIN);

        deathLabel.setStyle(new Label.LabelStyle(SalmonLadder.FONT, Color.BLACK));

        deathLabel.setFontScale(1.25f);

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_LEVELS))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(IMAGE_PATH_BUTTON_RESTART))));

        ImageButton buttonLevels = new ImageButton(levels);

        ImageButton buttonRestart = new ImageButton(restart);

        buttonLevels.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

            }

        });

        buttonRestart.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(LevelParser.levelNumber));

            }

        });

        this.row().expand().center();

        this.add(deathLabel).colspan(3);

        this.row().expand().center();

        this.add(buttonLevels).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));

        this.add(buttonRestart).width(Value.percentWidth(PERCENTAGE_WIDTH, this)).height(Value.percentHeight(PERCENTAGE_HEIGHT, this));

    }

}