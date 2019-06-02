package com.spill.salmonladder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

class MenuTutorial extends Menu {

    MenuTutorial(float heightModifier, float widthModifier, String ninePatchBG, String type) {

        super(heightModifier, widthModifier, ninePatchBG);

        if (type.equals("Start")) {

            this.createTutorial("Welcome to Salmon Ladder!", SalmonLadderConstants.IMAGE_PATH_TUTORIAL_START);

        } else if (type.equals("Rock")) {

            this.createTutorial("A Daunting First Test!", SalmonLadderConstants.IMAGE_PATH_TUTORIAL_ROCK);

        } else if (type.equals("Bear")) {

            this.createTutorial("A Wild Enemy Appears!", SalmonLadderConstants.IMAGE_PATH_TUTORIAL_BEAR);

        }

        this.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                LevelParser.TutorialTable.bringUp(SalmonLadderConstants.MENU_TYPE_TUTORIAL);

            }

        });

    }

    private void createTutorial(String message, String imagePath) {

        Label tutorialLabel = new Label(message, SalmonLadderConstants.SKIN);

        tutorialLabel.setStyle(new Label.LabelStyle(SalmonLadderConstants.FONT, Color.BLACK));

        tutorialLabel.setFontScale(1f);

        Image tutorialStart = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal(imagePath)))));

        this.row().expand().center();

        this.add();

        this.row().expand().center();

        this.add(tutorialLabel).colspan(3);

        this.row().expand().center();

        this.add(tutorialStart).colspan(3);


    }
}
