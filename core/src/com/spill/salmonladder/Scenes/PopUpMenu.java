package com.spill.salmonladder.Scenes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class PopUpMenu extends Table {

    private int height = Gdx.graphics.getHeight();

    private int width = Gdx.graphics.getWidth();

    private float widthCenter;

    private float heightCenter;

    public PopUpMenu(float heightModifier, float widthModifier, String initialPlacement) {

        this.setVisible(false);

        this.setDebug(true);

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

        NinePatch patch = new NinePatch(new Texture(Gdx.files.internal(internalPath)), 10, 10, 10, 10);

        NinePatchDrawable background = new NinePatchDrawable(patch);

        this.setBackground(background);

    }

    public void bringToCenter(float duration) {

        this.setVisible(true);

        this.addAction(moveTo(widthCenter, heightCenter, duration));

        LevelParser.screenLock = true;

    }

    public void bringUp(float duration) {

        this.addAction(sequence(moveTo(widthCenter, height * 2f, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

            }
        })));

    }


    public void bringLeft(float duration) {

        this.addAction(sequence(moveTo(width * -2f, heightCenter, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

            }
        })));

    }

    public void bringRight(float duration) {

        this.addAction(sequence(moveTo(width * 2f, heightCenter, duration), run(new Runnable() {
            @Override
            public void run() {

                setVisible(false);

                LevelParser.screenLock = false;

            }
        })));

    }

    public void createPauseMenu() {

        this.row().expand();

        this.add();

        this.row().expand();

        this.add();

        this.row().expand();

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonLevels.png"))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonRestart.png"))));

        Drawable resume = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("Images/PauseButtonResume.png"))));

        ImageButton buttonLevels = new ImageButton(levels);

        ImageButton buttonRestart = new ImageButton(restart);

        ImageButton buttonResume = new ImageButton(resume);

        buttonLevels.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

                LevelParser.screenLock = false;

            }

        });

        buttonRestart.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                ((Game) Gdx.app.getApplicationListener()).setScreen(new LevelParser(LevelParser.levelNumber));

                LevelParser.screenLock = false;

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

        this.add(buttonLevels);

        this.add(buttonRestart);

        this.add(buttonResume);


    }

}
