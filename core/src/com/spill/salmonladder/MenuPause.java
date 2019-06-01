package com.spill.salmonladder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class MenuPause extends Menu {

    private SalmonLadderSettings preferences;

    MenuPause(float heightModifier, float widthModifier, String ninePatchBG) {

        super(heightModifier, widthModifier, ninePatchBG);

        preferences = new SalmonLadderSettings();

        Label pauseLabel = new Label("Game Paused!", SalmonLadderConstants.SKIN);

        pauseLabel.setStyle(new Label.LabelStyle(SalmonLadderConstants.FONT, Color.BLACK));

        pauseLabel.setFontScale(1.5f);

        Drawable soundUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_SOUND_UP))));

        Drawable soundDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_SOUND_DOWN))));

        Drawable musicUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_MUSIC_UP))));

        Drawable musicDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_MUSIC_DOWN))));

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_LEVELS))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_RESTART))));

        Drawable resume = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_RESUME))));

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

                LevelParser.PauseTable.bringUp(SalmonLadderConstants.MENU_TYPE_PAUSE);

            }

        });

        this.row().expand().center();

        this.add(pauseLabel).colspan(3);

        this.row().expand().center();

        Table middleRow = new Table();

        middleRow.add(buttonSound).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT_LARGE, this));

        middleRow.add(buttonMusic).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT_LARGE, this));

        this.add(middleRow).colspan(3);

        this.row().expand();

        this.add(buttonLevels).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.add(buttonRestart).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.add(buttonResume).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

    }

}