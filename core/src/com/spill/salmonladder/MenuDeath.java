package com.spill.salmonladder;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class MenuDeath extends Menu {

    MenuDeath(float heightModifier, float widthModifier, String ninePatchBG) {

        super(heightModifier, widthModifier, ninePatchBG);

        Label deathLabel = new Label("Game Over!", SalmonLadderConstants.SKIN);

        deathLabel.setStyle(new Label.LabelStyle(SalmonLadderConstants.FONT, Color.BLACK));

        deathLabel.setFontScale(1.5f);

        Drawable levels = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_LEVELS))));

        Drawable restart = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(SalmonLadderConstants.IMAGE_PATH_BUTTON_RESTART))));

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

        this.add(buttonLevels).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

        this.add(buttonRestart).width(Value.percentWidth(SalmonLadderConstants.PERCENTAGE_WIDTH, this)).height(Value.percentHeight(SalmonLadderConstants.PERCENTAGE_HEIGHT, this));

    }

}
