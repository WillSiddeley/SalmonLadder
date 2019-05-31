package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.spill.salmonladder.SalmonLadderPreferences;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PopUpMenu extends Table {

    private SalmonLadderPreferences preferences;

    private int height = Gdx.graphics.getHeight();

    private int width = Gdx.graphics.getWidth();

    private float widthCenter;

    private float heightCenter;

    public PopUpMenu(float heightModifier, float widthModifier, String initialPlacement) {

        preferences = new SalmonLadderPreferences();

        this.setVisible(false);

        this.setHeight(height / heightModifier);

        this.setWidth(width / widthModifier);

        this.widthCenter = (Gdx.graphics.getWidth() / 2f) - (this.getWidth() / 2f);

        this.heightCenter = (Gdx.graphics.getHeight() / 2f) - (this.getHeight() / 2f);

        if (initialPlacement.equals("up")) {

            this.setPosition(widthCenter, height * 2f);

        } else if (initialPlacement.equals("down")) {

            this.setPosition(widthCenter, height * -2f);

        } else if (initialPlacement.equals("left")) {

            this.setPosition(width * -2f, heightCenter);

        } else if (initialPlacement.equals("right")) {

            this.setPosition(width * 2f, heightCenter);

        } else {

            this.setPosition(widthCenter, heightCenter);

        }

    }

    public void setNinePatchBG(String internalPath) {

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal(internalPath)), 8, 8, 8, 8);

        NinePatchDrawable background = new NinePatchDrawable(patch);

        this.setBackground(background);

    }

    public void bringToCenter(float duration) {

        this.setVisible(true);

        LevelParser.screenLock = true;

        this.addAction(sequence(moveTo(widthCenter, heightCenter, duration), run(new Runnable() {
            @Override
            public void run() {

                LevelParser.inMenu = true;

            }
        })));

    }

    public void bringUp(float duration) {

        this.addAction(sequence(moveTo(widthCenter, height * 2f, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

                LevelParser.inMenu = false;

            }
        })));

    }


    public void bringLeft(float duration) {

        this.addAction(sequence(moveTo(width * -2f, heightCenter, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

                LevelParser.inMenu = false;

            }
        })));

    }

    public void bringRight(float duration) {

        this.addAction(sequence(moveTo(width * 2f, heightCenter, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

                LevelParser.inMenu = false;

            }
        })));

    }

    public void createPauseMenu() {

        Label pauseLabel = new Label("Game Paused!", new Skin(Gdx.files.internal("Skins/uiskin.json")));

        pauseLabel.setStyle(new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Skins/MovesFont.fnt")), Color.BLACK));

        pauseLabel.setFontScale(1.5f);

        Drawable soundUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonSoundUp.png"))));

        Drawable soundDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonSoundDown.png"))));

        Drawable musicUp = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonMusicUp.png"))));

        Drawable musicDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonMusicDown.png"))));

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonLevels.png"))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonRestart.png"))));

        Drawable resume = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonResume.png"))));

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

                    System.out.println("Turned sound off");

                } else {

                    // turn Sound on

                    preferences.setSoundEnabled(true);

                    buttonSound.setChecked(false);

                    System.out.println("Turned sound on");

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

                    System.out.println("Turned music off");

                } else {

                    // turn Music on

                    preferences.setMusicEnabled(true);

                    buttonMusic.setChecked(false);

                    System.out.println("Turned music on");

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

                if (LevelParser.PauseTable.isVisible()) {

                    LevelParser.PauseTable.bringUp(0.3f);

                } else {

                    LevelParser.PauseTable.bringToCenter(0.3f);

                }

            }

        });

        this.row().expand().center();

        this.add(pauseLabel).colspan(3);

        this.row().expand().center();

        Table middleRow = new Table();

        middleRow.add(buttonSound).width(Value.percentWidth(0.4f, this)).height(Value.percentHeight(0.3f, this));

        middleRow.add(buttonMusic).width(Value.percentWidth(0.4f, this)).height(Value.percentHeight(0.3f, this));

        this.add(middleRow).colspan(3);

        this.row().expand();

        this.add(buttonLevels).width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));

        this.add(buttonRestart).width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));

        this.add(buttonResume).width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));

    }

    public void createWinMenu(int stars) {

        Label winLabel = new Label("Level Complete!", new Skin(Gdx.files.internal("Skins/uiskin.json")));

        winLabel.setStyle(new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Skins/MovesFont.fnt")), Color.BLACK));

        winLabel.setFontScale(1.25f);

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonLevels.png"))));

        Drawable nextLevel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ButtonNextLevel.png"))));

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

                if (LevelParser.levelNumber == ScreenLevelSelect.levelCount) {

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
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                break;

            case 1:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                break;

            case 2:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStarEmpty.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                break;

            case 3:
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                this.add(new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/ImageStar.png"))))))
                        .width(Value.percentWidth(0.3f, this)).height(Value.percentHeight(0.25f, this));
                break;
        }

        this.row().expand();

        Table bottomRow = new Table();

        bottomRow.add(buttonLevels).width(Value.percentWidth(0.4f, this)).height(Value.percentHeight(0.3f, this));

        bottomRow.add(buttonNextLevel).width(Value.percentWidth(0.4f, this)).height(Value.percentHeight(0.3f, this));

        this.add(bottomRow).colspan(3);

    }

}
