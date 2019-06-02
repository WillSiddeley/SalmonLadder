package com.spill.salmonladder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class MenuWin extends Menu {

    private Image Star1 = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR_EMPTY)))));

    private Image Star2 = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR_EMPTY)))));

    private Image Star3 = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR_EMPTY)))));

    MenuWin(float heightModifier, float widthModifier, String ninePatchBG) {

        super(heightModifier, widthModifier, ninePatchBG);

        Label winLabel = new Label("Level Complete!", SalmonLadderConstants.SKIN);

        winLabel.setStyle(new Label.LabelStyle(SalmonLadderConstants.FONT, Color.BLACK));

        winLabel.setFontScale(1.25f);

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_LEVELS))));

        Drawable nextLevel = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_NEXT_LEVEL))));

        ImageButton buttonLevels = new ImageButton(levels);

        ImageButton buttonNextLevel = new ImageButton(nextLevel);

        buttonLevels.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.resetBooleans(false);

                if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                    SalmonLadderConstants.SOUND_MENU_CLOSE.play();

                }

                ((Game) Gdx.app.getApplicationListener()).setScreen(new ScreenLevelSelect());

            }

        });

        buttonNextLevel.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (SalmonLadderConstants.SETTINGS.isSoundEnabled()) {

                    SalmonLadderConstants.SOUND_MENU_CLOSE.play();

                }

                if (LevelParser.levelNumber == SalmonLadderConstants.LEVEL_COUNT) {

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

        this.add(Star1).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.add(Star2).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.add(Star3).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.row().expand().center();

        Table bottomRow = new Table();

        bottomRow.add(buttonLevels).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT_LARGE, this));

        bottomRow.add(buttonNextLevel).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH_LARGE, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT_LARGE, this));

        this.add(bottomRow).colspan(3);

    }

    void updateStarDrawable(int stars) {

        switch (stars) {

            case 1:

                Star1.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));
                break;

            case 2:

                Star1.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));

                Star2.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));

                break;

            case 3:

                Star1.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));

                Star2.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));

                Star3.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_STAR)))));

                break;

            default:

                break;

        }

    }

}